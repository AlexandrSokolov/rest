package com.savdev.rest.dto;

import com.savdev.rest.jackson.DefaultObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

class MultipleFormatsFieldViaGenericsTest {
  public static final String TEST_NAME = "Test name";
  public static final BigDecimal TEST_DECIMAL_VALUE = new BigDecimal("12345.99");
  public static final String TEST_STRING_VALUE = "some string";
  public static final String TEST_LABEL_STRING_VALUE = "test label";
  public static final String AS_NUMBER_JSON_FILE_NAME = "/multiple_formats_field_as_number.json";
  public static final String AS_STRING_JSON_FILE_NAME = "/multiple_formats_field_as_string.json";
  public static final String AS_MAP_JSON_FILE_NAME = "/multiple_formats_field_as_map.json";


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
      MultipleFormatsFieldViaGenerics.class);
    Assertions.assertEquals(
      new MultipleFormatsFieldViaGenerics(
        TEST_NAME,
        new MultipleFormatsFieldValue<>(TEST_DECIMAL_VALUE)),
      r);
    //to extract number value for client:
    Assertions.assertEquals(
      BigInteger.ZERO.intValue(),
      TEST_DECIMAL_VALUE.compareTo(r.value().asDecimal()));
  }
  @Test
  public void testStringDeserialization() throws IOException {
    var r = DefaultObjectMapper.instance().readValue(
      MultipleFormatsFieldAsObjectTest.class.getResourceAsStream(AS_STRING_JSON_FILE_NAME),
      MultipleFormatsFieldViaGenerics.class);
    Assertions.assertEquals(
      new MultipleFormatsFieldViaGenerics(
        TEST_NAME,
        new MultipleFormatsFieldValue<>(TEST_STRING_VALUE)),
      r);
    //to extract number value for client:
    Assertions.assertEquals(
      TEST_STRING_VALUE,
      r.value().asText());
  }

  @Test
  public void testMapDeserialization() throws IOException {
    var r = DefaultObjectMapper.instance().readValue(
      MultipleFormatsFieldAsObjectTest.class.getResourceAsStream(AS_MAP_JSON_FILE_NAME),
      MultipleFormatsFieldViaGenerics.class);
    Assertions.assertEquals(
      new MultipleFormatsFieldViaGenerics(
        TEST_NAME,
        new MultipleFormatsFieldValue<>(Map.of(
          "label", TEST_LABEL_STRING_VALUE))),
      r);
    //to extract number value for client:
    Assertions.assertEquals(
      TEST_LABEL_STRING_VALUE,
      r.value().asMap().get("label"));

  }
}