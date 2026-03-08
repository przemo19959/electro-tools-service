package pl.dabrowski.electrotools.elements.basic;

import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pl.dabrowski.electrotools.elements.abstractelement.*;
import pl.dabrowski.electrotools.project.Project;
import pl.dabrowski.electrotools.wire.Wire;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@Table(name = "t_basic_elements")
@EntityListeners(value = AuditingEntityListener.class)
public class BasicElement extends AbstractElement {
  public static BasicElement create(CreateAbstractElementDto dto,
                                    Project project) {
    BasicElement basicElement = new BasicElement();
    AbstractElement.create(basicElement, dto, project);

    return basicElement;
  }

  @Override
  public BasicElement update(UpdateAbstractElementDto dto) {
    super.update(dto);

    return this;
  }

  public BasicElement updatePosition(double x, double y) {
    this.x = x;
    this.y = y;

    return this;
  }

  @Override
  public ReadAbstractElementDto toDto(List<ReadAbstractElementDto> children) {
    ReadAbstractElementDto dto = new ReadAbstractElementDto();
    dto.setId(id);
    dto.setX(x);
    dto.setY(y);
    dto.setLabel(label);
    dto.setParentId(parentId);
    dto.setWire(Optional.ofNullable(wire).map(Wire::toDto).orElse(null));
    dto.setChildren(children);

    dto.setElementType(BasicElementType.UNKNOWN);

    return dto;
  }
}
