package pl.dabrowski.electrotools.connection.service.update;

import lombok.*;
import pl.dabrowski.electrotools.wire.service.update.UpdateWireDto;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class UpdateConnectionDto {
  private UUID id;
  private UUID fromElementId;
  private UUID toElementId;
  private UpdateWireDto wire;
}
