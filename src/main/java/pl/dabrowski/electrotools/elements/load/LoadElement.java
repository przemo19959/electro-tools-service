package pl.dabrowski.electrotools.elements.load;

import lombok.Getter;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pl.dabrowski.electrotools.elements.abstractelement.AbstractElement;
import pl.dabrowski.electrotools.elements.abstractelement.BasicElementType;
import pl.dabrowski.electrotools.elements.abstractelement.ReadAbstractElementDto;
import pl.dabrowski.electrotools.elements.load.service.create.CreateLoadElementDto;
import pl.dabrowski.electrotools.elements.load.service.read.ReadLoadElementDto;
import pl.dabrowski.electrotools.elements.load.service.update.UpdateLoadElementDto;
import pl.dabrowski.electrotools.project.Project;
import pl.dabrowski.electrotools.wire.Wire;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@Table(name = "t_load_elements")
@EntityListeners(value = AuditingEntityListener.class)
@Audited
public class LoadElement extends AbstractElement {
  @Column(name = "draw_power")
  private double drawPower;

  @Column(name = "power_factor")
  private double powerFactor = 1;

  @Column(name = "high_start_current")
  private Boolean highStartCurrent;

  public static LoadElement create(CreateLoadElementDto dto, Project project) {
    final LoadElement loadElement = new LoadElement();
    AbstractElement.create(loadElement, dto, project);

    loadElement.drawPower = dto.getDrawPower();
    loadElement.powerFactor = dto.getPowerFactor();
    loadElement.highStartCurrent = dto.isHighStartCurrent();

    return loadElement;
  }

  public LoadElement update(UpdateLoadElementDto dto) {
    super.update(dto);
    this.drawPower = dto.getDrawPower();
    this.powerFactor = dto.getPowerFactor();
    this.highStartCurrent = dto.isHighStartCurrent();

    return this;
  }

  @Override
  public ReadAbstractElementDto toDto(List<ReadAbstractElementDto> children) {
    ReadLoadElementDto dto = new ReadLoadElementDto();
    dto.setId(id);
    dto.setX(x);
    dto.setY(y);
    dto.setLabel(label);
    dto.setParentId(parentId);
    dto.setWire(Optional.ofNullable(wire).map(Wire::toDto).orElse(null));
    dto.setChildren(children);

    dto.setElementType(BasicElementType.LOAD);
    dto.setDrawPower(drawPower);
    dto.setPowerFactor(powerFactor);
    dto.setHighStartCurrent(highStartCurrent);

    return dto;
  }
}
