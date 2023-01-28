package pl.dabrowski.electrotools.wire.service.update;

import lombok.*;
import pl.dabrowski.electrotools.wire.PlacementType;
import pl.dabrowski.electrotools.wire.WireDiameter;
import pl.dabrowski.electrotools.wire.WireType;
import pl.dabrowski.electrotools.wire.phase.PhaseType;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class UpdateWireDto {
  private UUID id;
  private WireDiameter diameter;
  private PlacementType placement;
  private WireType type;
  private PhaseType phase;
  private double length;
}
