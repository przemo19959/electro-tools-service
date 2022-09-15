package pl.dabrowski.electrotools.elements.terminalelement.service.update;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.dabrowski.electrotools.elements.abstractelement.UpdateAbstractElementDto;
import pl.dabrowski.electrotools.elements.terminalelement.TerminalType;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class UpdateTerminalElementDto extends UpdateAbstractElementDto {
  private TerminalType type;
}
