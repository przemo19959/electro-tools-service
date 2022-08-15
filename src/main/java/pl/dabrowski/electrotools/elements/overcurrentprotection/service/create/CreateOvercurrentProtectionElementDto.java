package pl.dabrowski.electrotools.elements.overcurrentprotection.service.create;

import lombok.*;
import pl.dabrowski.electrotools.elements.overcurrentprotection.OvercurrentProtectionType;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class CreateOvercurrentProtectionElementDto {
    private double x;
    private double y;
    private String label;
    private OvercurrentProtectionType type;
    private int amperage;
    private UUID projectId;
}
