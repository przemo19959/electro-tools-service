package pl.dabrowski.electrotools.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.dabrowski.electrotools.filter.column.FilterableColumn;
import pl.dabrowski.electrotools.filter.jackson.FilterableColumnDeserializer;
import pl.dabrowski.electrotools.filter.jackson.FilterableColumnSerializer;
import tools.jackson.databind.JacksonModule;
import tools.jackson.databind.module.SimpleModule;

@Configuration
public class JacksonConfig {
    @Bean
    public JacksonModule filterableColumnModule() {
        var module = new SimpleModule();
        module.addSerializer(FilterableColumn.class, new FilterableColumnSerializer());
        module.addDeserializer(FilterableColumn.class, new FilterableColumnDeserializer());
        return module;
    }
}
