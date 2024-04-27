package io.teamchallenge.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.teamchallenge.entity.User;
import io.teamchallenge.enumerated.Role;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service class for handling JWT (JSON Web Token) operations.
 */
@Service
public class JwtService {
    @Getter
    private final String secretKey;
    private final int accessTokenValidTimeInMinutes;
    private final int refreshTokenValidTimeInMinutes;

    /**
     * Constructor for JwtService.
     *
     * @param secretKey                      Secret key used for signing JWTs.
     * @param accessTokenValidTimeInMinutes  Valid time for access tokens in minutes.
     * @param refreshTokenValidTimeInMinutes Valid time for refresh tokens in minutes.
     */
    @Autowired
    public JwtService(@Value("${JWT_TOKEN_KEY}") String secretKey,
                      @Value("${accessTokenValidTimeMin}") int accessTokenValidTimeInMinutes,
                      @Value("${refreshTokenValidTimeMin}") int refreshTokenValidTimeInMinutes) {
        this.secretKey = secretKey;
        this.accessTokenValidTimeInMinutes = accessTokenValidTimeInMinutes;
        this.refreshTokenValidTimeInMinutes = refreshTokenValidTimeInMinutes;
    }

    /**
     * Retrieves the JWT token from the HTTP request.
     *
     * @param request The HTTP request.
     * @return An optional string containing the JWT token if found in the request.
     */
    public Optional<String> getTokenFromRequest(HttpServletRequest request) {
        return Optional
            .ofNullable(request.getHeader("Authorization"))
            .filter(authHeader -> authHeader.startsWith("Bearer "))
            .map(token -> token.substring(7));
    }

    /**
     * Generates an access token.
     *
     * @param id    The user ID.
     * @param email The user email.
     * @param role  The user role.
     * @return The generated access token.
     */
    public String generateAccessToken(Long id, String email, Role role) {
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.MINUTE, accessTokenValidTimeInMinutes);
        return Jwts.builder()
            .issuer("GadgetHouse")
            .subject(email)
            .claim("id", id)
            .claim("role", role)
            .issuedAt(now)
            .expiration(calendar.getTime())
            .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)), Jwts.SIG.HS256)
            .compact();
    }

    /**
     * Generates a refresh token.
     *
     * @param user The user for whom the refresh token is generated.
     * @return The generated refresh token.
     */
    public String generateRefreshToken(User user) {
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.MINUTE, refreshTokenValidTimeInMinutes);
        return Jwts.builder()
            .issuer("GadgetHouse")
            .subject(user.getEmail())
            .claim("id", user.getId())
            .claim("role", user.getRole())
            .issuedAt(now)
            .expiration(calendar.getTime())
            .signWith(Keys.hmacShaKeyFor(user.getRefreshTokenKey().getBytes(StandardCharsets.UTF_8)), Jwts.SIG.HS256)
            .compact();
    }

    /**
     * Generates a unique token key.
     *
     * @return The generated token key.
     */
    public String generateTokenKey() {
        return UUID.randomUUID().toString();
    }
}