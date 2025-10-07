package com.savdev.rest.client.lib2.service;

import com.savdev.rest.client.lib2.api.RestApi2;
import com.savdev.rest.client.lib2.config.Lib2RestClientConfiguration;
import com.savdev.rest.commons.BaseRestClientService;
import org.springframework.stereotype.Service;

@Service
public class RestApi2Service extends BaseRestClientService<RestApi2> implements RestApi2 {

  public RestApi2Service(Lib2RestClientConfiguration restClientConfiguration) {
    super(restClientConfiguration);
  }

  @Override
  public String getStrValue() {
    return proxyRestApi().getStrValue();
  }
}
