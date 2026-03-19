package pl.dabrowski.electrotools.elements.basic.service.update;

import java.util.UUID;

public record UpdateBasicElementPositionDto(UUID elementId, double x, double y) {
}
