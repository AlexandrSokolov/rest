package com.savdev.rest.dto;

import com.savdev.rest.jackson.DefaultObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

class MultipleFormatsFieldAsObjectTest {

  public static final String TEST_NAME = "Test name";
  public static final BigDecimal TEST_DECIMAL_VALUE = new BigDecimal("12345.99");
  public static final String AS_NUMBER_JSON_FILE_NAME = "/multiple_formats_field_as_number.json";


  @Test
  public void testNumberSerialization() {

  }

  @Test
  public void testStringSerialization() {

  }

  @Test
  public void testMapSerialization() {

  }

  @Test
  public void testNumberDeserialization() throws IOException {
    var r = DefaultObjectMapper.instance().readValue(
      MultipleFormatsFieldAsObjectTest.class.getResourceAsStream(AS_NUMBER_JSON_FILE_NAME),
      MultipleFormatsFieldAsObject.class);
    Assertions.assertEquals(
      new MultipleFormatsFieldAsObject(TEST_NAME, TEST_DECIMAL_VALUE).toString(),
      r.toString());
    Assertions.assertEquals(
      BigInteger.ZERO.intValue(),
      TEST_DECIMAL_VALUE.compareTo(new BigDecimal(((Double) r.value()).toString())));
  }
  @Test
  public void testStringDeserialization() {

  }
  @Test
  public void testMapDeserialization() {

  }

}