package br.com.cvc.evaluation.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import br.com.cvc.evaluation.domain.User;
import br.com.cvc.evaluation.fixtures.TokenBuilder;
import br.com.cvc.evaluation.fixtures.UserFixture;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {
    private static final Long TTL_MILLIS = 86400000L;
    private static final String SECRET_KEY = "A+X;fTJP&Pd,TD9dwVq(hsHX,ya^<wsD_UK7L+@=S;{'CydP]{v@}G'b>et;yz$*\\yL5S8EJN:%P:X%H9>#nYLrX}@\\s?CQcpspH,2emzBc!Q[V'AYa~uzF8WR~AUrMzxp/V$9([S9X#zj/CH('#]B_Hc+%fGhe27YB;^j4\\Xk=Ju\"Ap~_&<L;=!Z;!,2UP;!hF3P]j85#*`&T]/kB/W^6$v~u6qpejL>kY^f)sy4:qTq_Ec!-z!@aAp~sLKGU>$";
    private static final String APPLICATION_NAME = "testecvc";
    @Mock
    private Authentication authentication;
    private final TokenService tokenService = new TokenService(TTL_MILLIS.toString(), SECRET_KEY, APPLICATION_NAME);
    private final TokenBuilder tokenBuilder = new TokenBuilder();
    private UserDetails user;
    private String token;

    @BeforeAll
    public static void setUp() {
        FixtureFactoryLoader.loadTemplates("br.com.cvc.evaluation.fixtures");
    }

    @BeforeEach
    void randomUserToken() {
        this.user = Fixture.from(User.class).gimme(UserFixture.VALID);
        this.token = tokenBuilder.createJWT(user.getUsername(), TTL_MILLIS);
    }

    @Test
    void testExtractUsername() {
        final var result = tokenService.extractUsername(this.token);

        assertEquals(this.user.getUsername(), result);
    }

    @Test
    void testGenerateToken() {
        when(this.authentication.getPrincipal()).thenReturn(this.user);

        final var  result = tokenService.generateToken(this.authentication);

        assertNotNull(result);
    }

    @Test
    void testValidateToken() {
        final var result = tokenService.validateToken(this.token, this.user);

        assertTrue(result);
    }

    @Test
    void testIsTokenValid() {
        final var result = tokenService.isTokenValid(this.token);

        assertTrue(result);
    }
}