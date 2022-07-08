
|        Http Engine         |                          Resteasy                           | Jersey |
|:--------------------------:|:-----------------------------------------------------------:|:------:|
|     Apache HttpClient      | [+](apache_httpclient/apache_httpclient_resteasy/README.md) |   ?    |
|   Apache HttpAsyncClient   |                              +                              |   ?    |
|       Eclipse Jetty        |                              +                              |   ?    |
|       Reactor Netty        |                              +                              |   ?    |
|       Eclipse Vert.x       |                              +                              |   ?    |
| java.net.HttpURLConnection |                              +                              |   ?    |


|       Http client feature        | Apache HttpClient |
|:--------------------------------:|:-----------------:|
|         Connection pool          |         +         |
| Connection timeout configuration |         +         |
|       Connection Eviction        |         +         |
|    Keep-Alive header support     |         +         |
|      Connection persistence      |         +         |


### Connection pool and Connection persistence

- allows to handle multithreaded connections
- allows Connection Persistence - re-use of the connections if they have not been closed.
  Once a connection is released by the manager it stays open for re-use.

### Configuring Timeouts

Could be configured:
- connection timeout
- connection request timeout
- socket timeout

### Connection Keep-Alive Strategy

By default, Keep-Alive header is handled by client library.

If the Keep-Alive header is not present in the response, then depending on implementation handling might differ.
For instance Apache HttpClient assumes the connection can be kept alive indefinitely
which might cause to performance issues.

You could control it and set a default value, in case the header is absent.

### Connection Eviction

Used to detect idle and expired connections and close them.