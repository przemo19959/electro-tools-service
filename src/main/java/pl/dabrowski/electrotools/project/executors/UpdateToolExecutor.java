package pl.dabrowski.electrotools.project.executors;

import com.google.genai.types.FunctionCall;
import com.google.genai.types.FunctionResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.server.ResponseStatusException;
import pl.dabrowski.electrotools.ai.service.ToolExecutor;
import pl.dabrowski.electrotools.project.service.update.UpdateProjectDto;
import pl.dabrowski.electrotools.project.service.update.UpdateProjectService;
import pl.dabrowski.electrotools.utils.GenAiUtils;

import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class UpdateToolExecutor implements ToolExecutor {
    private final UpdateProjectService updateProjectService;

    @Override
    public FunctionResponse execute(FunctionCall call) {
        var args = call.args().orElseGet(Map::of);
        var id = (String) args.get("id");
        var name = (String) args.get("name");

        if (StringUtils.isEmpty(name)) {
            return GenAiUtils.createErrorResponse(call, "Name is required to create project");
        }

        try {
            updateProjectService.update(UUID.fromString(id), new UpdateProjectDto(name));
        } catch (ResponseStatusException e) {
            return GenAiUtils.createErrorResponse(call, e.getMessage());
        }


        return GenAiUtils.createSuccessResponse(call, "Project updated successfully");
    }
}
