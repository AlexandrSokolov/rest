
- [Declarative REST API Definition](#declarative-rest-api-definition)
- [Extracting rest api module, purposes](#extracting-rest-api-module-purposes)
- [Extracting rest api module in Spring applications](#extracting-rest-api-module-in-spring-applications)
- [What you should consider when define a rest api module? What should it include?](#rest-api-module-definition-issues)
- [Dependencies in rest api module](#dependencies-in-rest-api-module)
- [Requests logging](#requests-logging)

Documentation:
- [RESTEasy Proxy Framework](https://docs.resteasy.dev/6.2/userguide/#_client_proxies)
- [Spring HTTP Interface](https://docs.spring.io/spring-framework/reference/integration/rest-clients.html#rest-http-interface)

### Declarative REST API Definition

In essence, using a REST proxy interface provides a more object-oriented and developer-friendly way to
interact with RESTful services, promoting clean code and reducing development effort.

Benefits of declarative API definition:
- The client code more readable and maintainable - Instead of manually constructing HTTP requests
  (setting headers, body, method, URL), a Java interface defines the REST API using annotations.
  The framework handles the underlying HTTP communication, including serialization/deserialization of data
  (e.g., JSON to Java objects and vice-versa), error handling, and connection management.
  **This significantly reduces the amount of boilerplate code** and as a result makes
  the client code more readable and maintainable.
- Type Safety - The proxy generates client implementations based on the interface, providing compile-time type checking
  for method parameters and return types. This reduces runtime errors related to incorrect data types or
  missing parameters.

### Extracting rest api module, purposes

Extract rest interfaces into a rest api module. 
This module can be shared (added to the dependency list) by both server and client sides. 
Developers of the rest services on the backend implement them by inheriting the rest interfaces.
Clients of rest services can include dependency on the rest api module and focus on the business logic.   

- you decouple REST specific attributes from the implementation. 
- Speeds up development of rest clients - rest interfaces defined in a rest api module are shared among 
  other rest customers. Customers add dependency on that rest api module without needing deep knowledge of 
  HTTP specifics like headers, status codes. Developers can focus on the business logic of interacting with 
  the remote service.
- Speeds up development of rest backend services - to implement the service developer also implements the rest 
  interfaces, defined in the rest api module. You focus on implementation, rather than HTTP/REST specific details.

### Extracting rest api module in Spring applications

Spring defines `@HttpExchange` and related annotations used to create HTTP interface in:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

As a result you cannot extract Java interfaces into a light api module 
that does not contain other Spring implementations in an efficient way. 
Having `spring-boot-starter-web` as part of API cannot be considered as efficient solution.

As a result:
- You can use Spring Web annotations to define interfaces only for the same application without the benefits 
  from sharing api with the customers, but you still get all the benefits of declarative REST API definition.
- If you want to share rest api with other customers, you need to use Spring application with other solutions, 
  like [RESTEasy Proxy Framework](https://docs.resteasy.dev/7.0/userguide/#_client_proxies).

### Rest api module definition issues

Define:
- rest specific attributes like http method, path, method parameters and return types via Java interface
- dto objects, used as method parameters or return types. 
- a customized `ObjectMapper` that controls serialization/deserialization features.
- custom serializers, responsible for converting object into json string values, when requests are sent
- custom deserializers, responsible for converting json string values into  object, when requests are received

### Dependencies in rest api module

Any API module must include as few dependencies as possible. 

For a rest api these are dependencies on
- rest api itself, it could be: Jakarta API (`jakarta.ws.rs:jakarta.ws.rs-api`), Jax RS API
- jackson `com.fasterxml.jackson.core:jackson-databind` - to provide a custom `ObjectMapper` 
- additional dependencies, like `com.fasterxml.jackson.datatype:jackson-datatype-jsr310`, 
  that allow to customise `ObjectMapper` with specific features

### Requests logging

It depends on http engine, used by rest client framework.

Most popular http engines:
- Apache HttpClient v.4x
- Apache HttpClient v.5.x
- Jetty RS client
- Reactor Netty 
- JDK HttpClient

If you use:
- Java standalone application (not based on Spring)
- `Apache HttpClient` as http engine
- `log4j` as api - to log your application
- `slf4j`/`logback` pair as log implementation:
and want to enable request logging then:
- add in the maven dependencies of the parent module:
  ```xml
  <dependencyManagement>
    <dependencies>
      <!-- LOGGING DEPENDENCIES START:-->
      <!--  use log4j api in the app: -->
      <!-- https://mvnrepository.com/artifact/org.apache.logging.log4j/log4j-api -->
      <dependency>
        <groupId>org.apache.logging.log4j</groupId>
        <artifactId>log4j-api</artifactId>
        <version>${log4j-api.version}</version>
      </dependency>
      <!--  to align slf4j api version and fix the warnings in the tests:
      SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
      SLF4J: Defaulting to no-operation (NOP) logger implementation
      SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
      Note: not needed to add explicitly in the child modules
      -->
      <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-api</artifactId>
        <version>${slf4j-api.version}</version>
        <scope>test</scope>
      </dependency>
      <!--  to redirect log4j calls to slf4j implementation: -->
      <!-- https://mvnrepository.com/artifact/org.apache.logging.log4j/log4j-to-slf4j -->
      <dependency>
        <groupId>org.apache.logging.log4j</groupId>
        <artifactId>log4j-to-slf4j</artifactId>
        <version>${log4j-to-slf4j.version}</version>
        <scope>test</scope>
      </dependency>
      <!--    to redirect commons-logging to slf4j implementation-->
      <!-- https://mvnrepository.com/artifact/org.slf4j/jcl-over-slf4j -->
      <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>jcl-over-slf4j</artifactId>
        <version>${jcl-over-slf4j.version}</version>
      </dependency>
      <!-- logback implementation of slf4j: -->
      <dependency>
        <groupId>ch.qos.logback</groupId>
        <artifactId>logback-core</artifactId>
        <version>${logback.version}</version>
        <scope>test</scope>
      </dependency>
      <!-- https://mvnrepository.com/artifact/ch.qos.logback/logback-classic -->
      <dependency>
        <groupId>ch.qos.logback</groupId>
        <artifactId>logback-classic</artifactId>
        <version>${logback.version}</version>
        <scope>test</scope>
      </dependency>
      <!-- LOGGING DEPENDENCIES END-->
    </dependencies>
  </dependencyManagement>
  ```
- maven dependencies of the child module:
  ```xml
    <dependencies>
      <dependency>
        <groupId>org.apache.logging.log4j</groupId>
        <artifactId>log4j-api</artifactId>
      </dependency>
      <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-api</artifactId>
      </dependency>
      <dependency>
        <groupId>org.apache.logging.log4j</groupId>
        <artifactId>log4j-to-slf4j</artifactId>
      </dependency>
      <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>jcl-over-slf4j</artifactId>
      </dependency>
      <dependency>
        <groupId>ch.qos.logback</groupId>
        <artifactId>logback-core</artifactId>
      </dependency>
      <dependency>
        <groupId>ch.qos.logback</groupId>
        <artifactId>logback-classic</artifactId>
      </dependency>
    </dependencies>
  ```
- exclude `commons-logging` from `resteasy-client`:
  ```xml
      <dependency>
        <groupId>org.jboss.resteasy</groupId>
        <artifactId>resteasy-client</artifactId>
        <exclusions>
          <exclusion>
            <artifactId>commons-logging</artifactId>
            <groupId>commons-logging</groupId>
          </exclusion>
        </exclusions>
      </dependency>
  ```
- [enable debug level for `org.apache.http.wire` in case you use `Apache HttpClient v.4.x`](jakarta/jakarta_resteasy_client/src/test/resources/logback-test.xml)
- [enable debug level for `org.apache.hc.client5.http.wire` in case you use `Apache HttpClient v.5.x`](jakarta/jakarta_resteasy_client/src/test/resources/logback-test.xml)

If you use Spring, it manages all dependencies for you. Just enable debug level in its properties:
```yaml
logging:
  level:
    org:
      apache:
        http:
          wire: debug
```