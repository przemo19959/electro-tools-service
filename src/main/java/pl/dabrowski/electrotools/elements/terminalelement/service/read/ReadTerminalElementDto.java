package pl.dabrowski.electrotools.elements.terminalelement.service.read;

import lombok.Getter;
import lombok.Setter;
import pl.dabrowski.electrotools.elements.abstractelement.ReadAbstractElementDto;
import pl.dabrowski.electrotools.elements.terminalelement.TerminalType;

@Getter
@Setter
public class ReadTerminalElementDto extends ReadAbstractElementDto {
    private TerminalType type;
}
