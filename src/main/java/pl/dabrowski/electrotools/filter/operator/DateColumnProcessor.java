package pl.dabrowski.electrotools.filter.operator;

import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import pl.dabrowski.electrotools.filter.FilterGroupDto;

import java.time.LocalDateTime;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class DateColumnProcessor {
    private final FilterGroupDto.FilterColumnDto column;

    public Condition process() {
        var field = column.column().getField().cast(LocalDateTime.class);
        return switch (column.operator()) {
            case DATE_EQ -> field.eq(LocalDateTime.parse(column.value()));
            case DATE_NOT_EQ -> field.ne(LocalDateTime.parse(column.value()));
            case DATE_BEFORE -> field.lt(LocalDateTime.parse(column.value()));
            case DATE_AFTER -> field.gt(LocalDateTime.parse(column.value()));
            case DATE_IN -> field.in(
                    Stream.of(column.value().split(","))
                            .map(LocalDateTime::parse)
                            .toList());
            case DATE_NOT_IN -> field.notIn(
                    Stream.of(column.value().split(","))
                            .map(LocalDateTime::parse)
                            .toList());
            default -> throw new IllegalArgumentException("Unsupported operator: " + column.operator());
        };
    }
}
