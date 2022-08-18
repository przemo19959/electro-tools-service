package pl.dabrowski.electrotools.connection;

import lombok.Getter;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pl.dabrowski.electrotools.AbstractAuditedEntity;
import pl.dabrowski.electrotools.connection.service.create.CreateConnectionDto;
import pl.dabrowski.electrotools.connection.service.read.ReadConnectionDto;
import pl.dabrowski.electrotools.connection.service.update.UpdateConnectionDto;
import pl.dabrowski.electrotools.wire.Wire;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Table(name = "t_connections")
@EntityListeners(value = AuditingEntityListener.class)
@Audited
public class Connection extends AbstractAuditedEntity {
  @Id
  @GeneratedValue
  @Column(name = "id")
  private UUID id;

  @Column(name = "from_element_id")
  private UUID fromElementId;

  @Column(name = "to_element_id")
  private UUID toElementId;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "wire_id")
  private Wire wire;

  public static Connection create(CreateConnectionDto dto, Wire wire) {
    final Connection connection = new Connection();
    connection.fromElementId = dto.getFromElementId();
    connection.toElementId = dto.getToElementId();
    connection.wire = wire;

    return connection;
  }

  public Connection update(UpdateConnectionDto dto, Wire wire) {
    this.fromElementId = dto.getFromElementId();
    this.toElementId = dto.getToElementId();
    this.wire = wire;

    return this;
  }

  public ReadConnectionDto toDto() {
    return ReadConnectionDto.builder()
        .id(id)
        .fromElementId(fromElementId)
        .toElementId(toElementId)
        .wire(wire.toDto())
        .build();
  }
}
