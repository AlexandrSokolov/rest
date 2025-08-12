package com.savdev.rest.service;

import com.savdev.rest.api.RestApi;
import com.savdev.rest.api.model.Message;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
public class AppRestService implements RestApi {

  @Override
  public Message get() {
    return new Message("Hello!", ZonedDateTime.now());
  }
}
