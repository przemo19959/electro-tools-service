package pl.dabrowski.electrotools.elements.abstractelement;

import lombok.Getter;
import lombok.Setter;
import pl.dabrowski.electrotools.wire.service.read.ReadWireDto;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ReadAbstractElementDto {
  protected UUID id;
  protected double x;
  protected double y;
  protected String label;
  protected UUID parentId;
  protected ReadWireDto wire;
  //extra fields
  protected List<ReadAbstractElementDto> children;
  protected BasicElementType elementType;
}
