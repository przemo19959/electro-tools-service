package pl.dabrowski.electrotools.project.executors;

import com.google.genai.types.FunctionCall;
import com.google.genai.types.FunctionResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import pl.dabrowski.electrotools.ai.service.ToolExecutor;
import pl.dabrowski.electrotools.project.service.create.CreateProjectDto;
import pl.dabrowski.electrotools.project.service.create.CreateProjectService;
import pl.dabrowski.electrotools.utils.GenAiUtils;

import java.util.Map;

@RequiredArgsConstructor
public class CreateToolExecutor implements ToolExecutor {
    private final CreateProjectService createProjectService;

    @Override
    public FunctionResponse execute(FunctionCall call) {
        var args = call.args().orElseGet(Map::of);
        var name = (String) args.get("name");

        if (StringUtils.isEmpty(name)) {
            return GenAiUtils.createErrorResponse(call, "Name is required to create project");
        }

        try {
            createProjectService.create(new CreateProjectDto(name));
        } catch (Exception e) {
            return GenAiUtils.createErrorResponse(call, e.getMessage());
        }

        return GenAiUtils.createSuccessResponse(call, "Project created successfully");
    }
}
