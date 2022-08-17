package pl.dabrowski.electrotools.elements.basic.service.update;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class UpdateBasicElementDto {
  protected double x;
  protected double y;
  protected String label;
}
