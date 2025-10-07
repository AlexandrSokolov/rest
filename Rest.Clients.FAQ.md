- [Rest client useful features](jakarta/jakarta_resteasy_client/README.md#commons-rest-client-useful-features)
- [Architectural approaches to create rest clients](#architectural-approaches-to-create-rest-clients)
- [Roles in rest-client based applications implemented via rest interfaces](#roles-in-rest-client-based-applications-implemented-via-rest-interfaces)
- [Commons rest client module responsibilities](#commons-rest-client-module-responsibilities)
- [Commons rest client features](#commons-rest-client-features)
- [Rest API Module responsibilities](#rest-api-module-responsibilities)
- [Dependencies in Rest API module](#dependencies-in-rest-api-module)
- [Who is responsible for rest API module creation?](#who-is-responsible-for-rest-api-module-creation)
- [Rest API technology choice](#rest-api-technology-choice)
- [Using Spring Web annotations for REST API definition](#using-spring-web-annotations-for-rest-api-definition)
- [Rest client library responsibilities](#rest-client-library-responsibilities)
- [How rest clients can be used in the apps?](#rest-client-consumer-application)


### Architectural approaches to create rest clients

- [HTTP client that offers a fluent API](https://docs.spring.io/spring-framework/reference/integration/rest-clients.html#rest-restclient)
- [HTTP client based on templates](https://docs.spring.io/spring-framework/reference/integration/rest-clients.html#rest-resttemplate)
- [Reactive HTTP client](https://docs.spring.io/spring-framework/reference/web/webflux-webclient.html)
- [Declarative REST API Definition](https://docs.spring.io/spring-framework/reference/integration/rest-clients.html#rest-http-interface)


### Declarative REST API Definition

Note: declarative REST API definition can be used with any other type of the architectural rest client approaches.
See also [HTTP Interface](https://docs.spring.io/spring-framework/reference/integration/rest-clients.html#rest-http-interface)


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
- you decouple REST specific attributes from the domain logic.
- Speeds up development of rest clients - rest interfaces defined in a rest api module are shared among
  other rest customers. Customers add dependency on that rest api module without needing deep knowledge of
  HTTP specifics like headers, status codes. Developers can focus on the business logic of interacting with
  the remote service.
- Speeds up development of rest backend services - to implement the service developer also implements the rest
  interfaces, defined in the rest api module. You focus on implementation, rather than HTTP/REST specific details.


### Roles in rest-client based applications implemented via rest interfaces

- [Commons rest client module](#commons-rest-client-features)
- [Rest API module](#rest-api-module)
- [Rest client library](#rest-client-library)
- [Rest client consumer application](#rest-client-consumer-application)

### Commons rest client module responsibilities

Rest clients simplified its creation in the application. 
When it is used based on REST API definition, you cannot share a single instance 
and must create rest client per rest interface.


#### Commons rest client features

- [Simplifies rest client creation (how to pass only server url and authentication)]
- [Rest client destruction]
- [Authentication utilities]
- [Fine-tuning logging]
- [Errors handling]

#### Commons rest client examples
- [Full version of the rest client with all the tests and features described](jakarta/jakarta_resteasy_client/README.md)

  This project covers all the useful features and can be used to create an independent rest client library.
- [Rest client for Spring Boot](spring/multiple_rest_clients/rest_client/README.md)

  This is a simplified version of the commons rest client, defined in its own module as a shared library.
  It highlights how the full application that uses rest clients could look like.
- [Rest client for Spring Boot defined as part of a single app](spring/multiple_rest_clients_no_libs_app/src/main/java/com/savdev/rest/sb/app/rest/BaseRestClientService.java)

  This demo could be used in existing projects for bugs fixing only.
  Use this option if you need the solution as soon as possible and 
  switching to the library-based solution is considered as a time-consuming approach.

### Rest API module

#### Rest API module responsibilities:
- definition of rest-specific details via REST interfaces
- DTO objects, used as method parameters or return types
- data format - date, time, money (BigDecimal), boolean
- requests serialization and responses deserialization rules (via `ObjectMapper` providing)
- rest documentation - as close as possible to the rest methods, see [`enunciate`](https://enunciate.webcohesion.com/index.html)
- `OpenAPI` docs - to help those customers, who use rest clients generation

#### Rest API examples
- [Rest API module](jakarta/jakarta_rest_api/README.md)

  Project example that covers all the useful features for Rest API module. 
- Rest API definition as part of the rest client library for Spring Boot

  In these modules, rest API is part of the rest client libraries.
  Use this approach, when you have no rest API module defined.
  See:
    - [Rest Client Library #1](spring/multiple_rest_clients/rest_client_lib_1/README.md)
    - [Rest Client Library #2](spring/multiple_rest_clients/rest_client_lib_2/README.md)
- Rest API for Spring Boot defined as part of a single app

  These demos could be used in existing projects for bugs fixing only.
  Use this option if you need the solution as soon as possible and
  switching to the library-based solution is considered as a time-consuming approach.
  See:
    - [`RestApi1`](spring/multiple_rest_clients_no_libs_app/src/main/java/com/savdev/rest/sb/app/rest/client1/RestApi1.java)
    - [`RestApi2`](spring/multiple_rest_clients_no_libs_app/src/main/java/com/savdev/rest/sb/app/rest/client2/RestApi2.java)


#### Dependencies in Rest API module

Any API module must include as few dependencies as possible.

For a rest api these are dependencies on
- rest api itself, it could be: Jakarta API (`jakarta.ws.rs:jakarta.ws.rs-api`), Jax RS API
- jackson `com.fasterxml.jackson.core:jackson-databind` - to provide a custom `ObjectMapper`
- additional dependencies, like `com.fasterxml.jackson.datatype:jackson-datatype-jsr310`,
  that allow to customise `ObjectMapper` with specific features

#### Who is responsible for rest API module creation?

REST API is tightly coupled to a technology.
Rest consumers (clients) can choose any technology.
Rest service creators cannot guess and should not affect that choice.
As a result it might be a motivation to create those API by rest services consumers only.

On the other side, if rest API is used only for communication within a complicated system between internal components
and those components get created using the same technology stack, it could be a good reason 
to create rest API module by rest service creators and share it among all the clients.

Additional reasons to define such modules by rest service creators:
- It simplifies server-side implementation, cause your rest services also can implement the same rest interfaces
  that rest clients use. 
  As a result you define rest-related details and share them between the server and the clients in a very clear way.
- You could create documentation and locate it on the API method, 
  making the docs and rest API definition as close to each other as possible.
- You could generate `OpenAPI` docs from the rest interfaces and method comments that document the logic.
  It allows any rest client that uses client generation based on `OpenAPI` use your REST API module indirectly.

#### Rest API technology choice

Currently, you could define rest API with:
- [Spring Web annotations](https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-requestmapping.html)
- [Jakarta REST API (former `JAX-RS`)](https://jakarta.ee/learn/docs/jakartaee-tutorial/current/websvcs/rest/rest.html)
- `JAX-RS` API

#### Using Spring Web annotations for REST API definition

Spring defines its Web annotations like `@HttpExchange`in:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

As a result you cannot in an efficient way extract only Java interfaces into a light shared API module,
that does not contain other Spring implementations.
Having `spring-boot-starter-web` as part of API cannot be considered as efficient solution.

As a result:
- You can use Spring Web annotations to define interfaces only for the same application without the benefits
  from sharing api among its consumers, but you still get all the benefits of declarative REST API definition.
- If you want to share rest api with other customers, you need to use Spring application with other solutions,
  like [RESTEasy Proxy Framework](https://docs.resteasy.dev/7.0/userguide/#_client_proxies).


### Rest client library

#### Rest client library responsibilities
- provide functionality, injectable via interface
- extend rest api with hiding rest-specific complexity, to make it more easily for its consumers 
- expose clear requirements for the rest library configuration
- it might also implement certain caching strategies. 
  In certain cases only the library consumer can decide what to cache and when.

#### Rest Client Library examples
- [Rest client library](jakarta/rest_client_library/README.md)

  This project contains all the useful features, covered with the tests.
  This project only depends on REST API module, but does not contain it as part of the library.
- Rest client library for Sprig Boot

  This is a simplified version of rest client library. It contains both rest API definition
  and its methods invocation by the rest client.

  See:
    - [Rest client library #1](spring/multiple_rest_clients/rest_client_lib_1/README.md)
    - [Rest client library #1](spring/multiple_rest_clients/rest_client_lib_2/README.md)

- Rest client functionality defined without libraries as part of a single app
  
  This demo could be used in existing projects for bugs fixing only.
  Use this option if you need the solution as soon as possible and
  switching to the library-based solution is considered as a time-consuming approach.

  See:
  - [`RestApi1Service`](spring/multiple_rest_clients_no_libs_app/src/main/java/com/savdev/rest/sb/app/rest/client1/RestApi1Service.java)
  - [`RestApi2Service`](spring/multiple_rest_clients_no_libs_app/src/main/java/com/savdev/rest/sb/app/rest/client2/RestApi2Service.java)

### Rest client consumer application

The application ideally is completely decoupled from the rest-specific details.
Such applications:
- add dependency on the rest client library
- meets the rest client requirements (configure server url and authentication)
- inject interfaces and use them as plain java interfaces

#### Rest client consumer application example:
- [App that uses rest client libraries](spring/multiple_rest_clients/rest_libs_consumer_app)
- [App that doesn't use rest client libraries](spring/multiple_rest_clients_no_libs_app/README.md)

  This demo could be used in existing projects for bugs fixing only.
  Use this option if you need the solution as soon as possible and
  switching to the library-based solution is considered as a time-consuming approach.


