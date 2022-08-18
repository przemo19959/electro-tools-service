package pl.dabrowski.electrotools.connection.service.read;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import pl.dabrowski.electrotools.wire.service.read.ReadWireDto;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReadConnectionDto {
  private final UUID id;
  private final UUID fromElementId;
  private final UUID toElementId;
  private final ReadWireDto wire;
}
