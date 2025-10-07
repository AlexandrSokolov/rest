package com.savdev.rest.sb.app.rest.client2;


import com.savdev.rest.sb.app.configs.rest.RestClient2Conf;
import com.savdev.rest.sb.app.rest.BaseRestClientService;
import org.springframework.stereotype.Service;

@Service
public class RestApi2Service extends BaseRestClientService<RestApi2> implements RestApi2 {

  public RestApi2Service(RestClient2Conf restClientConfiguration) {
    super(restClientConfiguration);
  }

  @Override
  public String getStrValue() {
    return proxyRestApi().getStrValue();
  }
}
