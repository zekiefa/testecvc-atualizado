package br.com.cvc.evaluation.endpoint;

import static io.restassured.RestAssured.given;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import br.com.cvc.evaluation.EvaluationApplication;
import br.com.cvc.evaluation.config.WebClientConfig;
import br.com.cvc.evaluation.config.WireMockConfig;
import br.com.cvc.evaluation.domain.Hotel;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = EvaluationApplication.class)
@Import({WireMockConfig.class, WebClientConfig.class})
public class BookingEndpointTest {
    private static final String DATE_FORMAT = "yyyy-MM-dd";

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
                        .when().get(String.format("/booking/%d/%s/%s/1/1", cityCode, checkin, checkout))
                        .then()
                        .assertThat()
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .as(Hotel[].class);
    }
}
