package pl.dabrowski.electrotools.elements.rcdelement.service.update;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.dabrowski.electrotools.elements.abstractelement.UpdateAbstractElementDto;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class UpdateRcdElementDto extends UpdateAbstractElementDto {
  private int nominalCurrent;
  private int diffCurrent;
  private int poleNumber;
}
