package pl.dabrowski.electrotools.wire.service.read;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import pl.dabrowski.electrotools.wire.*;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReadWireDto {
  private final UUID id;
  private final WireDiameter diameter;
  private final WireSymbol symbol;
  private final PlacementType placement;
  private final WireType type;
  private final PhaseType phase;
  private final Double length;
}
