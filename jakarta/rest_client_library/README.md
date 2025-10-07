
### Steps to create a library

### Rest API definition

### Rest autowired/injection service 

    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-context</artifactId>
    </dependency>

@Service
public class RestApi1Service

### to use:

package must be scanned
`@ComponentScan("com.savdev.rest.client.lib2")`
`RestClientConfiguration` - produced


Lib2RestClientConfiguration vs Qualifier

### only in rest api module, but not in the library

    <dependency>
      <groupId>org.junit.jupiter</groupId>
      <artifactId>junit-jupiter-engine</artifactId>
      <version>${junit5.version}</version>
    </dependency>

in spring lib and spring app use:
```xml
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-test</artifactId>
      <scope>test</scope>
    </dependency>
```

