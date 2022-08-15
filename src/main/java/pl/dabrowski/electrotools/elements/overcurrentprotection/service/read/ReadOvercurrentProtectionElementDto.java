package pl.dabrowski.electrotools.elements.overcurrentprotection.service.read;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import pl.dabrowski.electrotools.elements.overcurrentprotection.OvercurrentProtectionType;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReadOvercurrentProtectionElementDto {
    private final UUID id;
    private final double x;
    private final double y;
    private final String label;
    private final OvercurrentProtectionType type;
    private final int amperage;
}
