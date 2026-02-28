package pl.dabrowski.electrotools.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Electro Tools API",
        version = "v1",
        description = "Swagger documentation for all REST controllers"
    )
)
public class OpenApiConfig {
}
