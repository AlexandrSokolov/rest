package com.savdev.rest.jackson;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.time.ZonedDateTime;

public class MoneyDeserializer extends JsonDeserializer<BigDecimal> {

  @Override
  public BigDecimal deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
    throws IOException, JacksonException {
    try {
      return (BigDecimal) new MoneySerializer().moneyFormat()
        .parse(
          jsonParser.getValueAsString());
    } catch (ParseException e) {
      throw new IllegalStateException(e);
    }
  }
}
