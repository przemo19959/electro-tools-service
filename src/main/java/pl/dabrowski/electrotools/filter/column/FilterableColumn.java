package pl.dabrowski.electrotools.filter.column;

import org.jooq.Field;

public interface FilterableColumn {
    FilterableColumnType getType();

    Field<?> getField();
}
