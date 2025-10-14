package com.savdev.rest.commons;

import jakarta.ws.rs.client.ClientRequestFilter;

public interface RestClientConfiguration {

  String serverUrl();

  ClientRequestFilter authFilter();
}