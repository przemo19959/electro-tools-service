package pl.dabrowski.electrotools.elements.load.service.create;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.dabrowski.electrotools.elements.abstractelement.CreateAbstractElementDto;
import pl.dabrowski.electrotools.elements.load.Config;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class CreateLoadElementDto extends CreateAbstractElementDto {
  private double drawPower;
  private double powerFactor;
  private boolean highStartCurrent;
  private Config config;
  private boolean zeroed;
}
