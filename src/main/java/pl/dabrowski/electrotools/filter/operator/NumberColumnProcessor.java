package pl.dabrowski.electrotools.filter.operator;

import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import pl.dabrowski.electrotools.filter.column.FilterableColumn;

import java.util.stream.Stream;

@RequiredArgsConstructor
public class NumberColumnProcessor {
    private final FilterableColumn column;
    private final FilterColumnOperator operator;
    private final String value;

    public Condition process() {
        var field = column.getField().cast(Number.class);
        return switch (operator) {
            case NUMBER_EQ -> field.eq(Double.valueOf(value));
            case NUMBER_NOT_EQ -> field.ne(Double.valueOf(value));
            case NUMBER_GT -> field.gt(Double.valueOf(value));
            case NUMBER_GTE -> field.ge(Double.valueOf(value));
            case NUMBER_LT -> field.lt(Double.valueOf(value));
            case NUMBER_LTE -> field.le(Double.valueOf(value));
            case NUMBER_IN -> field.in(
                    Stream.of(value.split(","))
                            .map(Double::valueOf)
                            .toList()
            );
            case NUMBER_NOT_IN -> field.notIn(
                    Stream.of(value.split(","))
                            .map(Double::valueOf)
                            .toList());
            case NUMBER_ILIKE -> field.likeIgnoreCase('%' + value + '%');
            default -> throw new IllegalArgumentException("Unsupported operator: " + operator);
        };
    }
}
