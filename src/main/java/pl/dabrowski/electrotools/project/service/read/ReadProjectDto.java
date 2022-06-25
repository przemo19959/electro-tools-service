package pl.dabrowski.electrotools.project.service.read;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReadProjectDto {
  private final UUID id;
  private final String name;
  private final String owner;
}
