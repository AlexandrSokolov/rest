package com.savdev.rest.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;

public class MoneySerializer extends JsonSerializer<BigDecimal> {

  private final DecimalFormat MONEY_FORMAT = new DecimalFormat("#,##0.00");

  public MoneySerializer() {
    super();
    MONEY_FORMAT.setParseBigDecimal(true);
  }

  @Override
  public void serialize(BigDecimal value, JsonGenerator jgen, SerializerProvider serializerProvider) throws IOException {
    jgen.writeString(MONEY_FORMAT.format(value));
  }

  public DecimalFormat moneyFormat() {
    return MONEY_FORMAT;
  }
}
