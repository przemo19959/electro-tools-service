package pl.dabrowski.electrotools.wire.phase;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = PhaseDeserializer.class)
public enum PhaseType {
  ONE,
  THREE;
}
