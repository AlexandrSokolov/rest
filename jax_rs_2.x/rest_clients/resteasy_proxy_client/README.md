## RESTEasy Proxy Framework

### Features of proxy-based rest client:
- [RESTEasy Proxy Framework Usage](#resteasy-proxy-framework-usage)
- [Closing](todo)
- [Passing path parameter, like `userId` in `GET /users/{userId}`](#passing-path-parameter)
- [Passing query parameter, like `name` in `GET /users?name=alex`](#passing-query-parameter)
- [Passing http cookie](#passing-http-cookie)
- [Passing http header](#passing-header)
- [Passing matrix parameter, like in `GET /categories;name=foo/objects;name=green`](#passing-matrix-parameters)
- [Handling Responses](#responses-handling)

### RESTEasy Proxy Framework Usage

Define rest interface. 

Notes: 
- [A rest interface](src/test/java/com/savdev/rest/client/api/TestRestApi.java) could be defined by rest service creators and shared as rest-api.
- ObjectMapper should also be defined by rest service creators.
- [Resteasy Proxy Client creation](src/main/java/com/savdev/rest/client/ResteasyProxyClient.java)
- [Resteasy Proxy Client usage](src/test/java/com/savdev/rest/client/ResteasyProxyClientTest.java)

### Closing

### Passing path parameter

[Rest Api](src/test/java/com/savdev/rest/client/api/PathParamRestApi.java)

[Unit test](/src/test/java/com/savdev/rest/client/PathParamTest.java)

### Passing query parameter

[Rest Api](/src/test/java/com/savdev/rest/client/api/QueryParamRestApi.java)

[Unit test](/src/test/java/com/savdev/rest/client/QueryParamTest.java)

### Passing http cookie

[Rest Api](src/test/java/com/savdev/rest/client/api/CookieParamRestApi.java)

[Unit test](src/test/java/com/savdev/rest/client/CookieParamTest.java)

### Passing header

[Rest Api](src/test/java/com/savdev/rest/client/api/HeaderParamRestApi.java)

[Unit test](src/test/java/com/savdev/rest/client/HeaderParamTest.java)

### Passing matrix parameters

[Rest Api](src/test/java/com/savdev/rest/client/api/MatrixParamsRestApi.java)

[Unit test](src/test/java/com/savdev/rest/client/MatrixParamsTest.java)

[URL matrix parameters vs. query parameters on stackoverflow](https://stackoverflow.com/questions/2048121/url-matrix-parameters-vs-query-parameters)

### Responses handling