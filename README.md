### Rest Projects Container

Container for rest projects

#### Build the project:

To build and apply all code quality checks, run:  `mvn clean install`

To speed build up without code quality checks, run: `mvn clean package`

To skip tests, run Maven with: `-DskipTests=true` parameter. For instance:

`mvn clean package -DskipTests=true`

#### Increment version:

`mvn versions:set -DnewVersion=1.0.1 -DoldVersion=* -DgroupId=* -DartifactId=* -DgenerateBackupPoms=false`

Note: it is not enough to run it as: `mvn versions:set -DnewVersion=1.0.1 -DgenerateBackupPoms=false`

In that case dependency in `deps/pom.xml` will not be changed.

#### Dev issues:

Logging:
Add mvn dependency to the module:
```xml
    <dependency>
      <groupId>org.apache.logging.log4j</groupId>
      <artifactId>log4j-api</artifactId>
    </dependency>
```
Get logger:
```java
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
...
private static final Logger logger = LogManager.getLogger(SomeClass.class.getName());
```