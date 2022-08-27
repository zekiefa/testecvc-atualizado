package br.com.cvc.evaluation.endpoint;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import br.com.cvc.evaluation.EvaluationApplication;
import br.com.cvc.evaluation.config.SpringSecurityTestConfig;
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
@Import({WireMockConfig.class, WebClientConfig.class, SpringSecurityTestConfig.class})
public class HotelEndpointTest {
    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = this.port;
    }

    @Test
     void testFind() {
         final var idHotel = 1;

         final var response = given()
                         .when().get(String.format("/hotels/%d", idHotel))
                         .then()
                         .statusCode(HttpStatus.OK.value())
                         .extract()
                         .as(Hotel.class);

         assertAll("success",
                         () -> assertNotNull(response),
                         () -> assertThat(response.getId(), is(idHotel))
         );
     }
}
