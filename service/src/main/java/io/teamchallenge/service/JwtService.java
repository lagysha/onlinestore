package io.teamchallenge.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.teamchallenge.entity.User;
import io.teamchallenge.enumerated.Role;
import io.teamchallenge.exception.BadTokenException;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import javax.crypto.SecretKey;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static io.teamchallenge.constant.ExceptionMessage.TOKEN_CAN_NOT_BE_PARSED;
import static io.teamchallenge.constant.ExceptionMessage.TOKEN_HAS_BEEN_EXPIRED;
import static io.teamchallenge.constant.ExceptionMessage.TOKEN_WAS_NOT_SIGNED_BY_USER;

/**
 * Service class for handling JWT (JSON Web Token) operations.
 */
@Service
@Slf4j
public class JwtService {
    @Getter
    private final String secretKey;
    private final ObjectMapper objectMapper;
    private final Integer accessTokenValidTimeInMinutes;
    private final Integer refreshTokenValidTimeInMinutes;

    /**
     * Constructor for JwtService.
     *
     * @param secretKey                      Secret key used for signing JWTs.
     * @param objectMapper                   Injected object mapper.
     * @param accessTokenValidTimeInMinutes  Valid time for access tokens in minutes.
     * @param refreshTokenValidTimeInMinutes Valid time for refresh tokens in minutes.
     */
    @Autowired
    public JwtService(@Value("${JWT_TOKEN_KEY}") String secretKey, ObjectMapper objectMapper,
                      @Value("${accessTokenValidTimeMin}") Integer accessTokenValidTimeInMinutes,
                      @Value("${refreshTokenValidTimeMin}") Integer refreshTokenValidTimeInMinutes) {
        this.secretKey = secretKey;
        this.objectMapper = objectMapper;
        this.accessTokenValidTimeInMinutes = accessTokenValidTimeInMinutes;
        this.refreshTokenValidTimeInMinutes = refreshTokenValidTimeInMinutes;
    }

    private static Date getNow() {
        LocalDateTime localDateTime = LocalDateTime.now();
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
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
        Date now = getNow();
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
        Date now = getNow();
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

    /**
     * Retrieves the subject (sub) claim from the provided JWT token.
     *
     * @param token The JWT token from which to extract the subject claim.
     * @return An {@link Optional} containing the subject claim if present, empty otherwise.
     * @throws BadTokenException If the token cannot be parsed or does not contain a subject claim.
     */
    public Optional<String> getSubjectFromToken(String token) {
        String[] splitToken = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String payload = new String(decoder.decode(splitToken[1]));
        try {
            return Optional.of(objectMapper.readTree(payload).path("sub").asText())
                .filter(text -> !text.isEmpty());
        } catch (JsonProcessingException e) {
            throw new BadTokenException(TOKEN_CAN_NOT_BE_PARSED);
        }
    }

    /**
     * Verifies the integrity and expiration of the provided JWT token using the specified token key.
     *
     * @param token    The JWT token to verify.
     * @param tokenKey The key used for token verification.
     * @throws BadTokenException If the token has expired, cannot be verified, or was not signed by the user.
     */
    public void verifyToken(String token, String tokenKey) {
        SecretKey key = Keys.hmacShaKeyFor(tokenKey.getBytes());
        try {
            Jwts.parser().verifyWith(key).build()
                .parseSignedClaims(token).getPayload()
                .getExpiration();
        } catch (ExpiredJwtException e) {
            throw new BadTokenException(TOKEN_HAS_BEEN_EXPIRED);
        } catch (JwtException | IllegalArgumentException e) {
            throw new BadTokenException(TOKEN_WAS_NOT_SIGNED_BY_USER);
        }
    }
}