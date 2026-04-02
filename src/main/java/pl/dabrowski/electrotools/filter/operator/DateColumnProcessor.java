package pl.dabrowski.electrotools.filter.operator;

import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import pl.dabrowski.electrotools.filter.column.FilterableColumn;

import java.time.LocalDateTime;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class DateColumnProcessor {
    private final FilterableColumn column;
    private final FilterColumnOperator operator;
    private final String value;

    public Condition process() {
        var field = column.getField().cast(LocalDateTime.class);
        return switch (operator) {
            case DATE_EQ -> field.eq(LocalDateTime.parse(value));
            case DATE_NOT_EQ -> field.ne(LocalDateTime.parse(value));
            case DATE_BEFORE -> field.lt(LocalDateTime.parse(value));
            case DATE_AFTER -> field.gt(LocalDateTime.parse(value));
            case DATE_IN -> field.in(
                    Stream.of(value.split(","))
                            .map(LocalDateTime::parse)
                            .toList());
            case DATE_NOT_IN -> field.notIn(
                    Stream.of(value.split(","))
                            .map(LocalDateTime::parse)
                            .toList());
            default -> throw new IllegalArgumentException("Unsupported operator: " + operator);
        };
    }
}
