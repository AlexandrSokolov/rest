package com.savdev.rest.client.lib2.config;

import jakarta.ws.rs.client.ClientRequestFilter;

public interface Lib2RestClientConfiguration {

  String serverUrl();

  ClientRequestFilter authFilter();
}
