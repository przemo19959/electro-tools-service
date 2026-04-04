package pl.dabrowski.electrotools.filter.operator;

import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import pl.dabrowski.electrotools.filter.column.FilterableColumn;

@RequiredArgsConstructor
public class StringColumnProcessor {
    private final FilterableColumn column;
    private final FilterColumnOperator operator;
    private final String value;

    public Condition process() {
        var field = column.getField().cast(String.class);
        return switch (operator) {
            case STRING_EQ -> field.eq(value);
            case STRING_NOT_EQ -> field.ne(value);
            case STRING_IN -> field.in(value.split(","));
            case STRING_NOT_IN -> field.notIn(value.split(","));
            case STRING_ILIKE -> field.likeIgnoreCase('%' + value + '%');
            default -> throw new IllegalArgumentException("Unsupported operator: " + operator);
        };
    }
}
