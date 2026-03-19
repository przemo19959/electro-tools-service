package pl.dabrowski.electrotools.wire;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import pl.dabrowski.electrotools.wire.phase.PhaseType;
import pl.dabrowski.electrotools.wire.service.create.CreateWireDto;
import pl.dabrowski.electrotools.wire.service.read.ReadWireDto;
import pl.dabrowski.electrotools.wire.service.update.UpdateWireDto;

@Getter
@Embeddable
public class Wire {
  @Enumerated(EnumType.STRING)
  @Column(name = "diameter")
  private WireDiameter diameter;

  @Enumerated(EnumType.STRING)
  @Column(name = "placement")
  private PlacementType placement;

  @Enumerated(EnumType.STRING)
  @Column(name = "wire_type")
  private WireType wireType;

  @Enumerated(EnumType.STRING)
  @Column(name = "phase")
  private PhaseType phase;

  @Positive
  @Column(name = "length")
  private Double length = 5.0;

  public static Wire create(CreateWireDto dto) {
    if (dto != null) {
      final Wire wire = new Wire();

      wire.diameter = dto.getDiameter();
      wire.placement = dto.getPlacement();
      wire.wireType = dto.getType();
      wire.phase = dto.getPhase();
      wire.length = dto.getLength();

      return wire;
    }
    return null;
  }

  public Wire update(UpdateWireDto dto) {
    if (dto != null) {
      this.diameter = dto.getDiameter();
      this.placement = dto.getPlacement();
      this.wireType = dto.getType();
      this.phase = dto.getPhase();
      this.length = dto.getLength();
    }

    return this;
  }

  public ReadWireDto toDto() {
    return new ReadWireDto(diameter, placement, wireType, phase, length);
  }
}
