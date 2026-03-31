package pl.dabrowski.electrotools.filter.operator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.dabrowski.electrotools.filter.column.FilterableColumnType;

@RequiredArgsConstructor
@Getter
public enum FilterColumnOperator {
    STRING_EQ(FilterableColumnType.STRING),
    STRING_NOT_EQ(FilterableColumnType.STRING),
    STRING_IN(FilterableColumnType.STRING),
    STRING_NOT_IN(FilterableColumnType.STRING),

    NUMBER_EQ(FilterableColumnType.NUMBER),
    NUMBER_NOT_EQ(FilterableColumnType.NUMBER),
    NUMBER_GT(FilterableColumnType.NUMBER),
    NUMBER_GTE(FilterableColumnType.NUMBER),
    NUMBER_LT(FilterableColumnType.NUMBER),
    NUMBER_LTE(FilterableColumnType.NUMBER),
    NUMBER_IN(FilterableColumnType.NUMBER),
    NUMBER_NOT_IN(FilterableColumnType.NUMBER),

    DATE_EQ(FilterableColumnType.DATE),
    DATE_NOT_EQ(FilterableColumnType.DATE),
    DATE_BEFORE(FilterableColumnType.DATE),
    DATE_AFTER(FilterableColumnType.DATE),
    DATE_IN(FilterableColumnType.DATE),
    DATE_NOT_IN(FilterableColumnType.DATE);

    private final FilterableColumnType type;
}
