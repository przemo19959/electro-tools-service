package pl.dabrowski.electrotools.wire;

import lombok.Getter;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pl.dabrowski.electrotools.wire.service.create.CreateWireDto;
import pl.dabrowski.electrotools.wire.service.read.ReadWireDto;
import pl.dabrowski.electrotools.wire.service.update.UpdateWireDto;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Table(name = "t_wires")
@EntityListeners(value = AuditingEntityListener.class)
@Audited
public class Wire {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "diameter")
    private WireDiameter diameter;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "symbol")
    private WireSymbol symbol;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "placement")
    private PlacementType placement;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "type")
    private WireType type;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "phase")
    private PhaseType phase;

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

    public static Wire create(CreateWireDto dto) {
        final Wire wire = new Wire();
        wire.diameter = dto.getDiameter();
        wire.symbol = dto.getSymbol();
        wire.placement = dto.getPlacement();
        wire.type = dto.getType();
        wire.phase = dto.getPhase();
        return wire;
    }

    public Wire update(UpdateWireDto dto) {
        this.diameter = dto.getDiameter();
        this.symbol = dto.getSymbol();
        this.placement = dto.getPlacement();
        this.type = dto.getType();
        this.phase = dto.getPhase();
        return this;
    }

    public ReadWireDto toDto() {
        return ReadWireDto.builder().id(id).diameter(diameter).symbol(symbol).placement(placement).type(type).phase(phase).build();
    }
}
