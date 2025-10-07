package com.savdev.rest.client.lib2.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ContextConfiguration(classes = { RestClientTestConfiguration.class })
class RestApi2Test {

  @Autowired
  private RestApi2 restApi2;

  @Test
  public void getAll() {
    assertEquals("rrew", restApi2.getStrValue());
  }

}