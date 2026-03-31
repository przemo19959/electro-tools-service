package pl.dabrowski.electrotools.filter.operator;

import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import pl.dabrowski.electrotools.filter.FilterGroupDto;

@RequiredArgsConstructor
public class StringColumnProcessor {
    private final FilterGroupDto.FilterColumnDto column;

    public Condition process() {
        var field = column.column().getField().cast(String.class);
        return switch (column.operator()) {
            case STRING_EQ -> field.eq(column.value());
            case STRING_NOT_EQ -> field.ne(column.value());
            case STRING_IN -> field.in(column.value().split(","));
            case STRING_NOT_IN -> field.notIn(column.value().split(","));
            default -> throw new IllegalArgumentException("Unsupported operator: " + column.operator());
        };
    }
}
