### Client authentications

basic
jwt
web context
2 factors, jwt via web context
oauth

### Serialization, see existing projects

### Generate new rest client via java generators

- as a separate module
- as a package

###  Add BM rest client module with serialiation test, auth filters and other convenient filters

### Server-side

###
Total time to live (TTL) set at construction time defines maximum life span of persistent connections regardless of their expiration setting. No persistent connection will be re-used past its TTL value.

The handling of stale connections was changed in version 4.4. Previously, the code would check every connection by default before re-using it. The code now only checks the connection if the elapsed time since the last use of the connection exceeds the timeout that has been set. The default timeout is set to 2000ms

### Caching

35.2. Client "Browser" Cache
https://docs.jboss.org/resteasy/docs/5.0.3.Final/userguide/html_single/index.html

How can I cache/reuse the resource?
https://stackoverflow.com/questions/54428593/in-java-how-to-check-that-autocloseable-close-has-been-called

This approach may not work if the lifecycle of a resource is longer than action. E.g. it is necessary for the client to keep the resource for a longer time.

Then you might want to dive deep in ReferenceQueue/Cleaner (since Java 9) and related API.
https://docs.oracle.com/javase/8/docs/api/java/lang/ref/ReferenceQueue.html
https://docs.oracle.com/javase/9/docs/api/java/lang/ref/Cleaner.html


Some pools in Java:

HikariCP is a JDBC connection pool library
OkHttps ConnectionPool
Tomcat JDBC Connection pool
ThreadPoolExecutor
When implementing a pool several questions are raised:

When the resource actually should be closed?
How the resource should be shared between multiple threads?

### Client features by:

https://golb.hplar.ch/2019/01/java-11-http-client.html

### Async invocation

Chapter 37. Asynchronous HTTP Request Processing
https://docs.jboss.org/resteasy/docs/5.0.3.Final/userguide/html_single/index.html

```java
ResteasyClient client = (ResteasyClient)ClientBuilder.newClient();
client.asyncInvocationExecutor()

```

[See also example](http://www.mastertheboss.com/jboss-frameworks/resteasy/resteasy-client-api-tutorial/)

### Sending requests in chunked mode
```java
ResteasyClient client = (ResteasyClient)ClientBuilder.newClient();
ResteasyWebTarget target = client.target(url)
  .setChunked(true);
```

### Scheduling requests
```java
ResteasyClient client = (ResteasyClient)ClientBuilder.newClient();
client.getScheduledExecutor();
```

### authentication
see 
as a separete clientrequestfilter, see tools/budgetSnapshoter
see AuthClientRequestFilterFactory in ikea fusion

### http protocol and its versions and features

### TODO books
- [RESTful Java with JAX-RS 2.0, 2nd Edition by Bill Burke, 2013](https://www.oreilly.com/library/view/restful-java-with/9781449361433/)
- [RESTful Web Services by Leonard Richardson and Sam Ruby. A great introduction to REST.](https://www.oreilly.com/library/view/restful-web-services/9780596529260/)
- [RESTful Web Services Cookbook by Subbu Allamaraju and Mike Amundsen](https://www.oreilly.com/library/view/restful-web-services/9780596809140/)