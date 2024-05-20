package io.teamchallenge.security.provider;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.teamchallenge.service.JwtService;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final JwtService jwtService;

    /**
     * Constructor for JwtAuthenticationProvider.
     *
     * @param jwtService JwtService instance for JWT operations.
     */
    @Autowired
    public JwtAuthenticationProvider(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    /**
     * Authenticates the given authentication token.
     *
     * @param authentication Authentication object containing JWT token.
     * @return Authentication object representing the authenticated user.
     * @throws AuthenticationException If authentication fails.
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SecretKey secretKey = Keys.hmacShaKeyFor(jwtService.getSecretKey().getBytes(StandardCharsets.UTF_8));
        JwtParser jwtParser = Jwts.parser().verifyWith(secretKey).build();
        String email = jwtParser
            .parseSignedClaims(authentication.getName())
            .getPayload()
            .getSubject();
        String role = (String) jwtParser
            .parseSignedClaims(authentication.getName())
            .getPayload()
            .get("role");
        return new UsernamePasswordAuthenticationToken(email, "",
            Collections.singleton(new SimpleGrantedAuthority(role)));
    }

    /**
     * Checks if this AuthenticationProvider supports the provided authentication token.
     *
     * @param authentication The authentication token to check.
     * @return True if the token is supported, false otherwise.
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }
}