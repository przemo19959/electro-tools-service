package pl.dabrowski.electrotools.project;

import lombok.Getter;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pl.dabrowski.electrotools.project.service.create.CreateProjectDto;
import pl.dabrowski.electrotools.project.service.read.ReadProjectDto;
import pl.dabrowski.electrotools.project.service.update.UpdateProjectDto;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Table(name = "t_projects")
@EntityListeners(value = AuditingEntityListener.class)
@Audited
public class Project {
  @Id
  @GeneratedValue
  @Column(name = "id")
  private UUID id;

  @Column(name = "name")
  private String name;

  @Version
  @Column(name = "version")
  private Integer version;

  @CreatedBy
  @Column(name = "created_by", updatable = false)
  private String createdBy;

  @CreatedDate
  @Column(name = "created_date", updatable = false)
  private Instant createdDate;

  @LastModifiedBy
  @Column(name = "modified_by")
  private String modifiedBy;

  @LastModifiedDate
  @Column(name = "modified_date")
  private Instant modifiedDate;

  public static Project create(CreateProjectDto dto) {
    final Project project = new Project();
    project.name = dto.getName();
    return project;
  }

  public Project update(UpdateProjectDto dto) {
    this.name = dto.getName();
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
