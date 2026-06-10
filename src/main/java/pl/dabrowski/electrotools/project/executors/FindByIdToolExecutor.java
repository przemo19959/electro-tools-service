package pl.dabrowski.electrotools.project.executors;

import com.google.genai.types.FunctionCall;
import com.google.genai.types.FunctionResponse;
import lombok.RequiredArgsConstructor;
import pl.dabrowski.electrotools.ai.service.ToolExecutor;
import pl.dabrowski.electrotools.project.service.read.ReadProjectService;
import pl.dabrowski.electrotools.utils.GenAiUtils;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class FindByIdToolExecutor implements ToolExecutor {
    private final ReadProjectService readProjectService;
    private final ObjectMapper objectMapper;

    @Override
    public FunctionResponse execute(FunctionCall call) {
        var args = call.args().orElseGet(Map::of);
        var id = (String) args.get("id");

        var project = readProjectService.findById(UUID.fromString(id));

        return GenAiUtils.createSuccessResponse(call, objectMapper.writeValueAsString(project));
    }
}
