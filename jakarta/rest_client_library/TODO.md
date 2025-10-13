
#### Rest client library responsibilities
- provide functionality, injectable via interface
- extend rest api with hiding rest-specific complexity, to make it more easily for its consumers
- expose clear requirements for the rest library configuration
- it might also implement certain caching strategies.
  In certain cases only the library consumer can decide what to cache and when.

### testing with wiremock and Spring Boot

https://wiremock.org/docs/spring-boot/