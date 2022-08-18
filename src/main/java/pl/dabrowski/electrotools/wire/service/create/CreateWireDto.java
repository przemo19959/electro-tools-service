package pl.dabrowski.electrotools.wire.service.create;

import lombok.*;
import pl.dabrowski.electrotools.wire.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class CreateWireDto {
  private WireDiameter diameter;
  private WireSymbol symbol;
  private PlacementType placement;
  private WireType type;
  private PhaseType phase;
}
