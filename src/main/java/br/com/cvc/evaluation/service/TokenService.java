package br.com.cvc.evaluation.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    @Value("${jwt.expiration}")
    private String expiration;

    @Value("${jwt.secret}")
    private String secret;

    /**
     * Extracting username from the token
     * @param token of authentication
     * @return the username
     */
    public String extractUsername(final String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracting the expiry of the token
     * @param token authentication
     * @return the expiration time
     */
    public Date extractExpiration(final String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracting the claim based on the condition
     * @param token authentication
     * @param claimsResolver for claims
     * @return the claim resolver
     * @param <T> the generic class
     */
    public <T> T extractClaim(final String token, final Function<Claims, T> claimsResolver) {
        final var claims = extractAllClaims(token);

        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(final String token) {
        return Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(final String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Generating token
     * @param username of the authentication
     * @return the new token
     */
    public String generateToken(final String username) {
        final var claims = new HashMap<String, Object>();
        return createToken(claims, username);
    }

    /**
     * Generating token
     * @param authentication for generate token
     * @return the new token
     */
    public String generateToken(final Authentication authentication) {
        final var user = (User) authentication.getPrincipal();

        return this.generateToken(user.getUsername());
    }


    private String createToken(final Map<String, Object> claims, final String subject) {
        final var now = new Date();
        final var expirationTime = new Date(now.getTime() + Long.parseLong(this.expiration));

        return Jwts.builder().setClaims(claims)
                        .setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
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
        final var username = extractUsername(token);

        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Validate the token
     * @param token to be validate
     * @return true if token is valid
     */
    public boolean isTokenValid(final String token) {
        try {
            Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
   }


}
