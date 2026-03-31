package pl.dabrowski.electrotools.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.TableField;
import org.jooq.impl.DSL;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JooqUtils {
    public static <R extends Record, T> Field<T> resolve(String tableAlias, TableField<R, T> field) {
        return DSL.field(DSL.name(tableAlias, field.getName()), field.getDataType());
    }

    public static <R extends Record> Field<String> format(TableField<R, LocalDateTime> field) {
        return DSL.field("to_char({0}, 'YYYY-MM-DD')", String.class, field);
    }

    @SafeVarargs
    public static Condition query(String query, Field<String>... fields) {
        var likeQuery = "%" + query + "%";
        Condition condition = DSL.noCondition();
        for (var field : fields) {
            condition = condition.or(field.likeIgnoreCase(likeQuery));
        }
        return condition;
    }
}
