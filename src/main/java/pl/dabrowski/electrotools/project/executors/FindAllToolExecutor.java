package pl.dabrowski.electrotools.project.executors;

import com.google.genai.types.FunctionCall;
import com.google.genai.types.FunctionResponse;
import lombok.RequiredArgsConstructor;
import pl.dabrowski.electrotools.ai.service.ToolExecutor;
import pl.dabrowski.electrotools.project.service.read.ReadProjectService;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

@RequiredArgsConstructor
public class FindAllToolExecutor implements ToolExecutor {
    private final ReadProjectService readProjectService;
    private final ObjectMapper objectMapper;

    @Override
    public FunctionResponse execute(FunctionCall call) {
        var projects = readProjectService.findAll();

        return FunctionResponse.builder()
                .name(call.name().get())
                .response(Map.of(
                        "result",
                        objectMapper.writeValueAsString(projects)
                ))
                .build();
    }
}
