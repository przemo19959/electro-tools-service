package pl.dabrowski.electrotools.filter.column;

import org.jooq.Field;
import pl.dabrowski.electrotools.filter.FilterGroupDto;
import pl.dabrowski.electrotools.filter.operator.FilterColumnOperator;

public interface FilterableColumn {
    String name();

    FilterableColumnType getType();

    Field<?> getField();

    default FilterGroupDto.FilterColumnDto create(FilterColumnOperator operator, String value) {
        return new FilterGroupDto.FilterColumnDto(this, operator, value);
    }
}
