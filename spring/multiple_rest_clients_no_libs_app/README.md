This demo could be used in existing projects for bugs fixing only.
Use this option if you need the solution as soon as possible and
switching to the library-based solution is considered as a time-consuming approach.

This project shows how to create completely independent rest clients, 
that have different urls and authentication attributes.

To decouple the rest clients more - `@RestClientConfiguration` is not used.
Independent configurations are used:
- [`RestClient1Conf`](src/main/java/com/savdev/rest/sb/app/configs/rest/RestClient1Conf.java)
- [`RestClient2Conf`](src/main/java/com/savdev/rest/sb/app/configs/rest/RestClient2Conf.java)