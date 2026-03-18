package pl.dabrowski.electrotools.project.service.create;

import jakarta.validation.constraints.NotBlank;

public record CreateProjectDto(
        @NotBlank String name
) {
}
