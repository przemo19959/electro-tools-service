package pl.dabrowski.electrotools.elements.basic.service.read;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class ReadBasicElementDto {
  protected UUID id;
  protected double x;
  protected double y;
  protected String label;
}
