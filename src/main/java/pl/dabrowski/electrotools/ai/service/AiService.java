package pl.dabrowski.electrotools.ai.service;

import com.google.genai.Client;
import com.google.genai.types.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dabrowski.electrotools.project.ProjectTools;
import pl.dabrowski.electrotools.project.executors.*;
import pl.dabrowski.electrotools.project.service.create.CreateProjectService;
import pl.dabrowski.electrotools.project.service.delete.DeleteProjectService;
import pl.dabrowski.electrotools.project.service.read.ReadProjectService;
import pl.dabrowski.electrotools.project.service.update.UpdateProjectService;
import pl.dabrowski.electrotools.utils.GenAiUtils;
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

                System.out.printf("Round: %d - %s with args:%s%n", i + 1, functionCall.name(), functionCall.args());
                FunctionResponse responsePart = toolExecutors.get(functionCall.name().orElseThrow()).execute(functionCall);
                contents.add(GenAiUtils.createModelFunctionCallContent(functionCall));
                contents.add(GenAiUtils.createResponseContent(responsePart));
            }
        }
        throw new IllegalStateException("Max tool rounds reached");
    }
}
