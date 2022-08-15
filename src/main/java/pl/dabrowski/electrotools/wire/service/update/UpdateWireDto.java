package pl.dabrowski.electrotools.wire.service.update;

import lombok.*;
import pl.dabrowski.electrotools.wire.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class UpdateWireDto {

  private WireDiameter diameter;

  private WireSymbol symbol;

  private PlacementType placement;

  private WireType type;

  private PhaseType phase;
}
