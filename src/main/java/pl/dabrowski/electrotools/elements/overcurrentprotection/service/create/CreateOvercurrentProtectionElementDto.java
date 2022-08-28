package pl.dabrowski.electrotools.elements.overcurrentprotection.service.create;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.dabrowski.electrotools.elements.abstractelement.CreateAbstractElementDto;
import pl.dabrowski.electrotools.elements.overcurrentprotection.OvercurrentProtectionType;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class CreateOvercurrentProtectionElementDto extends CreateAbstractElementDto {
  private OvercurrentProtectionType type;
  private int amperage;
}
