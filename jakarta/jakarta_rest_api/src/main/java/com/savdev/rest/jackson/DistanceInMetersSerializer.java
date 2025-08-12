package com.savdev.rest.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.savdev.rest.dto.DistanceInMeters;

import java.io.IOException;

public class DistanceInMetersSerializer extends JsonSerializer<DistanceInMeters> {

  @Override
  public void serialize(
    DistanceInMeters distance,
    JsonGenerator jgen, SerializerProvider serializerProvider) throws IOException {
    jgen.writeStartObject();
    jgen.writeFieldName("name");
    jgen.writeString(distance.name());
    jgen.writeFieldName("unit");
    jgen.writeString(distance.getUnit());
    jgen.writeFieldName("meters");
    jgen.writeNumber(distance.getMeters());
    jgen.writeEndObject();
  }
}
