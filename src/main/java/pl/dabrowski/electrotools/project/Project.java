package pl.dabrowski.electrotools.project;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pl.dabrowski.electrotools.AbstractAuditedEntity;
import pl.dabrowski.electrotools.project.service.create.CreateProjectDto;
import pl.dabrowski.electrotools.project.service.read.ReadProjectDto;
import pl.dabrowski.electrotools.project.service.update.UpdateProjectDto;

import java.util.UUID;

@Entity
@Getter
@Table(name = "t_projects")
@EntityListeners(value = AuditingEntityListener.class)
@Audited
public class Project extends AbstractAuditedEntity {
  @Id
  @GeneratedValue
  @Column(name = "id")
  private UUID id;

  @Column(name = "name")
  private String name;

  public static Project create(CreateProjectDto dto) {
    final Project project = new Project();
    project.name = dto.name();
    return project;
  }

  public Project update(UpdateProjectDto dto) {
    this.name = dto.name();
    return this;
  }

  public ReadProjectDto toDto() {
    return ReadProjectDto.builder()
        .id(id)
        .name(name)
        .createdBy(createdBy)
        .modifiedBy(modifiedBy)
        .modifiedDate(modifiedDate)
        .build();
  }
}
