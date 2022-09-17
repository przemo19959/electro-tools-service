package pl.dabrowski.electrotools.elements.load.service.read;

import lombok.Getter;
import lombok.Setter;
import pl.dabrowski.electrotools.elements.abstractelement.ReadAbstractElementDto;
import pl.dabrowski.electrotools.elements.load.Config;

@Getter
@Setter
public class ReadLoadElementDto extends ReadAbstractElementDto {
  private double drawPower;
  private double powerFactor;
  private Boolean highStartCurrent;
  private Config config;
  private Boolean zeroed;
}
