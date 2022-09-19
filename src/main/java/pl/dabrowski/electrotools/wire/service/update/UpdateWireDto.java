package pl.dabrowski.electrotools.wire.service.update;

import lombok.*;
import pl.dabrowski.electrotools.wire.PhaseType;
import pl.dabrowski.electrotools.wire.PlacementType;
import pl.dabrowski.electrotools.wire.WireDiameter;
import pl.dabrowski.electrotools.wire.WireType;

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
