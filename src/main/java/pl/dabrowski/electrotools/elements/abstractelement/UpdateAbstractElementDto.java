package pl.dabrowski.electrotools.elements.abstractelement;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.dabrowski.electrotools.wire.service.update.UpdateWireDto;

import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class UpdateAbstractElementDto {
  protected double x;
  protected double y;
  protected String label;
  protected UUID parentId;
  protected UpdateWireDto wire;
}
