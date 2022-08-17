package pl.dabrowski.electrotools.elements.basic;

import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pl.dabrowski.electrotools.elements.basic.service.create.CreateBasicElementDto;
import pl.dabrowski.electrotools.elements.basic.service.read.ReadBasicElementDto;
import pl.dabrowski.electrotools.elements.basic.service.update.UpdateBasicElementDto;
import pl.dabrowski.electrotools.project.Project;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;

@Entity
@Getter
@Table(name = "t_basic_elements")
@EntityListeners(value = AuditingEntityListener.class)
public class BasicElement extends AbstractBasicElement {
  public static BasicElement create(CreateBasicElementDto dto, Project project) {
    BasicElement basicElement = new BasicElement();
    basicElement.x = dto.getX();
    basicElement.y = dto.getY();
    basicElement.label = dto.getLabel();
    basicElement.project = project;

    return basicElement;
  }

  public BasicElement update(UpdateBasicElementDto dto) {
    this.x = dto.getX();
    this.y = dto.getY();
    this.label = dto.getLabel();

    return this;
  }

  public ReadBasicElementDto toDto() {
    return ReadBasicElementDto.builder()
        .id(id)
        .x(x)
        .y(y)
        .label(label)
        .build();
  }
}
