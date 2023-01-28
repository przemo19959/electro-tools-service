package pl.dabrowski.electrotools.wire.phase;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class PhaseDeserializer extends StdDeserializer<PhaseType> {
  public PhaseDeserializer() {
    this(null);
  }

  public PhaseDeserializer(Class<?> vc) {
    super(vc);
  }

  @Override
  public PhaseType deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
    TreeNode phaseObj = jsonParser.getCodec().readTree(jsonParser);
    return PhaseType.valueOf(phaseObj.get("id").toString().replace("\"", ""));
  }
}
