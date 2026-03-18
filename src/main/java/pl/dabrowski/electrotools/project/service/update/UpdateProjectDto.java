package pl.dabrowski.electrotools.project.service.update;

import jakarta.validation.constraints.NotBlank;

public record UpdateProjectDto(
        @NotBlank String name
) {
}
