package com.savdev.rest.client.lib1.config;

import jakarta.ws.rs.client.ClientRequestFilter;

public interface Lib1RestClientConfiguration {

  String serverUrl();

  ClientRequestFilter authFilter();
}
