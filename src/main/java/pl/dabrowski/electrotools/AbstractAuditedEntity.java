package pl.dabrowski.electrotools;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

@MappedSuperclass
public class AbstractAuditedEntity {
  @Version
  @Column(name = "version")
  protected Integer version;

  @CreatedBy
  @Column(name = "created_by", updatable = false)
  protected String createdBy;

  @CreatedDate
  @Column(name = "created_date", updatable = false)
  protected Instant createdDate;

  @LastModifiedBy
  @Column(name = "modified_by")
  protected String modifiedBy;

  @LastModifiedDate
  @Column(name = "modified_date")
  protected Instant modifiedDate;
}
