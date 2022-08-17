package pl.dabrowski.electrotools.elements.load.service.update;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.dabrowski.electrotools.elements.basic.service.update.UpdateBasicElementDto;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class UpdateLoadElementDto extends UpdateBasicElementDto {
  private double drawPower;
}
