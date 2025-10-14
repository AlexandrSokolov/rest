### [Rest Clients FAQ](../../Rest.Clients.FAQ.md)

### Commons rest client useful features
- [Rest logging](#rest-logging)

Other topics:
- [Reasons to create `RestClientConfiguration`](#reasons-to-create-restclientconfiguration)

### Rest logging

### Reasons to create `RestClientConfiguration`

Without this interface we could make `BaseRestClientService` simpler by passing only:
- server url as `String` and 
- authentication request filter as `ClientRequestFilter`
```java
public abstract class BaseRestClientService<T> {
 
  public BaseRestClientService(
    String serverUrl, ClientRequestFilter authFilter) {
    this.serverUrl = serverUrl;
    this.authFilter = authFilter;
  }
}
```

Rest client libraries creators must specify how to pass these attributes to add the library.
And it might happen, that for the multiple libraries the same server url and authentication methods are needed.
We could combine such configurations easily with interfaces, that have the same methods.
And having `RestClientConfiguration` helps us to make sure those method names are the same:
```java
public interface RestClientConfiguration {
  String serverUrl();
  ClientRequestFilter authFilter();
}
```
In independent rest client libraries we define configs and rest services as:
```java
public interface Lib1RestClientConfiguration extends RestClientConfiguration {
}
@Service
public class RestApi1Service extends BaseRestClientService<RestApi1> implements RestApi1 {
  public RestApi1Service(Lib1RestClientConfiguration restClientConfiguration) {
    super(
      restClientConfiguration.serverUrl(),
      restClientConfiguration.authFilter());
  }
}

public interface Lib2RestClientConfiguration extends RestClientConfiguration {
}
@Service
public class RestApi2Service extends BaseRestClientService<RestApi2> implements RestApi2 {
  public RestApi2Service(Lib2RestClientConfiguration restClientConfiguration) {
    super(
      restClientConfiguration.serverUrl(),
      restClientConfiguration.authFilter());
  }
}
```
Now in the app, we can define same configuration for all the libraries 
that have the same server url and authentication logic, 
if needed:
```java
@Configuration
@ComponentScan({
  "com.savdev.rest.client.lib1.service",
  "com.savdev.rest.client.lib2.service",
})
public class SharedRestClientConfig implements Lib1RestClientConfiguration, Lib2RestClientConfiguration {

  @Override
  public String serverUrl() {
    return SERVER_URL;
  }

  @Override
  public ClientRequestFilter authFilter() {
    return null;
  }
}
```
Without this `RestClientConfiguration` we might meet a case when have to implement multiple methods with different name, 
that have the same purpose.

###