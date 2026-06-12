package pl.dabrowski.electrotools.ai.service;

import com.google.genai.Client;
import com.google.genai.types.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dabrowski.electrotools.ai.event.*;
import pl.dabrowski.electrotools.project.ProjectTools;
import pl.dabrowski.electrotools.project.executors.*;
import pl.dabrowski.electrotools.project.service.create.CreateProjectService;
import pl.dabrowski.electrotools.project.service.delete.DeleteProjectService;
import pl.dabrowski.electrotools.project.service.read.ReadProjectService;
import pl.dabrowski.electrotools.project.service.update.UpdateProjectService;
import pl.dabrowski.electrotools.utils.GenAiUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AiService {
    private static final String MODEL = "gemini-2.5-flash";
    private static final int MAX_TOOL_ROUNDS = 10;
    private final GenerateContentConfig config = GenerateContentConfig.builder()
            .tools(Tool.builder()
                    .functionDeclarations(List.of(
                            ProjectTools.PROJECT_FIND_ALL_TOOL,
                            ProjectTools.PROJECT_PAGE_ALL_TOOL,
                            ProjectTools.PROJECT_FIND_BY_ID_TOOL,
                            ProjectTools.PROJECT_FIND_DISTINCT_VALUES_TOOL,
                            ProjectTools.PROJECT_CREATE_TOOL,
                            ProjectTools.PROJECT_UPDATE_TOOL,
                            ProjectTools.PROJECT_DELETE_ALL_BY_ID_TOOL
                    ))
                    .build())
            .build();

    private final Client client;
    private final ReadProjectService readProjectService;
    private final CreateProjectService createProjectService;
    private final UpdateProjectService updateProjectService;
    private final DeleteProjectService deleteProjectService;
    private final ObjectMapper objectMapper;

    private final Map<String, ToolExecutor> toolExecutors = new HashMap<>();

    @PostConstruct
    public void init() {
        toolExecutors.put(ProjectTools.PROJECT_FIND_ALL_TOOL.name().orElseThrow(), new FindAllToolExecutor(readProjectService, objectMapper));
        toolExecutors.put(ProjectTools.PROJECT_PAGE_ALL_TOOL.name().orElseThrow(), new PageAllToolExecutor(readProjectService, objectMapper));
        toolExecutors.put(ProjectTools.PROJECT_FIND_BY_ID_TOOL.name().orElseThrow(), new FindByIdToolExecutor(readProjectService, objectMapper));
        toolExecutors.put(ProjectTools.PROJECT_CREATE_TOOL.name().orElseThrow(), new CreateToolExecutor(createProjectService));
        toolExecutors.put(ProjectTools.PROJECT_UPDATE_TOOL.name().orElseThrow(), new UpdateToolExecutor(updateProjectService));
        toolExecutors.put(ProjectTools.PROJECT_DELETE_ALL_BY_ID_TOOL.name().orElseThrow(), new DeleteAllToolExecutor(deleteProjectService));
    }

    public String handle(String prompt) {
        List<Content> contents = new ArrayList<>();
        contents.add(GenAiUtils.createPromptContent(prompt));

        for (int i = 0; i < MAX_TOOL_ROUNDS; i++) {
            GenerateContentResponse response = client.models.generateContent(MODEL, contents, config);

            var call = response.functionCalls();

            if (call == null || call.isEmpty()) {
                return response.text();
            }

            for (int j = 0; j < call.size(); j++) {
                FunctionCall functionCall = call.get(j);

                FunctionResponse responsePart = toolExecutors.get(functionCall.name().orElseThrow()).execute(functionCall);
                contents.add(GenAiUtils.createModelFunctionCallContent(functionCall));
                contents.add(GenAiUtils.createResponseContent(responsePart));
            }
        }
        throw new IllegalStateException("Max tool rounds reached");
    }

    public Flux<AgentEvent> handleStream(String prompt) {
        List<Content> contents = new ArrayList<>();
        contents.add(GenAiUtils.createPromptContent(prompt));

        return runRound(new AgentState(contents, 0));
    }

    private Flux<AgentEvent> runRound(AgentState state) {
        if (state.round() >= MAX_TOOL_ROUNDS) {
            return Flux.just(new ErrorEvent("Max tool rounds reached"));
        }

        return generate(state.contents())
                .flatMapMany(response -> {
                    var calls = response.functionCalls();
                    if (calls == null || calls.isEmpty()) {
                        return Flux.just(new TokenEvent(response.text()));
                    }

                    return Flux.fromIterable(calls)
                            .concatMap(call -> {
                                String name = call.name().orElseThrow();
                                AgentEvent toolCall = new ToolCallEvent(name, call.args());

                                long start = System.currentTimeMillis();

                                return executeTool(call)
                                        .flatMapMany(toolResponse -> {
                                            state.contents().add(GenAiUtils.createModelFunctionCallContent(call));
                                            state.contents().add(GenAiUtils.createResponseContent(toolResponse));

                                            AgentEvent toolResult = new ToolResultEvent(
                                                    name,
                                                    (System.currentTimeMillis() - start) / 1000f,
                                                    toolResponse.response());

                                            return Flux.just(toolCall, toolResult);
                                        });
                            })
                            .concatWith(Flux.defer(() -> runRound(new AgentState(state.contents(), state.round() + 1))));
                });
    }

    private Mono<GenerateContentResponse> generate(List<Content> contents) {
        return Mono.fromCallable(() ->
                        client.models.generateContent(MODEL, contents, config))
                .subscribeOn(Schedulers.boundedElastic());
    }

    private Mono<FunctionResponse> executeTool(FunctionCall call) {
        return Mono.fromCallable(() ->
                        toolExecutors.get(call.name().orElseThrow()).execute(call))
                .subscribeOn(Schedulers.boundedElastic());
    }
}
