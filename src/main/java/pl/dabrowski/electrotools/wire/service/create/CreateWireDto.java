package pl.dabrowski.electrotools.wire.service.create;

import lombok.*;
import pl.dabrowski.electrotools.wire.PhaseType;
import pl.dabrowski.electrotools.wire.PlacementType;
import pl.dabrowski.electrotools.wire.WireDiameter;
import pl.dabrowski.electrotools.wire.WireType;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class CreateWireDto {
  private WireDiameter diameter;
  private PlacementType placement;
  private WireType type;
  private PhaseType phase;
  private double length;
}
