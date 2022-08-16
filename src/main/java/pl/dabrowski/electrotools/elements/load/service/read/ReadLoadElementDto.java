package pl.dabrowski.electrotools.elements.load.service.read;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReadLoadElementDto {
  private final UUID id;
  private final double x;
  private final double y;
  private final String label;
  private final double drawPower;
}
