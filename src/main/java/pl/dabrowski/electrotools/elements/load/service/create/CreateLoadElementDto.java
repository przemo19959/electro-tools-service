package pl.dabrowski.electrotools.elements.load.service.create;

import lombok.*;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class CreateLoadElementDto {
    private double x;
    private double y;
    private String label;
    private double drawPower;
    private UUID projectId;
}
