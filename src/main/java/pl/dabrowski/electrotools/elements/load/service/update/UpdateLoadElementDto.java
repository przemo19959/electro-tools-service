package pl.dabrowski.electrotools.elements.load.service.update;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.dabrowski.electrotools.elements.abstractelement.UpdateAbstractElementDto;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class UpdateLoadElementDto extends UpdateAbstractElementDto {
  private double drawPower;
  private double powerFactor;
  private boolean highStartCurrent;
}
