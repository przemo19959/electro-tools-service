package pl.dabrowski.electrotools.project.executors;

import com.google.genai.types.FunctionCall;
import com.google.genai.types.FunctionResponse;
import lombok.RequiredArgsConstructor;
import pl.dabrowski.electrotools.ai.service.ToolExecutor;
import pl.dabrowski.electrotools.project.service.delete.DeleteProjectService;
import pl.dabrowski.electrotools.utils.GenAiUtils;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class DeleteAllToolExecutor implements ToolExecutor {
    private final DeleteProjectService deleteProjectService;

    @Override
    public FunctionResponse execute(FunctionCall call) {
        var args = call.args().orElseGet(Map::of);
        var ids = (List<String>) args.get("ids");

        try {
            deleteProjectService.deleteAllById(ids.stream().map(UUID::fromString).toList());
        } catch (Exception e) {
            return GenAiUtils.createErrorResponse(call, e.getMessage());
        }

        return GenAiUtils.createSuccessResponse(call, "Projects deleted successfully");
    }
}
