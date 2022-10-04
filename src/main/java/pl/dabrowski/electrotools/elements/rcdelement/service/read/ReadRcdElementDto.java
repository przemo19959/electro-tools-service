package pl.dabrowski.electrotools.elements.rcdelement.service.read;

import lombok.Getter;
import lombok.Setter;
import pl.dabrowski.electrotools.elements.abstractelement.ReadAbstractElementDto;

@Getter
@Setter
public class ReadRcdElementDto extends ReadAbstractElementDto {
  private int nominalCurrent;
  private int diffCurrent;
  private int poleNumber;
}
