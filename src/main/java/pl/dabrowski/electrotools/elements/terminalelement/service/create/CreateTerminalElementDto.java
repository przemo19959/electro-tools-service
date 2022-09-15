package pl.dabrowski.electrotools.elements.terminalelement.service.create;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.dabrowski.electrotools.elements.abstractelement.CreateAbstractElementDto;
import pl.dabrowski.electrotools.elements.terminalelement.TerminalType;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class CreateTerminalElementDto extends CreateAbstractElementDto {
  private TerminalType type;
}
