package com.savdev.rest.sb.app.rest.client1;


import com.savdev.rest.sb.app.configs.rest.RestClient1Conf;
import com.savdev.rest.sb.app.rest.BaseRestClientService;
import org.springframework.stereotype.Service;

@Service
public class RestApi1Service extends BaseRestClientService<RestApi1> implements RestApi1 {


  public RestApi1Service(RestClient1Conf restClientConfiguration) {
    super(restClientConfiguration);
  }

  @Override
  public String getStrValue() {
    return proxyRestApi().getStrValue();
  }
}
