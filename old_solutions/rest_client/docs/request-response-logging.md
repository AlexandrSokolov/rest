### Request/response debugging (`LogDebugFilter`)

**Problem:** We need an option to enable logging:
1. for a specific package
2. at runtime
3. to see all the details about requests and responses to simplify maintenance.

Issue with existing solutions: no way to control debugging per specific services/functionality.
Debug can be enabled/disabled only for a single package, which affects all rest clients.

Functional notes:
- You get all request and response meta information (http method, url, response code, headers)
- You get request and response bodies if it is text
- The filter tries to extract text information from gzip content.

[Example is based on](../../rest_client/src/test/java/com/savdev/rest/client/filter/LogDebugFilterTest.java):
```java
package com.company.service.rest;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 * In this example you configure rest client to debug request and responses.
 * You need to enable debug logging for `com.company.service.rest` package. 
 * The package of your `RestClient` class.
 */
class RestClient {
  
  public void sendRequest() {
    
    //used to serialize request you send for logging
    ObjectMapper customObjectMapper = new ObjectMapper();
    Client client = ClientBuilder.newBuilder().build();
    client.register(LogDebugFilter.instance(RestClient.class, customObjectMapper));
    WebTarget target = client.target(wmRuntimeInfo.getHttpBaseUrl() + HTTP_URL);
    try (Response response = target.request().get()) {
      //do something with response
      String responseBody = response.readEntity(String.class);
    }
  }
}
```