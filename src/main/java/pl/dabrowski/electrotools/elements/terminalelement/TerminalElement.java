package pl.dabrowski.electrotools.elements.terminalelement;

import lombok.Getter;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pl.dabrowski.electrotools.elements.abstractelement.AbstractElement;
import pl.dabrowski.electrotools.elements.abstractelement.BasicElementType;
import pl.dabrowski.electrotools.elements.abstractelement.ReadAbstractElementDto;
import pl.dabrowski.electrotools.elements.terminalelement.service.create.CreateTerminalElementDto;
import pl.dabrowski.electrotools.elements.terminalelement.service.read.ReadTerminalElementDto;
import pl.dabrowski.electrotools.elements.terminalelement.service.update.UpdateTerminalElementDto;
import pl.dabrowski.electrotools.project.Project;
import pl.dabrowski.electrotools.wire.Wire;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@Table(name = "t_terminal_elements")
@EntityListeners(value = AuditingEntityListener.class)
@Audited
public class TerminalElement extends AbstractElement {
  @Column(name = "type")
  @GeneratedValue
  public TerminalType type;

  public static TerminalElement create(CreateTerminalElementDto dto, Project project) {
    final TerminalElement terminalElement = new TerminalElement();
    AbstractElement.create(terminalElement, dto, project);

    terminalElement.type = dto.getType();

    return terminalElement;
  }

  public TerminalElement update(UpdateTerminalElementDto dto) {
    super.update(dto);
    this.type = dto.getType();

    return this;
  }

  @Override
  public ReadAbstractElementDto toDto(List<ReadAbstractElementDto> children) {
    ReadTerminalElementDto dto = new ReadTerminalElementDto();
    dto.setId(id);
    dto.setX(x);
    dto.setY(y);
    dto.setLabel(label);
    dto.setParentId(parentId);
    dto.setWire(Optional.ofNullable(wire).map(Wire::toDto).orElse(null));
    dto.setChildren(children);

    dto.setElementType(BasicElementType.TERMINAL);
    dto.setType(type);

    return dto;
  }
}
