package pl.dabrowski.electrotools.elements.overcurrentprotection.service.read;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import pl.dabrowski.electrotools.elements.basic.service.read.ReadBasicElementDto;
import pl.dabrowski.electrotools.elements.overcurrentprotection.OvercurrentProtectionType;

@Getter
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReadOvercurrentProtectionElementDto extends ReadBasicElementDto {
    private final OvercurrentProtectionType type;
    private final int amperage;
}
