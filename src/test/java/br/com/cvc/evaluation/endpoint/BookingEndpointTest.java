package br.com.cvc.evaluation.endpoint;

import static io.restassured.RestAssured.given;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import br.com.cvc.evaluation.config.WebClientConfig;
import br.com.cvc.evaluation.config.WireMockConfig;
import br.com.cvc.evaluation.domain.Hotel;
import br.com.cvc.evaluation.fixtures.TokenBuilder;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;

@SpringBootTest(
                webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
                classes = {WireMockConfig.class, WebClientConfig.class})
@WithMockUser(username = "usuario", authorities = {"admin", "user"})
public class BookingEndpointTest {
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    private final TokenBuilder tokenBuilder = new TokenBuilder();
    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = this.port;
    }

    @Test
    void testFind() {
        // Asserts
        final var cityCode = 55;
        final var checkin = LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern(DATE_FORMAT));
        final var checkout = LocalDate.now().plusDays(5).format(DateTimeFormatter.ofPattern(DATE_FORMAT));

        // Act | Asserts
        given()
                        .auth()
                        .oauth2(tokenBuilder.createJWT("usuario", Long.parseLong("0")))
                        .when().get(String.format("/booking/%d/%s/%s/1/1", cityCode, checkin, checkout))
                        .then()
                        .assertThat()
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .as(Hotel[].class);
    }
}
