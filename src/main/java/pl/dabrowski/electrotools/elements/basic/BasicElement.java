package pl.dabrowski.electrotools.elements.basic;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import pl.dabrowski.electrotools.project.Project;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@MappedSuperclass
public class BasicElement {
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

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "project_id")
  protected Project project;

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
}
