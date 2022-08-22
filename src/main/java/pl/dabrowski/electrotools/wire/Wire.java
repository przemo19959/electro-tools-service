package pl.dabrowski.electrotools.wire;

import lombok.Getter;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pl.dabrowski.electrotools.AbstractAuditedEntity;
import pl.dabrowski.electrotools.wire.service.create.CreateWireDto;
import pl.dabrowski.electrotools.wire.service.read.ReadWireDto;
import pl.dabrowski.electrotools.wire.service.update.UpdateWireDto;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import java.util.UUID;

@Entity
@Getter
@Table(name = "t_wires")
@EntityListeners(value = AuditingEntityListener.class)
@Audited
public class Wire extends AbstractAuditedEntity {
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

    @Positive
    @Column(name = "length")
    private Double length = 5.0;

    public static Wire create(CreateWireDto dto) {
        final Wire wire = new Wire();
        wire.diameter = dto.getDiameter();
        wire.symbol = dto.getSymbol();
        wire.placement = dto.getPlacement();
        wire.type = dto.getType();
        wire.phase = dto.getPhase();
        wire.length = dto.getLength();

        return wire;
    }

    public Wire update(UpdateWireDto dto) {
        this.diameter = dto.getDiameter();
        this.symbol = dto.getSymbol();
        this.placement = dto.getPlacement();
        this.type = dto.getType();
        this.phase = dto.getPhase();
        this.length = dto.getLength();

        return this;
    }

    public ReadWireDto toDto() {
        return ReadWireDto.builder()
            .id(id)
            .diameter(diameter)
            .symbol(symbol)
            .placement(placement)
            .type(type)
            .phase(phase)
            .length(length)
            .build();
    }
}
