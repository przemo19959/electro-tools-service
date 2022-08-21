package pl.dabrowski.electrotools.elements.load.service.read;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import pl.dabrowski.electrotools.elements.basic.service.read.ReadBasicElementDto;

@Getter
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReadLoadElementDto extends ReadBasicElementDto {
  private final double drawPower;
  private final double powerFactor;
}
