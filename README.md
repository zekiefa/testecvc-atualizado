# Simple Spring Boot Project

### Architecture
- Java 11
- Spring Boot 2.6.4
- [Lombok](https://projectlombok.org) to avoid boilerplate code
- [Mapstruct](https://mapstruct.org) for conversions between domains
- [WebClient](https://www.baeldung.com/spring-5-webclient): a reactive client HTTP
- [SL4J](https://www.slf4j.org/manual.html) for logging
- [io.jsonwebtoken](https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt-api) for JWT authentication
- [JUnit 5](https://junit.org/junit5/docs/current/user-guide) for unit tests
- [Mockito](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [Fixture Factory](https://github.com/six2six/fixture-factory) for generate fake objects
- [Hamcrest](http://hamcrest.org/JavaHamcrest) for alternative asserts
- [EasyRandom](https://github.com/j-easy/easy-random) for generate randomic objects
- [Wiremock](https://wiremock.org/docs/) for asserts with endpoints and http clients

### Instructions
- Clone the project
- Run the project: `$ mvn spring-boot:run`

### About the application
It's a RESTFull API for booking and searching a hotel with JWT authentication.

### Access the application
http://localhost:8080/booking/9626/2019-09-24/2019-09-30/1/0, where:
- 9626: city code
- 2019-09-24: checkin
- 2019-09-30: checkout
- 1: adults
- 0: child

http://localhost:8080/hotel/6, where:
- 6: hotel ID

### References
- [Baeldung](https://www.baeldung.com)

