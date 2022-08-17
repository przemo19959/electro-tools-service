package pl.dabrowski.electrotools.elements.basic.service.create;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class CreateBasicElementDto {
  protected double x;
  protected double y;
  protected String label;
  protected UUID projectId;
}
