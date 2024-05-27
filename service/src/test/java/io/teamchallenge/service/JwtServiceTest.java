package io.teamchallenge.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.teamchallenge.entity.User;
import io.teamchallenge.exception.BadTokenException;
import io.teamchallenge.util.Utils;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.teamchallenge.util.Utils.ACCESS_TOKEN;
import static io.teamchallenge.util.Utils.SECRET_KEY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {
    private final int accessTokenValidTimeInMinutes = 20;
    private final int refreshTokenValidTimeInMinutes = 60;
    private final User user = Utils.getUser();

    @InjectMocks
    private JwtService jwtService =
        new JwtService(SECRET_KEY, new ObjectMapper(), accessTokenValidTimeInMinutes, refreshTokenValidTimeInMinutes);

    @Test
    void getTokenFromRequestTest() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getHeader("Authorization")).thenReturn("Bearer " + ACCESS_TOKEN);

        Optional<String> token = jwtService.getTokenFromRequest(request);

        verify(request).getHeader(eq("Authorization"));
        assertEquals(ACCESS_TOKEN, token.get());
    }

    @Test
    void generateAccessTokenTest() {
        LocalDateTime localDateTime = LocalDateTime.now();
        String accessToken = getToken(localDateTime, accessTokenValidTimeInMinutes, SECRET_KEY);

        String result;
        try (MockedStatic<LocalDateTime> mockedStatic = mockStatic(LocalDateTime.class)) {
            mockedStatic.when(LocalDateTime::now).thenReturn(localDateTime);
            result = jwtService.generateAccessToken(user.getId(), user.getEmail(), user.getRole());
        }

        assertEquals(accessToken, result);
    }

    @Test
    void generateRefreshTokenTest() {
        LocalDateTime localDateTime = LocalDateTime.now();
        String refreshToken = getToken(localDateTime, refreshTokenValidTimeInMinutes, user.getRefreshTokenKey());

        String result;
        try (MockedStatic<LocalDateTime> mockedStatic = mockStatic(LocalDateTime.class)) {
            mockedStatic.when(LocalDateTime::now).thenReturn(localDateTime);
            result = jwtService.generateRefreshToken(user);
        }

        assertEquals(refreshToken, result);
    }

    @Test
    void generateTokenKeyTest() {
        UUID uuid = UUID.randomUUID();
        String key;
        try (MockedStatic<UUID> mockUUID = mockStatic(UUID.class)) {
            mockUUID.when(UUID::randomUUID).thenReturn(uuid);
            key = jwtService.generateTokenKey();
        }
        assertEquals(uuid, UUID.fromString(key));
    }
    @Test
    void getSubjectFromTokenTest() {
        String subject = "test@mail.com";

        Optional<String> subjectFromToken = jwtService.getSubjectFromToken(ACCESS_TOKEN);

        assertEquals(subject, subjectFromToken.get());
    }

    @Test
    void getSubjectFromTokenReturnsEmptyOptionalWhenThereIsNoSubjectInTokenTest() {
        String token = Jwts.builder()
            .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)), Jwts.SIG.HS256)
            .compact();

        Optional<String> subjectFromToken = jwtService.getSubjectFromToken(token);

        assertTrue(subjectFromToken.isEmpty());
    }

    @Test
    void getSubjectFromTokenThrowsBadTokenExceptionWhenTokenCanNotBeParsedTest() {
        String token = "eyJhbGciOaiJIUzI1NiJ9" +
            ".eyJpc3MiOiJHYWRnZXRIbs3VzZSIsImlkIjoxLCJyb2xlIjoiUk9M" +
            "RV9VU0VSIiwiaWF0IjoxNzfE0NzUwMDIyLCJleHAiOjE3MTQ3ODYwMjJ9" +
            ".gYdMkgLvXJuAHXbPmeRY1DiwDKfcltJSxj6RRR-5VaQ";

        assertThrows(BadTokenException.class, ()->jwtService.getSubjectFromToken(token));
    }

    @Test
    void verifyTokenTest() {
        LocalDateTime localDateTime = LocalDateTime.now();
        String token = getToken(localDateTime, refreshTokenValidTimeInMinutes, SECRET_KEY);

        jwtService.verifyToken(token, SECRET_KEY);
    }

    @Test
    void verifyTokenThrowsBadTokenExceptionWhenTokenWasNotSignedByUserTest() {
        LocalDateTime localDateTime = LocalDateTime.now();
        String additionalWrongSymbol = "1";
        String key = SECRET_KEY+ additionalWrongSymbol;
        String token = getToken(localDateTime, refreshTokenValidTimeInMinutes, SECRET_KEY);

        assertThrows(BadTokenException.class, ()->jwtService.verifyToken(token, key));
    }

    @Test
    void verifyTokenThrowsBadTokenExceptionWhenTokenHasBeenExpiredTest() {
        LocalDateTime localDateTime = LocalDateTime.of(0,1,1,0,0);
        String token = getToken(localDateTime, refreshTokenValidTimeInMinutes, SECRET_KEY);

        assertThrows(BadTokenException.class, ()->jwtService.verifyToken(token, SECRET_KEY));
    }

    private String getToken(LocalDateTime localDateTime, int refreshTokenValidTimeInMinutes, String secretKey) {
        Date now = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
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
            .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)), Jwts.SIG.HS256)
            .compact();
    }
}
