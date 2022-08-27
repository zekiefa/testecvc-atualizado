package br.com.cvc.evaluation.service;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TokenService {
    private final String expiration;
    private final String secret;
    private final String issuer;

    public TokenService(@Value("${jwt.expiration}") final String expiration,
                    @Value("${jwt.secret}") final String secret,
                    @Value("${spring.application.name}") final String issuer) {
        this.expiration = expiration;
        this.secret = secret;
        this.issuer = issuer;
    }

    /**
     * Extracting username from the token
     * @param token of authentication
     * @return the username
     */
    public String extractUsername(final String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private Date extractExpiration(final String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(final String token, final Function<Claims, T> claimsResolver) {
        final var claims = extractAllClaims(token);

        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(final String token) {
        return Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(final String token) {
        return extractExpiration(token).before(new Date());
    }

    private String generateToken(final String username) {
        final var claims = new HashMap<String, Object>();
        return createToken(claims, username);
    }

    /**
     * Generating token
     * @param authentication for generate token
     * @return the new token
     */
    public String generateToken(final Authentication authentication) {
        final var user = (UserDetails) authentication.getPrincipal();

        return this.generateToken(user.getUsername());
    }


    private String createToken(final Map<String, Object> claims, final String subject) {
        log.info("Creating token...");
        final var now = Instant.ofEpochMilli(System.currentTimeMillis());
        final var expirationTime = Date.from(now.plusMillis(Long.parseLong(this.expiration)));

        return Jwts.builder().setClaims(claims)
                        .setSubject(subject)
                        .setIssuer(this.issuer)
                        .setIssuedAt(Date.from(now))
                        .setExpiration(expirationTime)
                        .signWith(SignatureAlgorithm.HS256, this.secret).compact();
    }

    /**
     * Validating the token
     * @param token of the authentication
     * @param userDetails - details of the user
     * @return return true if the @{@link UserDetails} is equal to token user
     */
    public Boolean validateToken(final String token, final UserDetails userDetails) {
        log.info("Validating token...");
        final var username = extractUsername(token);

        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Validate the token
     * @param token to be validated
     * @return true if token is valid
     */
    public boolean isTokenValid(final String token) {
        try {
            Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
            log.info("Token validated");
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Token invalid: {}", e.getMessage());
            return false;
        }
   }


}
