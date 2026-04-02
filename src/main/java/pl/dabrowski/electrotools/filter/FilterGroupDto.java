package pl.dabrowski.electrotools.filter;

import org.jooq.Condition;
import org.jooq.impl.DSL;
import pl.dabrowski.electrotools.filter.column.ProjectFilterableColumn;
import pl.dabrowski.electrotools.filter.operator.DateColumnProcessor;
import pl.dabrowski.electrotools.filter.operator.FilterColumnOperator;
import pl.dabrowski.electrotools.filter.operator.NumberColumnProcessor;
import pl.dabrowski.electrotools.filter.operator.StringColumnProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public record FilterGroupDto(
        FilterGroupOperator operator,
        List<FilterColumnDto> columns,
        List<FilterGroupDto> groups
) {
    public static FilterGroupDto empty() {
        return new FilterGroupDto(FilterGroupOperator.AND, new ArrayList<>(), new ArrayList<>());
    }

    public static FilterGroupDto and(FilterColumnDto... columns) {
        return and(List.of(columns), new ArrayList<>());
    }

    public static FilterGroupDto or(FilterColumnDto... columns) {
        return or(List.of(columns), new ArrayList<>());
    }

    public static FilterGroupDto and(FilterGroupDto... groups) {
        return and(new ArrayList<>(), List.of(groups));
    }

    public static FilterGroupDto or(FilterGroupDto... groups) {
        return or(new ArrayList<>(), List.of(groups));
    }

    public static FilterGroupDto and(List<FilterColumnDto> columns, List<FilterGroupDto> groups) {
        return new FilterGroupDto(FilterGroupOperator.AND, columns, groups);
    }

    public static FilterGroupDto or(List<FilterColumnDto> columns, List<FilterGroupDto> groups) {
        return new FilterGroupDto(FilterGroupOperator.OR, columns, groups);
    }

    public record FilterColumnDto(
            String column,
            FilterColumnOperator operator,
            String value
    ) {
        public Condition process() {
            // Must be like this, previously FilterableColumn was used directly in the DTO, but it caused StackOverflow in Swagger UI,
            // because of the circular reference between FilterableColumn and FilterGroupDto
            var filterableColumn = ProjectFilterableColumn.valueOf(column);
            return switch (operator.getType()) {
                case STRING -> new StringColumnProcessor(filterableColumn, operator, value).process();
                case NUMBER -> new NumberColumnProcessor(filterableColumn, operator, value).process();
                case DATE -> new DateColumnProcessor(filterableColumn, operator, value).process();
            };
        }
    }

    public Condition process() {
        var conditions = columns.stream().map(FilterColumnDto::process).collect(Collectors.toList());
        conditions.addAll(groups.stream().map(FilterGroupDto::process).collect(Collectors.toList()));
        return switch (operator) {
            case OR -> DSL.or(conditions);
            case AND -> DSL.and(conditions);
        };
    }
}
