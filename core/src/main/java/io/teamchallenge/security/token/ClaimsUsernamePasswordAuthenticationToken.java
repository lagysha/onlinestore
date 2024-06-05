package io.teamchallenge.security.token;

import io.jsonwebtoken.Claims;
import java.util.Collection;
import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 * @author Denys Liubchenko
 */
@Getter
public class ClaimsUsernamePasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {
    private final Claims claims;

    /**
     * Constructs a new {@code ClaimsUsernamePasswordAuthenticationToken} with the specified principal,
     * credentials, authorities, and claims.
     *
     * @param principal   the principal, typically the username or user details (must not be {@code null})
     * @param credentials the credentials, typically the password (must not be {@code null})
     * @param authorities the collection of granted authorities for the principal (must not be {@code null})
     * @param claims      additional claims related to the authentication (must not be {@code null})
     */
    public ClaimsUsernamePasswordAuthenticationToken(Object principal, Object credentials,
                                                     Collection<? extends GrantedAuthority> authorities,
                                                     Claims claims) {
        super(principal, credentials, authorities);
        this.claims = claims;
    }
}
