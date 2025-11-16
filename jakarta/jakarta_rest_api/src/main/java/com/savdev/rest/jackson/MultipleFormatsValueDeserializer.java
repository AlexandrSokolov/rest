package com.savdev.rest.jackson;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.savdev.rest.dto.MultipleFormatsFieldValue;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;

public class MultipleFormatsValueDeserializer extends JsonDeserializer<MultipleFormatsFieldValue<?>> {

  @Override
  public MultipleFormatsFieldValue<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {

    JsonNode node = p.getCodec().readTree(p);

    if (node.isNumber()) {
      return new MultipleFormatsFieldValue<>(node.decimalValue());
    } else  if (node.isTextual()) {
      return new MultipleFormatsFieldValue<>(node.asText());
    }
    else if (node.isObject()) {
      return new MultipleFormatsFieldValue<>(
        node.properties().stream()
          .map(e -> new AbstractMap.SimpleEntry<>(
            e.getKey(),
            e.getValue().asText()))
          .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
    }

    ctxt.reportInputMismatch(MultipleFormatsFieldValue.class, "Expected number or string or object for 'value'");
    return null;
  }
}
