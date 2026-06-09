package pl.dabrowski.electrotools.project.executors;

import com.google.genai.types.FunctionCall;
import com.google.genai.types.FunctionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import pl.dabrowski.electrotools.ai.service.ToolExecutor;
import pl.dabrowski.electrotools.filter.FilterGroupDto;
import pl.dabrowski.electrotools.project.service.read.ReadProjectService;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class PageAllToolExecutor implements ToolExecutor {
    private final ReadProjectService readProjectService;
    private final ObjectMapper objectMapper;


    @Override
    public FunctionResponse execute(FunctionCall call) {
        var args = call.args().get();
        var page = (Integer) args.getOrDefault("page", 0);
        var size = (Integer) args.getOrDefault("size", 10);
        var query = (String) args.get("query");
        var filters = (List<FilterGroupDto.FilterColumnDto>) args.get("filters");
        //todo verify filters works with prompt: i need all projects for which name contains "te" chars

        var result = readProjectService.pageAll(
                PageRequest.of(page, size),
                query == null ? Optional.empty() : Optional.of(query),
                filters == null ? FilterGroupDto.empty() : FilterGroupDto.and(filters, List.of())
        );

        return FunctionResponse.builder()
                .name(call.name().get())
                .response(Map.of(
                        "result",
                        objectMapper.writeValueAsString(result)
                ))
                .build();
    }
}
