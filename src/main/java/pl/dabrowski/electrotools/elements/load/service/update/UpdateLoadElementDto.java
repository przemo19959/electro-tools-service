package pl.dabrowski.electrotools.elements.load.service.update;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class UpdateLoadElementDto {

  private double x;

  private double y;

  private String label;

  private double drawPower;
}
