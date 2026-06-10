package pl.dabrowski.electrotools.project.executors;

import com.google.genai.types.FunctionCall;
import com.google.genai.types.FunctionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import pl.dabrowski.electrotools.ai.service.ToolExecutor;
import pl.dabrowski.electrotools.filter.FilterGroupDto;
import pl.dabrowski.electrotools.filter.operator.FilterColumnOperator;
import pl.dabrowski.electrotools.project.service.read.ReadProjectService;
import pl.dabrowski.electrotools.utils.GenAiUtils;
import tools.jackson.databind.ObjectMapper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class PageAllToolExecutor implements ToolExecutor {
    private final ReadProjectService readProjectService;
    private final ObjectMapper objectMapper;


    @Override
    public FunctionResponse execute(FunctionCall call) {
        var args = call.args().orElseGet(Map::of);
        var page = (Integer) args.getOrDefault("page", 0);
        var size = (Integer) args.getOrDefault("size", 10);
        var query = (String) args.get("query");
        var filters = ((List<LinkedHashMap<String, Object>>) args.get("filters")).stream()
                .map(filter -> new FilterGroupDto.FilterColumnDto(
                        (String) filter.get("column"),
                        FilterColumnOperator.valueOf((String) filter.get("operator")),
                        filter.getOrDefault("value", "") + ""))
                .toList();

        var result = readProjectService.pageAll(
                PageRequest.of(page, size),
                query == null ? Optional.empty() : Optional.of(query),
                filters.isEmpty() ? FilterGroupDto.empty() : FilterGroupDto.and(filters, List.of())
        );

        return GenAiUtils.createSuccessResponse(call, objectMapper.writeValueAsString(result));
    }
}
