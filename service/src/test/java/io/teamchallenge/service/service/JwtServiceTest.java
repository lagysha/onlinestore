package io.teamchallenge.service.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.teamchallenge.entity.User;
import io.teamchallenge.enumerated.Role;
import io.teamchallenge.service.JwtService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {
    public static final int accessTokenValidTimeInMinutes = 20;
    public static final int refreshTokenValidTimeInMinutes = 60;
    public static final String secretKey = "5cZAVF/SKSCmCM2+1azD2XHK7K2PChcSg32vrrEh/Qk=";
    @InjectMocks
    private JwtService jwtService =
        new JwtService(secretKey, accessTokenValidTimeInMinutes, refreshTokenValidTimeInMinutes);

    @Test
    void getTokenFromRequestTest() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        String accessToken = "eyJhbGciOiJIUzI1NiJ9" +
            ".eyJpc3MiOiJHYWRnZXRIb3VzZSIsInN1YiI6InRlc3RAbWFpbC5jb20iLCJpZCI6MSwicm9sZSI6IlJPT" +
            "EVfVVNFUiIsImlhdCI6MTcxNDc1MDAyMiwiZXhwIjoxNzE0Nzg2MDIyfQ" +
            ".sfkczlafsasfVxmd9asfasfasfasCu8DbWbZAkSWHujs";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + accessToken);

        Optional<String> token = jwtService.getTokenFromRequest(request);

        verify(request).getHeader(eq("Authorization"));
        assertEquals(accessToken, token.get());
    }

    @Test
    void generateAccessTokenTest() {
        Integer id = 1;
        Role role = Role.ROLE_USER;
        String email = "test@mail.com";

        LocalDateTime localDateTime = LocalDateTime.now();
        Date now = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.MINUTE, accessTokenValidTimeInMinutes);

        String accessToken = Jwts.builder()
            .issuer("GadgetHouse")
            .subject(email)
            .claim("id", id)
            .claim("role", role)
            .issuedAt(now)
            .expiration(calendar.getTime())
            .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)), Jwts.SIG.HS256)
            .compact();

        String result;
        try (MockedStatic<LocalDateTime> mockedStatic = mockStatic(LocalDateTime.class)) {
            mockedStatic.when(LocalDateTime::now).thenReturn(localDateTime);
            result = jwtService.generateAccessToken(Long.parseLong(id.toString()), email, role);
        }

        assertEquals(accessToken, result);
    }

    @Test
    void generateRefreshTokenTest() {
        Integer id = 1;
        Role role = Role.ROLE_USER;
        String email = "test@mail.com";
        String secretKey = "5cZAVF/SKSCmCM2+1azD2XHK7K2PChcSg32vrrEh/Qk1";
        User user = User.builder()
            .id(Long.parseLong(id.toString()))
            .email(email)
            .role(role)
            .refreshTokenKey(secretKey)
            .build();

        LocalDateTime localDateTime = LocalDateTime.now();
        Date now = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.MINUTE, refreshTokenValidTimeInMinutes);

        String accessToken = Jwts.builder()
            .issuer("GadgetHouse")
            .subject(email)
            .claim("id", id)
            .claim("role", role)
            .issuedAt(now)
            .expiration(calendar.getTime())
            .signWith(Keys.hmacShaKeyFor(user.getRefreshTokenKey().getBytes(StandardCharsets.UTF_8)), Jwts.SIG.HS256)
            .compact();

        String result;
        try (MockedStatic<LocalDateTime> mockedStatic = mockStatic(LocalDateTime.class)) {
            mockedStatic.when(LocalDateTime::now).thenReturn(localDateTime);
            result = jwtService.generateRefreshToken(user);
        }

        assertEquals(accessToken, result);
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
}
