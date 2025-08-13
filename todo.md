### jakarta, rest api, file uploading

see also 

### jakarta, rest api, file downloading

### jakarta - managed in spring boot env-nt simplified example

### jakarta - wrapped for better logging client library

### jakarta - rest client library with spirng boot env-nt 

###  jakarta, rest api

docs generation

### dto with jackson annotations:

list of annoations:
field name
not required
serieliezr

and standard objectMapper - not the customized one!

### jax rs - based on jakararta examples

### remove old_solutions

### rest client with invalid rest interface

### testing client apps

OkHttp MockWebServer or WireMock

https://docs.spring.io/spring-framework/reference/testing/spring-mvc-test-client.html

### Spring @GetMapping vs @GetExchange

https://stackoverflow.com/questions/78476824/whats-the-difference-between-getmapping-and-getexchange

### go through resteasy multipart solutioins:

[Chapter 26. Multipart Providers](https://docs.jboss.org/resteasy/docs/3.13.2.Final/userguide/html/Multipart.html)
https://stackoverflow.com/questions/2637017/how-do-i-do-a-multipart-form-file-upload-with-jax-rs

### `multipart/form-data` file uploading with custom 

Google search by: `jakarta messagebodyreader multipart/form-data example`, solution from AI Overview

```java
@Provider
@Consumes(MediaType.MULTIPART_FORM_DATA)
public class MultiPartMessageBodyWriter implements MessageBodyWriter<MultiPartMessage> {}
```

- [JAX/RS Multipart Client](https://guntherrotsch.github.io/blog_2021/jaxrs-multipart-client.html)
- [github project](https://github.com/GuntherRotsch/guntherrotsch.github.io/tree/code/jaxrs-multipart/client)
- [api](https://github.com/GuntherRotsch/guntherrotsch.github.io/tree/code/jaxrs-multipart/api/src/main/java/net/gunther/multipart/client)
- [writer](https://github.com/GuntherRotsch/guntherrotsch.github.io/blob/code/jaxrs-multipart/client/src/main/java/net/gunther/multipart/client/MultiPartMessageBodyWriter.java)



compare implementation with [resteasy multipart providers](https://github.com/resteasy/resteasy/tree/main/providers/multipart/src/main/java/org/jboss/resteasy/plugins/providers/multipart)

Provider registration:
1. explicitly on the client: `client.register(MultiPartMessageBodyWriter.class);`
2. as part of the library via `/META-INF/services/jakarta.ws.rs.ext.Providers`, see `resteasy-multipart-provider-6.2.4.Final.jar` 

### multipart build with apache:

Docs, check, solution without resteasy:

package org.apache.http.entity.mime;
...
public class MultipartEntityBuilder


### File Uploads with JAX-RS 2

https://nofluffjuststuff.com/blog/jason_lee1/2014/05/file_uploads_with_jax_rs_2

### server side file uploading with `jakarta.ws.rs.core.EntityPart`

```java

```

### document: custom writer RESTEASY003215: could not find writer for content-type

a JAX-RS implementation, indicates that the framework cannot find a suitable MessageBodyWriter to serialize the 
Java object being sent in an HTTP request into the specified content type.

Missing or Incorrect JAX-RS Provider: 
- JSON/XML: For common data formats like JSON or XML, you need to include the appropriate JAX-RS provider 
  dependencies in your project. 
  For example, for JSON, resteasy-jackson2-provider or resteasy-json-p-provider are commonly used. 
  For XML, resteasy-jaxb-provider is typical. Ensure these dependencies are present and compatible with your 
  RESTEasy version.
- Manual Registration: If automatic registration isn't working, you might need to manually register the 
  MessageBodyWriter with your RESTEasy client or server.

Classpath Conflicts:
- Multiple JAX-RS Implementations: Having multiple JAX-RS implementations (e.g., RESTEasy and Jersey) on the classpath 
  can lead to conflicts where the wrong ClientBuilder or MessageBodyWriter is picked up. 
  Remove any unnecessary JAX-RS implementation dependencies.
- Maven Assembly Plugin Overwriting: If using Maven, ensure the assembly plugin is not overwriting 
  META-INF/services files crucial for JAX-RS provider registration.

Unsupported Content Type or Type:
- Custom Types: If sending a custom Java type that RESTEasy doesn't inherently know how to serialize for a 
  given content type, you must implement and register a custom MessageBodyWriter for that specific type and content type.
- Form Data: For application/x-www-form-urlencoded, ensure you are correctly constructing 
  javax.ws.rs.core.Form objects and that the necessary RESTEasy components for handling form data are present.

Incorrect RESTEasy Client Usage:
- Client Configuration: Verify that your RESTEasy client is correctly configured and that any custom 
  ClientConfig settings are not inadvertently preventing MessageBodyWriter discovery.
- Reactive API Bindings: If using RESTEasy's reactive client API, ensure you have the necessary reactive 
  client API bindings included in your dependencies.

Troubleshooting Steps:
Check Dependencies:
Verify that all required RESTEasy and JAX-RS provider dependencies are correctly added to your project's 
build file (e.g., pom.xml for Maven, build.gradle for Gradle).

Examine Classpath:
Inspect your application's classpath to identify any potential conflicts or missing JARs related to JAX-RS providers.

Debug Provider Discovery:
If possible, enable detailed logging for RESTEasy to see how it attempts to discover and register MessageBodyWriter instances.

Simplify Test Case:
Create a minimal test case that reproduces the error to isolate the problem and simplify debugging.

### go through docs:

[Chapter 51. RESTEasy Client API](https://docs.jboss.org/resteasy/docs/4.6.0.Final/userguide/html/RESTEasy_Client_Framework.html)
[Accessing REST Resources with the Jakarta REST Client API](https://jakarta.ee/learn/docs/jakartaee-tutorial/current/websvcs/rest-client/rest-client.html)
[Jakarta REST 3.1 by Examples](https://itnext.io/jakarta-rest-3-1-by-examples-dbe13fe6988c)