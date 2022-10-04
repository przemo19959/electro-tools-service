package pl.dabrowski.electrotools.elements.rcdelement.service.create;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.dabrowski.electrotools.elements.abstractelement.CreateAbstractElementDto;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class CreateRcdElementDto extends CreateAbstractElementDto {
  private int nominalCurrent;
  private int diffCurrent;
  private int poleNumber;
}
