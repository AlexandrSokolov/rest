### Rest client choice

**Preferable rest clients**
- [Resteasy rest client with proxy generation](resteasy_proxy_client/README.md)

Advantages:
- simplifies rest service creation -  you start with rest api definition,
  clear separation between api and implementation;
- simplifies rest service consuming, via rest interface providing (provided by the rest service owners)
  client uses plain java api to work with java objects, without touching http- or rest-specific aspects.

In case you work with simple requests and manually handle `javax.ws.rs.core.Response`
it makes no difference between the following rest clients. You only set the right dependency.
- [Jax Rs Client with Jersey client](jersey_client)
- [Jax Rs Client with Resteasy client](resteasy_client)
- [Jax Rs Client with run-time dependency](jax_rs_2_client)