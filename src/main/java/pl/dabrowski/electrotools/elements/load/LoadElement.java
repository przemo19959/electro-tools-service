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

import javax.persistence.*;
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

  @Column(name = "config")
  @Enumerated(EnumType.STRING)
  private Config config;

  @Column(name = "zeroed")
  private Boolean zeroed;

  public static LoadElement create(CreateLoadElementDto dto, Project project) {
    final LoadElement load = new LoadElement();
    AbstractElement.create(load, dto, project);

    load.drawPower = dto.getDrawPower();
    load.powerFactor = dto.getPowerFactor();
    load.highStartCurrent = dto.isHighStartCurrent();
    load.config = dto.getConfig();
    load.zeroed = dto.isZeroed();

    return load;
  }

  public LoadElement update(UpdateLoadElementDto dto) {
    super.update(dto);
    this.drawPower = dto.getDrawPower();
    this.powerFactor = dto.getPowerFactor();
    this.highStartCurrent = dto.isHighStartCurrent();
    this.config = dto.getConfig();
    this.zeroed = dto.isZeroed();

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
    dto.setConfig(config);
    dto.setZeroed(Optional.ofNullable(zeroed).orElse(Boolean.FALSE));

    return dto;
  }
}
