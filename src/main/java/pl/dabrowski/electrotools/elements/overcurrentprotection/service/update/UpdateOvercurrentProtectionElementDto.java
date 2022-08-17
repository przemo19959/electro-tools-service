package pl.dabrowski.electrotools.elements.overcurrentprotection.service.update;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.dabrowski.electrotools.elements.basic.service.update.UpdateBasicElementDto;
import pl.dabrowski.electrotools.elements.overcurrentprotection.OvercurrentProtectionType;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class UpdateOvercurrentProtectionElementDto extends UpdateBasicElementDto {
  private OvercurrentProtectionType type;
  private int amperage;
}
