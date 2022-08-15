package pl.dabrowski.electrotools.connection;

import lombok.Getter;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pl.dabrowski.electrotools.connection.service.create.CreateConnectionDto;
import pl.dabrowski.electrotools.connection.service.read.ReadConnectionDto;
import pl.dabrowski.electrotools.connection.service.update.UpdateConnectionDto;
import pl.dabrowski.electrotools.wire.Wire;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Table(name = "t_connections")
@EntityListeners(value = AuditingEntityListener.class)
@Audited
public class Connection {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "element_id")
    private UUID elementId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wire_id")
    private Wire wire;

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

    public static Connection create(CreateConnectionDto dto, Wire wire) {
        final Connection connection = new Connection();
        connection.elementId = dto.getElementId();
        connection.wire = wire;

        return connection;
    }

    public Connection update(UpdateConnectionDto dto, Wire wire) {
        this.wire = wire;


        return this;
    }

    public ReadConnectionDto toDto() {
        return ReadConnectionDto.builder()
            .id(id)
            .wire(wire.toDto())
            .build();
    }
}
