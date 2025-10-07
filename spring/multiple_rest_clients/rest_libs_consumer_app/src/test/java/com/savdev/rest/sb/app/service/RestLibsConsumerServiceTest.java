package com.savdev.rest.sb.app.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RestLibsConsumerServiceTest {

  @Autowired
  private RestLibsConsumerService service;

  @Test
  void lib1StrValue() {
    assertEquals("Hello World", service.lib1StrValue());
  }

  @Test
  void lib2StrValue() {
    assertEquals("Hello World", service.lib2StrValue());
  }
}