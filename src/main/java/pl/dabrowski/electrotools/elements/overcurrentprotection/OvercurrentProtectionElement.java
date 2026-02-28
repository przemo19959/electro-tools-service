package pl.dabrowski.electrotools.elements.overcurrentprotection;

import lombok.Getter;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pl.dabrowski.electrotools.elements.abstractelement.AbstractElement;
import pl.dabrowski.electrotools.elements.abstractelement.BasicElementType;
import pl.dabrowski.electrotools.elements.abstractelement.ReadAbstractElementDto;
import pl.dabrowski.electrotools.elements.overcurrentprotection.service.create.CreateOvercurrentProtectionElementDto;
import pl.dabrowski.electrotools.elements.overcurrentprotection.service.read.ReadOvercurrentProtectionElementDto;
import pl.dabrowski.electrotools.elements.overcurrentprotection.service.update.UpdateOvercurrentProtectionElementDto;
import pl.dabrowski.electrotools.project.Project;
import pl.dabrowski.electrotools.wire.Wire;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@Table(name = "t_overcurrent_protection_elements")
@EntityListeners(value = AuditingEntityListener.class)
@Audited
public class OvercurrentProtectionElement extends AbstractElement {
  @NotNull
  @Column(name = "type")
  @Enumerated(EnumType.STRING)
  private OvercurrentProtectionType type;

  @Column(name = "amperage")
  private int amperage;

  public static OvercurrentProtectionElement create(CreateOvercurrentProtectionElementDto dto, Project project) {
    final OvercurrentProtectionElement entity = new OvercurrentProtectionElement();
    AbstractElement.create(entity, dto, project);

    entity.type = dto.getType();
    entity.amperage = dto.getAmperage();

    return entity;
  }

  public OvercurrentProtectionElement update(UpdateOvercurrentProtectionElementDto dto) {
    super.update(dto);

    this.type = dto.getType();
    this.amperage = dto.getAmperage();

    return this;
  }

  @Override
  public ReadAbstractElementDto toDto(List<ReadAbstractElementDto> children) {
    ReadOvercurrentProtectionElementDto dto = new ReadOvercurrentProtectionElementDto();
    dto.setId(id);
    dto.setX(x);
    dto.setY(y);
    dto.setLabel(label);
    dto.setParentId(parentId);
    dto.setWire(Optional.ofNullable(wire).map(Wire::toDto).orElse(null));
    dto.setChildren(children);

    dto.setElementType(BasicElementType.OVER_CURRENT_PROTECTION);
    dto.setType(type);
    dto.setAmperage(amperage);

    return dto;
  }
}
