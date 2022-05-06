
### Features of rest client:

- [Rest client choice](#rest-client-choice)
- [Request/response debugging](#requestresponse-debugging-logdebugfilter)
- [Error handling](#error-handling-errorfilter)

### Rest client choice

Preferable rest clients
- [Resteasy rest client with proxy generation](resteasy_proxy_client)

Advantages:
- simplifies rest service creation -  you start with rest api definition, 
  clear separation between api and implementation;
- simplifies rest service consuming, via rest interface using, ideally provided by the rest service owners;
  client uses plain java api to work with java objects, without touching http- or rest-specific aspects.

In case you work with simple requests and manually handle `javax.ws.rs.core.Response`
it makes no difference between the following rest clients. You only set the right dependency.
- [Jax Rs Client with Jersey client](jersey_client)
- [Jax Rs Client with Resteasy client](resteasy_client)
- [Jax Rs Client with no compile dependency on Jersey/Resteasy](jax_rs_2_client)

### Request/response debugging (`LogDebugFilter`)

**Problem:** We need an option to enable debugging:
1. for a specific package 
2. at runtime 
3. to see all information about requests and responses.

Issue with existing solutions: no way to control debugging per specific services/functionality. 
Debug can be enabled/disabled only for a single package, which affects all rest clients.

Functional notes:
- You get all request and response meta information (http method, url, response code, headers)
- You get request and response bodies if it is text
- The filter tries to extract text information from gzip content.

[Example is based on](jax_rs_2_filters/src/test/java/com/savdev/rest/client/jax/rs/filter/LogDebugFilterTest.java):
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

### Error handling (`ErrorFilter`)

**Problem:** when issue occurred with a rest request we need:
- define a consumer for the issue, for instance to log request/response
- get all information about request and response. You need to be able to report a bug quickly with that information.
- be ale to clear identify, which situation is considered as failed, which as a normal case. 
  For instance in some case 404 response is not expected, in some case it is not an error.

Functional notes:
- you could define only a consumer, to consider each 4xx and 5xx response codes as error
- you could define both an error predicate and a consumer. 
  In that case you are responsible to identify if it is and error or not.

[Example is based on](jax_rs_2_filters/src/test/java/com/savdev/rest/client/jax/rs/filter/ErrorFilterTest.java):

```java
package com.company.service.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 * In this example you configure rest client with error consumer.
 * The consumer just logs request and response with error level.
 */
class RestClient {

  private final Logger logger = LogManager.getLogger(RestClient.class);

  public void sendRequest() {

    //used to serialize request you send for logging
    ObjectMapper customObjectMapper = new ObjectMapper();
    Client client = ClientBuilder.newBuilder().build();
    
    client.register(ErrorFilter.instance(
      customObjectMapper,
      //error consumer:
      (requestResponseInfo) -> logger.error(requestResponseInfo)));
    
    WebTarget target = client.target(wmRuntimeInfo.getHttpBaseUrl() + HTTP_URL);
    try (Response response = target.request().get()) {
      //do something with response
      String responseBody = response.readEntity(String.class);
    }
  }
}
```

```java
package com.company.service.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 * In this example you configure rest client with both error consumer and error predicate.
 */
class RestClient {

  private final Logger logger = LogManager.getLogger(RestClient.class);

  public void sendRequest() {

    //used to serialize request you send for logging
    ObjectMapper customObjectMapper = new ObjectMapper();
    Client client = ClientBuilder.newBuilder().build();
    
    client.register(ErrorFilter.instance(
      customObjectMapper,
      // error consumer:
      (requestResponseInfo) -> logger.error(requestResponseInfo),
      // error predicate:
      (responseContext) ->
        responseContext.getStatus() != HttpStatus.SC_NOT_FOUND
          && Sets.newHashSet(
            Response.Status.Family.CLIENT_ERROR,
            Response.Status.Family.SERVER_ERROR)
          .contains(Response.Status.Family.familyOf(responseContext.getStatus()))));
    
    WebTarget target = client.target(wmRuntimeInfo.getHttpBaseUrl() + HTTP_URL);
    try (Response response = target.request().get()) {
      //do something with response
      String responseBody = response.readEntity(String.class);
    }
  }
}
```
