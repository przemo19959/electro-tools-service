package pl.dabrowski.electrotools.filter.operator;

import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import pl.dabrowski.electrotools.filter.FilterGroupDto;

import java.util.stream.Stream;

@RequiredArgsConstructor
public class NumberColumnProcessor {
    private final FilterGroupDto.FilterColumnDto column;

    public Condition process() {
        var field = column.column().getField().cast(Number.class);
        return switch (column.operator()) {
            case NUMBER_EQ -> field.eq(Double.valueOf(column.value()));
            case NUMBER_NOT_EQ -> field.ne(Double.valueOf(column.value()));
            case NUMBER_GT -> field.gt(Double.valueOf(column.value()));
            case NUMBER_GTE -> field.ge(Double.valueOf(column.value()));
            case NUMBER_LT -> field.lt(Double.valueOf(column.value()));
            case NUMBER_LTE -> field.le(Double.valueOf(column.value()));
            case NUMBER_IN -> field.in(
                    Stream.of(column.value().split(","))
                            .map(Double::valueOf)
                            .toList()
            );
            case NUMBER_NOT_IN -> field.notIn(
                    Stream.of(column.value().split(","))
                            .map(Double::valueOf)
                            .toList());
            default -> throw new IllegalArgumentException("Unsupported operator: " + column.operator());
        };
    }
}
