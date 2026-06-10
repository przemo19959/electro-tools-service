package pl.dabrowski.electrotools.project.executors;

import com.google.genai.types.FunctionCall;
import com.google.genai.types.FunctionResponse;
import lombok.RequiredArgsConstructor;
import pl.dabrowski.electrotools.ai.service.ToolExecutor;
import pl.dabrowski.electrotools.project.service.read.ReadProjectService;
import pl.dabrowski.electrotools.utils.GenAiUtils;
import tools.jackson.databind.ObjectMapper;

@RequiredArgsConstructor
public class FindAllToolExecutor implements ToolExecutor {
    private final ReadProjectService readProjectService;
    private final ObjectMapper objectMapper;

    @Override
    public FunctionResponse execute(FunctionCall call) {
        var projects = readProjectService.findAll();

        return GenAiUtils.createSuccessResponse(call, objectMapper.writeValueAsString(projects));
    }
}
