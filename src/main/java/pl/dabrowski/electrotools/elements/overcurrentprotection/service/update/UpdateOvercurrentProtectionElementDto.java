package pl.dabrowski.electrotools.elements.overcurrentprotection.service.update;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.dabrowski.electrotools.elements.abstractelement.UpdateAbstractElementDto;
import pl.dabrowski.electrotools.elements.overcurrentprotection.OvercurrentProtectionType;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class UpdateOvercurrentProtectionElementDto extends UpdateAbstractElementDto {
  private OvercurrentProtectionType type;
  private int amperage;
}
