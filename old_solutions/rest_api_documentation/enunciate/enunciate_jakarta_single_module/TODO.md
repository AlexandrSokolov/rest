http://localhost:8080/docs/index.html
http://localhost:8080/rest/api

$ mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Dserver.servlet.context-path=/custom/context/path"

or in docker composee:
```yaml
    environment:
      - JAVA_OPTS=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8787
      - JAVA_TOOL_OPTIONS=
          -Dserver.servlet.context-path=/cs/att/estimation
          -Dsome.other.argument=true
```

2023-11-21T19:00:16.893+01:00  INFO 800339 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path '/custom/context/path'

deploy to container, make sure context is not set