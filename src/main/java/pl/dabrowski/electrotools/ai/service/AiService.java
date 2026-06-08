package pl.dabrowski.electrotools.ai.service;

import com.google.genai.Client;
import com.google.genai.types.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dabrowski.electrotools.project.ProjectController;
import pl.dabrowski.electrotools.project.service.read.ReadProjectService;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AiService {
    private final String model = "gemini-2.5-flash";
    private final GenerateContentConfig config = GenerateContentConfig.builder()
            .tools(Tool.builder()
                    .functionDeclarations(List.of(
                            ProjectController.PROJECT_FIND_ALL_TOOL,
                            ProjectController.PROJECT_PAGE_ALL_TOOL,
                            ProjectController.PROJECT_FIND_BY_ID_TOOL,
                            ProjectController.PROJECT_FIND_DISTINCT_VALUES_TOOL,
                            ProjectController.PROJECT_CREATE_TOOL,
                            ProjectController.PROJECT_UPDATE_TOOL,
                            ProjectController.PROJECT_DELETE_ALL_BY_ID_TOOL
                    ))
                    .build())
            .build();

    private final Client client;
    private final ReadProjectService readProjectService;
    private final ObjectMapper objectMapper;

    public String handle(String prompt) {
        GenerateContentResponse response = client.models.generateContent(model, prompt, config);

        if (response.functionCalls() == null || response.functionCalls().isEmpty()) {
            return response.text();
        }


        var functionCall = response.functionCalls().getFirst();
        System.out.println("Function call: " + functionCall.name());
        System.out.println("Arguments: " + functionCall.args());

        if (functionCall.name().isEmpty()) {
            throw new IllegalArgumentException("Function call name is empty");
        }

        FunctionResponse responsePart = null;
        var functionName = functionCall.name().get();
        if (functionName.equals(ProjectController.PROJECT_FIND_ALL_TOOL.name().get())) {
            var projects = readProjectService.findAll();

            responsePart = FunctionResponse.builder()
                    .name(ProjectController.PROJECT_FIND_ALL_TOOL.name().get())
                    .response(Map.of(
                            "result",
                            objectMapper.writeValueAsString(projects)
                    ))
                    .build();
        } else if (functionName.equals(ProjectController.PROJECT_PAGE_ALL_TOOL.name().get())) {
            var args = functionCall.args().get();
            var page = (Integer) args.get("page");
            var size = (Integer) args.get("size");
            var query = (String) args.get("query");

            var result = readProjectService.pageAll(
                    org.springframework.data.domain.PageRequest.of(page, size),
                    query == null ? java.util.Optional.empty() : java.util.Optional.of(query),
                    null
            );

            responsePart = FunctionResponse.builder()
                    .name(ProjectController.PROJECT_PAGE_ALL_TOOL.name().get())
                    .response(Map.of(
                            "result",
                            objectMapper.writeValueAsString(result)
                    ))
                    .build();
        } else if (functionName.equals(ProjectController.PROJECT_FIND_BY_ID_TOOL.name().get())) {
            var args = functionCall.args().get();
            var id = (String) args.get("id");
            var project = readProjectService.findById(UUID.fromString(id));

            responsePart = FunctionResponse.builder()
                    .name(ProjectController.PROJECT_FIND_BY_ID_TOOL.name().get())
                    .response(Map.of(
                            "result",
                            objectMapper.writeValueAsString(project)
                    ))
                    .build();
        }

        Content modelContent = Content.builder()
                .role("model")
                .parts(List.of(
                        Part.builder()
                                .functionCall(functionCall)
                                .build()
                ))
                .build();

        Content toolContent = Content.builder()
                .role("user")
                .parts(List.of(
                        Part.builder()
                                .functionResponse(responsePart)
                                .build()
                ))
                .build();

        Content promptContent = Content.builder()
                .role("user")
                .parts(List.of(
                        Part.builder()
                                .text(prompt)
                                .build()
                ))
                .build();

        GenerateContentResponse finalResponse = client.models.generateContent(
                model,
                List.of(
                        promptContent,
                        modelContent,
                        toolContent
                ),
                config
        );

        return finalResponse.text();
    }
}
