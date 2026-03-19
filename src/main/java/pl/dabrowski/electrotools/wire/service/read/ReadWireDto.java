package pl.dabrowski.electrotools.wire.service.read;

import pl.dabrowski.electrotools.wire.PlacementType;
import pl.dabrowski.electrotools.wire.WireDiameter;
import pl.dabrowski.electrotools.wire.WireType;
import pl.dabrowski.electrotools.wire.phase.PhaseType;

public record ReadWireDto(
        WireDiameter diameter,
        PlacementType placement,
        WireType type,
        PhaseType phase,
        Double length
) {
}
