package pl.dabrowski.electrotools.elements.basic.service.update;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class UpdateBasicElementPositionDto {
  private UUID elementId;
  private double x;
  private double y;
}
