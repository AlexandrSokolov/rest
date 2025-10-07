
This demo illustrates how to:
- create independent rest client modules/libraries, that can be shared by Spring Boot applications
- consume them by the Spring Boot App

### Roles

- [Base Rest Client Library](#base-rest-client-library)
- [Rest client libraries](#rest-client-libraries)
- [Spring Boot application - the rest clients libraries consumer](#rest-client-libraries-consumer)


### Base Rest Client Library

This module contains code, shared by the other rest client libraries. 
Its purpose is to simplify rest client library creation, to avoid duplicated code.

The base rest client library:
- contains code to create proxy-based rest client 
- provides enriched logic to fine-tune logging in rest-based applications
- allows clients to control logging size
- provides utilities to handle exceptions, to extract error messages from the body in case of errors
- provides utilities that help to create authentication client request filters 


### Rest client libraries

Those libraries are responsible for:
- providing rest-api for specific endpoints
- rest requests serialization and responses deserialization through `ObjectMapper`
- provides service client api that hides rest-specific logic and makes it more easily for usage by its consumers


With only a single rest client, it is not clear, how to configure them, 
in case we communicate with different external systems that have 
different server urls and different ways of authentication.

To make it more clear, multiple rest clients are created:
- [Rest client library #1](rest_client_lib_1/README.md)
- [Rest client library #2](rest_client_lib_2/README.md)

### Rest client libraries consumer

This demo application illustrates how to:
- use the rest client libraries 
- pass authentication details in different ways in Spring Boot application
- handle errors in rest communication
- fine-tune logging of rest requests and responses
- control log buffer size (useful in tests), to see the whole body
