package pl.dabrowski.electrotools.elements.overcurrentprotection.service.update;

import lombok.*;
import pl.dabrowski.electrotools.elements.overcurrentprotection.OvercurrentProtectionType;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class UpdateOvercurrentProtectionElementDto {
  private double x;
  private double y;
  private String label;
  private OvercurrentProtectionType type;
  private int amperage;
}
