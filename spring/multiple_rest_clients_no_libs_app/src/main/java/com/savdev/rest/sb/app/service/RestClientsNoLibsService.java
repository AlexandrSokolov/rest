package com.savdev.rest.sb.app.service;

import com.savdev.rest.sb.app.rest.client1.RestApi1;
import com.savdev.rest.sb.app.rest.client2.RestApi2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RestClientsNoLibsService {

  @Autowired
  private RestApi1 restApi1;

  @Autowired
  private RestApi2 restApi2;

  public String restClient1StrValue() {
    return restApi1.getStrValue();
  }

  public String restClient2StrValue() {
    return restApi2.getStrValue();
  }
}
