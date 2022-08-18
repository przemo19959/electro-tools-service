package pl.dabrowski.electrotools.connection.service.create;

import lombok.*;
import pl.dabrowski.electrotools.wire.service.create.CreateWireDto;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class CreateConnectionDto {
  private UUID fromElementId;
  private UUID toElementId;
  private CreateWireDto wire;
}
