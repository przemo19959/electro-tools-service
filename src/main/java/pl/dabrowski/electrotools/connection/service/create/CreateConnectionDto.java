package pl.dabrowski.electrotools.connection.service.create;

import lombok.*;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class CreateConnectionDto {
  private UUID elementId;
}
