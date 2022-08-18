package pl.dabrowski.electrotools.elements.basic;

import pl.dabrowski.electrotools.AbstractAuditedEntity;
import pl.dabrowski.electrotools.project.Project;

import javax.persistence.*;
import java.util.UUID;

@MappedSuperclass
public class AbstractBasicElement extends AbstractAuditedEntity {
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
}
