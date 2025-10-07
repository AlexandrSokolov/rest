package com.savdev.rest.sb.app.service;

import com.savdev.rest.client.lib1.api.RestApi1;
import com.savdev.rest.client.lib2.api.RestApi2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RestLibsConsumerService {

  @Autowired
  private RestApi1 restApi1;

  @Autowired
  private RestApi2 restApi2;

  public String lib1StrValue() {
    return restApi1.getStrValue();
  }

  public String lib2StrValue() {
    return restApi2.getStrValue();
  }
}
