package pl.dabrowski.electrotools.filter.jackson;

import pl.dabrowski.electrotools.filter.column.FilterableColumn;
import pl.dabrowski.electrotools.filter.column.ProjectFilterableColumn;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ValueDeserializer;
import tools.jackson.databind.exc.InvalidFormatException;

public class FilterableColumnDeserializer extends ValueDeserializer<FilterableColumn> {
    @Override
    public FilterableColumn deserialize(JsonParser p, DeserializationContext ctxt) {
        var value = p.getValueAsString();

        try {
            return ProjectFilterableColumn.valueOf(value);
        } catch (IllegalArgumentException _) {
            throw InvalidFormatException.from(
                    p,
                    "Unsupported filterable column: " + value,
                    value,
                    FilterableColumn.class
            );
        }
    }
}
