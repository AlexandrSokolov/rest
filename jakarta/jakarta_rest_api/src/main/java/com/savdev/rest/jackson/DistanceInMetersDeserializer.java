package com.savdev.rest.jackson;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.savdev.rest.dto.DistanceInMeters;

import java.io.IOException;

public class DistanceInMetersDeserializer extends JsonDeserializer<DistanceInMeters> {

  @Override
  public DistanceInMeters deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
    throws IOException, JacksonException {

    JsonNode node = jsonParser.getCodec().readTree(jsonParser);

    String unit = node.get("unit").asText();
    double meters = node.get("meters").asDouble();

    for (DistanceInMeters distance : DistanceInMeters.values()) {

      if (distance.getUnit().equals(unit) && Double.compare(distance.getMeters(), meters) == 0) {
        return distance;
      }
    }

    return null;
  }
}
