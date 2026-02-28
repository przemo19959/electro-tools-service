package pl.dabrowski.electrotools.elements.rcdelement;

import lombok.Getter;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pl.dabrowski.electrotools.elements.abstractelement.AbstractElement;
import pl.dabrowski.electrotools.elements.abstractelement.BasicElementType;
import pl.dabrowski.electrotools.elements.abstractelement.ReadAbstractElementDto;
import pl.dabrowski.electrotools.elements.rcdelement.service.create.CreateRcdElementDto;
import pl.dabrowski.electrotools.elements.rcdelement.service.read.ReadRcdElementDto;
import pl.dabrowski.electrotools.elements.rcdelement.service.update.UpdateRcdElementDto;
import pl.dabrowski.electrotools.project.Project;
import pl.dabrowski.electrotools.wire.Wire;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Positive;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@Table(name = "t_rcd_elements")
@EntityListeners(value = AuditingEntityListener.class)
@Audited
public class RcdElement extends AbstractElement {
  @Column(name = "nominal_current")
  @Positive
  private int nominalCurrent;

  @Column(name = "diff_current")
  @Positive
  private int diffCurrent;

  @Column(name = "pole_number")
  @Positive
  private int poleNumber = 2;

  public static RcdElement create(CreateRcdElementDto dto, Project project) {
    final RcdElement rcdElement = new RcdElement();
    AbstractElement.create(rcdElement, dto, project);

    rcdElement.nominalCurrent = dto.getNominalCurrent();
    rcdElement.diffCurrent = dto.getDiffCurrent();
    rcdElement.poleNumber = dto.getPoleNumber();

    return rcdElement;
  }

  public RcdElement update(UpdateRcdElementDto dto) {
    super.update(dto);
    this.nominalCurrent = dto.getNominalCurrent();
    this.diffCurrent = dto.getDiffCurrent();
    this.poleNumber = dto.getPoleNumber();

    return this;
  }

  @Override
  public ReadAbstractElementDto toDto(List<ReadAbstractElementDto> children) {
    ReadRcdElementDto dto = new ReadRcdElementDto();
    dto.setId(id);
    dto.setX(x);
    dto.setY(y);
    dto.setLabel(label);
    dto.setParentId(parentId);
    dto.setWire(Optional.ofNullable(wire).map(Wire::toDto).orElse(null));
    dto.setChildren(children);

    dto.setElementType(BasicElementType.RCD);
    dto.setNominalCurrent(nominalCurrent);
    dto.setDiffCurrent(diffCurrent);
    dto.setPoleNumber(poleNumber);

    return dto;
  }
}
