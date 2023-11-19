package com.savdev.rest.client.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;

public class MoneySerializer extends JsonSerializer<BigDecimal> {

  @Override
  public void serialize(BigDecimal value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
    // put your desired money style here
    // even for integer adds 2 digits: 123 becomes "123.00":
    // jgen.writeString(value.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
    //return 123 if it is nog decimal, returns 123.24 if it is decimal
    jgen.writeString(new DecimalFormat("#0.##").format(value));
  }
}
