package pl.dabrowski.electrotools.elements.abstractelement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.dabrowski.electrotools.wire.service.create.CreateWireDto;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateAbstractElementDto {
  protected double x;
  protected double y;
  protected String label;
  protected UUID parentId;
  protected CreateWireDto wire;
  protected UUID projectId;
}
