package pl.dabrowski.electrotools.project.service.read;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class ReadProjectDto {
  private final UUID id;
  private final String name;
  private final String owner;
  private final long elementCount;
}
