package pl.dabrowski.electrotools.elements.overcurrentprotection.service.read;

import lombok.Getter;
import lombok.Setter;
import pl.dabrowski.electrotools.elements.abstractelement.ReadAbstractElementDto;
import pl.dabrowski.electrotools.elements.overcurrentprotection.OvercurrentProtectionType;

@Getter
@Setter
public class ReadOvercurrentProtectionElementDto extends ReadAbstractElementDto {
  private OvercurrentProtectionType type;
  private int amperage;
}
