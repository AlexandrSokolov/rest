###

why: "com.savdev.rest.sb.app", in :

@Configuration
@ComponentScan({
"com.savdev.rest.sb.app",
"com.savdev.rest.client.lib1",
"com.savdev.rest.client.lib2"
})

###


TODO describe how you cou configure differently for prod and test envnts

See
[Lib1RestConf](src/main/java/com/savdev/rest/sb/app/configs/Lib1RestConf.java)
vs
[Lib1WireMockConf](src/test/java/com/savdev/rest/sb/app/service/config/Lib1WireMockConf.java)

make it different via:
- only properties control
- via different classes as it is now with:
```java
@Configuration
@Primary
public class Lib1WireMockConf implements Lib1RestClientConfiguration {}
```