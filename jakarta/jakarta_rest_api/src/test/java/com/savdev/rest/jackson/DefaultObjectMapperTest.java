package com.savdev.rest.jackson;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.savdev.rest.api.Item;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Optional;

class DefaultObjectMapperTest {

  public static final Long ITEM_ID = 777L;
  public static final String ITEM_NAME = "test item";
  public static final ZonedDateTime ZONED_DATE_TIME = ZonedDateTime.parse(
    "2025-08-24T18:35:06.0919+02:00[Europe/Berlin]");
  public static BigDecimal AMOUNT;

  @BeforeAll
  static void setup() throws ParseException {
    AMOUNT = (BigDecimal) new MoneySerializer().moneyFormat().parse("123,456.79");
  }

  /**
   * Convert object (serialize the object) to String and compare it with the file content from the test resources
   */
  @Test
  public void testSerialisationDto2String4Requests() throws Exception {
    //compare item deserialized as a string with the expected json file
    Assertions.assertEquals(
      IOUtils.toString(
        Optional.ofNullable(
        getClass().getClassLoader().getResourceAsStream("item.json"))
          .orElseThrow(),
        StandardCharsets.UTF_8),
      DefaultObjectMapper.instance()
        .writerWithDefaultPrettyPrinter()
        .writeValueAsString(
        new Item(ITEM_ID, ITEM_NAME, ZONED_DATE_TIME, AMOUNT))
        .replaceAll("\" :", "\":")
    );
  }

  /**
   * Deserialize the object from a String and compare it with the one created in memory
   */
  @Test
  public void testDeserialisationFromString2Dto4Responses() throws Exception {
    Assertions.assertEquals(
      new Item(ITEM_ID, ITEM_NAME, ZONED_DATE_TIME, AMOUNT),
      DefaultObjectMapper.instance().readValue(
        getClass().getClassLoader().getResourceAsStream("item.json"),
        Item.class));
  }
}