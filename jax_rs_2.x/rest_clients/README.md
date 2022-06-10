
### Features of rest client:

- [Rest client choice](#rest-client-choice)
- [Usage of rest client in the managed environment](client_usage_managed))
- [Usage of rest client, rules](client_usage/README.md)
- [Request/response debugging](#requestresponse-debugging-logdebugfilter)
- [Error handling](#error-handling-errorfilter)
- [URL matrix parameters vs. query parameters](#url-matrix-parameters-vs-query-parameters)

### Rest client choice

**Preferable rest clients**
- [Resteasy rest client with proxy generation](resteasy_proxy_client/README.md)

Advantages:
- simplifies rest service creation -  you start with rest api definition, 
  clear separation between api and implementation;
- simplifies rest service consuming, via rest interface using, ideally provided by the rest service owners;
  client uses plain java api to work with java objects, without touching http- or rest-specific aspects.

In case you work with simple requests and manually handle `javax.ws.rs.core.Response`
it makes no difference between the following rest clients. You only set the right dependency.
- [Jax Rs Client with Jersey client](jersey_client)
- [Jax Rs Client with Resteasy client](resteasy_client)
- [Jax Rs Client with run-time dependency](jax_rs_2_client)

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
### URL matrix parameters vs. query parameters

Matrix parameters: `http://www.example.com/example-page;field1=value1;field2=value2;field3=value3/other-example-page`

vs

Query parameters `http://www.example.com/example-page?field1=value1&field2=value2&field3=value3`

[Matrix parameters examples](resteasy_proxy_client/README.md#passing-matrix-parameters)

1. Matrix parameters apply to a particular path element while query parameters apply to the request as a whole. 
This comes into play when making a complex REST-style query to multiple levels of resources and sub-resources:

`http://example.com/res/categories;name=foo/objects;name=green/?page=1`

It comes down to namespacing.

Note: The 'levels' of resources here are categories and objects.

If only query parameters were used for a multi-level URL, you would end up with:

`http://example.com/res?categories_name=foo&objects_name=green&page=1`

This way you would also lose the clarity added by the locality of the parameters within the request. 

2. When using a framework like JAX-RS, all the query parameters would show up within each resource handler, 
leading to potential conflicts and confusion.

3. Matrix parameters particular useful with the uri template when url is defined via (unnamed) parameters.

[URL matrix parameters vs. query parameters on stackoverflow](https://stackoverflow.com/questions/2048121/url-matrix-parameters-vs-query-parameters)