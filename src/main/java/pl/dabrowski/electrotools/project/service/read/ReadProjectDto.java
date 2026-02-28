package pl.dabrowski.electrotools.project.service.read;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record ReadProjectDto(
  UUID id,
  String name,
  String createdBy,
  String modifiedBy,
  Instant modifiedDate,
  long elementCount
) {}
