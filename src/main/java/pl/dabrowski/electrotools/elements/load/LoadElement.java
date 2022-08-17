package pl.dabrowski.electrotools.elements.load;

import lombok.Getter;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pl.dabrowski.electrotools.elements.basic.AbstractBasicElement;
import pl.dabrowski.electrotools.elements.load.service.create.CreateLoadElementDto;
import pl.dabrowski.electrotools.elements.load.service.read.ReadLoadElementDto;
import pl.dabrowski.electrotools.elements.load.service.update.UpdateLoadElementDto;
import pl.dabrowski.electrotools.project.Project;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;

@Entity
@Getter
@Table(name = "t_load_elements")
@EntityListeners(value = AuditingEntityListener.class)
@Audited
public class LoadElement extends AbstractBasicElement {
  @Column(name = "draw_power")
  private double drawPower;

  public static LoadElement create(CreateLoadElementDto dto, Project project) {
    final LoadElement loadElement = new LoadElement();
    loadElement.x = dto.getX();
    loadElement.y = dto.getY();
    loadElement.label = dto.getLabel();
    loadElement.drawPower = dto.getDrawPower();
    loadElement.project = project;

    return loadElement;
  }

  public LoadElement update(UpdateLoadElementDto dto) {
    this.x = dto.getX();
    this.y = dto.getY();
    this.label = dto.getLabel();
    this.drawPower = dto.getDrawPower();
    return this;
  }

  public ReadLoadElementDto toDto() {
    return ReadLoadElementDto.builder()
        .id(id)
        .x(x)
        .y(y)
        .label(label)
        .drawPower(drawPower)
        .build();
  }
}
