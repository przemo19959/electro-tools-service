package pl.dabrowski.electrotools.filter.column;

import lombok.NoArgsConstructor;

import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class FilterableColumnFactory {
    public static FilterableColumn create(String name) {
        return Optional.ofNullable(name)
                .flatMap(ProjectFilterableColumn::fromName)
                .orElseThrow(() -> new IllegalArgumentException("Unsupported column name: " + name));
    }
}
