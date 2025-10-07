FAQs:
- [Rest client FAQ](Rest.Clients.FAQ.md)
- [TODO Rest Server FAQ](todo)

Official rest clients documentation
- [RESTEasy Proxy Framework](https://docs.resteasy.dev/6.2/userguide/#_client_proxies)
- [Spring HTTP Interface](https://docs.spring.io/spring-framework/reference/integration/rest-clients.html#rest-http-interface)

Rest client examples:
- [Commons rest client](#commons-rest-client-examples)
- [Rest API](#rest-api-examples)
- [Rest Client Library](#rest-client-library-examples)
- [Rest Client consumer](#rest-client-consumer-application-example)

### Commons rest client examples
- [Full version of the rest client with all the tests and features described](jakarta/jakarta_resteasy_client/README.md)

  This project covers all the useful features and can be used to create an independent rest client library.
- [Rest client for Spring Boot](spring/multiple_rest_clients/rest_client/README.md)

  This is a simplified version of the rest client, defined in its own module as a shared library.
  It highlights how the full application that uses rest clients could look like.
- [Rest client for Spring Boot defined as part of a single app](spring/multiple_rest_clients_no_libs_app/src/main/java/com/savdev/rest/sb/app/rest/BaseRestClientService.java)

  This demo could be used in existing projects for bugs fixing only.
  Us it if you need the solution as soon as possible and
  switching to the library-based solution is considered as a time-consuming approach.


### Rest API examples
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

### Rest Client Library examples
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

### Rest client consumer application example:
- [App that uses rest client libraries](spring/multiple_rest_clients/rest_libs_consumer_app)
- [App that doesn't use rest client libraries](spring/multiple_rest_clients_no_libs_app/README.md)

  This demo could be used in existing projects for bugs fixing only.
  Use this option if you need the solution as soon as possible and
  switching to the library-based solution is considered as a time-consuming approach.