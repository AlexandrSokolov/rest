
### Steps to create a library

- [todo - there is no app class]
- [library `pom.xml`](#library-pomxml)
- library api
- library implementation
- library requirements, how to meet in tests
- both unit with context loading, but against wiremock and integration tests
- authentiction is part of tests, but is not part of the library, only the consumer of the library can decide
  how to pass auth attributes  in proerty file, via system env, etc.
- logs from rest engine
- logs from commons rest client
- what must be documented for library consumer - rest api (if injectable), extended non-rest api, 
  package for scanning, package for logging, rest config

### Library `pom.xml`

#### Jakarta Rest API to define rest interfaces

```xml
    <dependency>
      <groupId>jakarta.ws.rs</groupId>
      <artifactId>jakarta.ws.rs-api</artifactId>
    </dependency>
```
Note: this dependency is inherited via a dependency on the rest client

#### Rest client dependency
```xml
    <dependency>
      <groupId>com.savdev.rest</groupId>
      <artifactId>common-rest-client</artifactId>
      <version>1.0.0</version>
    </dependency>
```

You might also need dependency on the library, 
that provides `jakarta.ws.rs.client.ClientRequestFilter` for authentication, if such exists.

#### Spring Context

```xml

    <dependencyManagement>
      <dependencies>
        <dependency>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-dependencies</artifactId>
          <version>${spring-boot.version}</version>
          <type>pom</type>
          <scope>import</scope>
        </dependency>
      </dependencies>
    </dependencyManagement>
    <dependencies>
      <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-context</artifactId>
      </dependency>
    </dependencies>
```
Rest client library provides injectable in Spring Context services.

#### Test dependency:
```xml
  <dependencies>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-test</artifactId>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>org.wiremock.integrations</groupId>
      <artifactId>wiremock-spring-boot</artifactId>
      <version>${wiremock-spring-boot.version}</version>
      <scope>test</scope>
    </dependency>
  </dependencies>
```

#### Configure custom repositories and distribution management

```xml
  <repositories>
    <repository>
      <id>custom-releases-repo-id</id>
      <url>https://nexus.custom.com/content/repositories/releases</url>
    </repository>
    <repository>
      <id>custom-snapshots-repo-id</id>
      <url>https://nexus.custom.com/content/repositories/snapshots</url>
    </repository>
  </repositories>

  <distributionManagement>
    <repository>
      <id>custom-releases-repo-id</id>
      <uniqueVersion>false</uniqueVersion>
      <url>https://nexus.custom.com/content/repositories/releases</url>
    </repository>
    <repository>
      <id>custom-snapshots-repo-id</id>
      <uniqueVersion>true</uniqueVersion>
      <url>https://nexus.custom.com/content/repositories/snapshots</url>
    </repository>
  </distributionManagement>
```

### Rest API definition

#### Create REST Api interfaces:
```java
@Path(BASE_URL)
@Produces(MediaType.APPLICATION_JSON)
public interface RestApi1 {

  String BASE_URL = "/api1";

  @GET
  String getStrValue();
}
```

todo link to such interface

#### DTO objects - as Java records

todo example

#### DTO as `lombok.Data` classes

todo example

### Rest autowired/injection service

Notes:
- it's not necessary to implement rest service by this service.
  You might want to hide rest-specific API, for instance iteration over elements via pagination, 
  providing a non-rest method that returns stream of elements instead.
- to implement such service you might extend the base `BaseRestClientService` class, provided by rest clients

todo rest service that implements rest api interface

todo rest service that does not implement rest api and hides rest-specific details


@Service
public class RestApi1Service

### to use:

package must be scanned
`@ComponentScan("com.savdev.rest.client.lib2")`
`RestClientConfiguration` - produced


Lib2RestClientConfiguration vs Qualifier
