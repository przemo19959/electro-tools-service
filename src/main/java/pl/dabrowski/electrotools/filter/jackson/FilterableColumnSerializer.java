package pl.dabrowski.electrotools.filter.jackson;

import pl.dabrowski.electrotools.filter.column.FilterableColumn;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;

public class FilterableColumnSerializer extends ValueSerializer<FilterableColumn> {
    @Override
    public void serialize(FilterableColumn value, JsonGenerator gen, SerializationContext ctxt) throws JacksonException {
        gen.writeString(value.name());
    }
}
