package pl.dabrowski.electrotools.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.TableField;
import org.jooq.impl.DSL;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JooqUtils {
    public static <R extends Record, T> Field<T> resolve(String tableAlias, TableField<R, T> field) {
        return DSL.field(DSL.name(tableAlias, field.getName()), field.getDataType());
    }
}
