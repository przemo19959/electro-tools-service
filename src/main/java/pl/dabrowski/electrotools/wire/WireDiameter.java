package pl.dabrowski.electrotools.wire;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum WireDiameter {
  D_05(0.5),
  D_075(0.75),
  D_1(1),
  D_15(1.5),
  D_25(2.5),
  D_40(4),
  D_60(6),
  D_100(10),
  D_160(16),
  D_250(25),
  D_350(35),
  D_500(50),
  D_700(70),
  D_950(95),
  D_1200(120);

  @JsonValue
  private final double value;

  @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
  public static WireDiameter fromValue(double value) {
    for (WireDiameter b : WireDiameter.values()) {
      if (b.value == value) {
        return b;
      }
    }
    return null;
  }
}
