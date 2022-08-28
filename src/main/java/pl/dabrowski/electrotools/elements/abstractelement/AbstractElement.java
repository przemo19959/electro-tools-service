package pl.dabrowski.electrotools.elements.abstractelement;

import lombok.Getter;
import pl.dabrowski.electrotools.AbstractAuditedEntity;
import pl.dabrowski.electrotools.project.Project;
import pl.dabrowski.electrotools.wire.Wire;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@MappedSuperclass
@Getter
public class AbstractElement extends AbstractAuditedEntity {
  @Id
  @GeneratedValue
  @Column(name = "id")
  protected UUID id;

  @Column(name = "x")
  protected double x;

  @Column(name = "y")
  protected double y;

  @Column(name = "label")
  protected String label;

  @Column(name = "parent_id")
  protected UUID parentId;

  @Embedded
  protected Wire wire;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "project_id")
  protected Project project;

  public static void create(AbstractElement entity,
                            CreateAbstractElementDto dto,
                            Project project) {
    entity.x = dto.getX();
    entity.y = dto.getY();
    entity.label = dto.getLabel();
    entity.parentId = dto.getParentId();
    entity.wire = Wire.create(dto.getWire());
    entity.project = project;
  }

  public AbstractElement update(UpdateAbstractElementDto dto) {
    this.x = dto.getX();
    this.y = dto.getY();
    this.label = dto.getLabel();
    this.parentId = dto.getParentId();
    if (this.wire == null) {
      this.wire = new Wire();
    }
    this.wire.update(dto.getWire());

    return this;
  }

  public ReadAbstractElementDto toDto(List<ReadAbstractElementDto> children) {
    ReadAbstractElementDto dto = new ReadAbstractElementDto();
    dto.setId(id);
    dto.setX(x);
    dto.setY(y);
    dto.setLabel(label);
    dto.setParentId(parentId);
    dto.setWire(Optional.ofNullable(wire).map(Wire::toDto).orElse(null));
    dto.setChildren(children);

    return dto;
  }
}
