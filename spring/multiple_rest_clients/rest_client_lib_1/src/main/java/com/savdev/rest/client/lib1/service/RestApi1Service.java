package com.savdev.rest.client.lib1.service;

import com.savdev.rest.client.lib1.api.RestApi1;
import com.savdev.rest.client.lib1.config.Lib1RestClientConfiguration;
import com.savdev.rest.commons.BaseRestClientService;
import org.springframework.stereotype.Service;

@Service
public class RestApi1Service extends BaseRestClientService<RestApi1> implements RestApi1 {


  public RestApi1Service(Lib1RestClientConfiguration restClientConfiguration) {
    super(restClientConfiguration);
  }

  @Override
  public String getStrValue() {
    return proxyRestApi().getStrValue();
  }
}
