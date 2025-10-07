package com.savdev.rest.sb.app.configs.rest;

import jakarta.ws.rs.client.ClientRequestFilter;

public interface RestClientConfiguration {

  String serverUrl();

  ClientRequestFilter authFilter();
}
