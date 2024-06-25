package io.teamchallenge.security.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.DefaultHeader;
import io.jsonwebtoken.impl.DefaultJwtParserBuilder;
import io.jsonwebtoken.security.Keys;
import io.teamchallenge.service.impl.JwtService;
import io.teamchallenge.utils.Utils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Optional;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccessTokenJwtAuthenticationFilterTest {
    @InjectMocks
    private AccessTokenJwtAuthenticationFilter authenticationFilter;
    @Mock
    private JwtService jwtService;

    @Test
    void doFilterInternalTest() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        String accessToken = Utils.getAccessToken();
        String secretKeyString = Utils.getSecretKey();
        String email = "test@mail.com";
        String roleUser = "ROLE_USER";
        SecretKey secretKey = mock(SecretKey.class);
        DefaultJwtParserBuilder parserBuilder = mock(DefaultJwtParserBuilder.class);
        JwtParser jwtParser = mock(JwtParser.class);
        Claims claims = mock(Claims.class);
        Jws<Claims> jws = mock(Jws.class);

        when(jwtService.getTokenFromRequest(request)).thenReturn(Optional.of(accessToken));
        when(jwtService.getSecretKey()).thenReturn(secretKeyString);
        try (MockedStatic<Jwts> jwts = mockStatic(Jwts.class);
             MockedStatic<Keys> secretKeyMockedStatic = mockStatic(Keys.class)) {
            when(Keys.hmacShaKeyFor(secretKeyString.getBytes(StandardCharsets.UTF_8))).thenReturn(secretKey);
            jwts.when(Jwts::parser).thenReturn(parserBuilder);
            when(parserBuilder.verifyWith(secretKey)).thenReturn(parserBuilder);
            when(parserBuilder.build()).thenReturn(jwtParser);
            when(claims.getSubject()).thenReturn(email);
            when(claims.get("role")).thenReturn(roleUser);
            when(claims.toString()).thenReturn("claims");
            when(jws.getPayload()).thenReturn(claims);
            when(jwtParser.parseSignedClaims(accessToken)).thenReturn(jws);

            authenticationFilter.doFilterInternal(request, response, filterChain);

            verify(jwtService).getTokenFromRequest(request);
            verify(jwtService).getSecretKey();
            Jwts.parser();
            verify(parserBuilder).verifyWith(secretKey);
            verify(parserBuilder).build();
            verify(jwtParser).parseSignedClaims(accessToken);
            verify(claims).getSubject();
            verify(claims).get("role");
            verify(filterChain).doFilter(request, response);
        }
    }

    @Test
    void doFilterInternalWhenTokenHasExpiredTest() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        String accessToken = Utils.getAccessToken();
        String secretKeyString = Utils.getSecretKey();
        SecretKey secretKey = mock(SecretKey.class);
        DefaultJwtParserBuilder parserBuilder = mock(DefaultJwtParserBuilder.class);
        JwtParser jwtParser = mock(JwtParser.class);
        Claims claims = mock(Claims.class);

        when(jwtService.getTokenFromRequest(request)).thenReturn(Optional.of(accessToken));
        when(jwtService.getSecretKey()).thenReturn(secretKeyString);
        try (MockedStatic<Jwts> jwts = mockStatic(Jwts.class);
             MockedStatic<Keys> secretKeyMockedStatic = mockStatic(Keys.class)) {
            when(Keys.hmacShaKeyFor(secretKeyString.getBytes(StandardCharsets.UTF_8))).thenReturn(secretKey);
            jwts.when(Jwts::parser).thenReturn(parserBuilder);
            when(parserBuilder.verifyWith(secretKey)).thenReturn(parserBuilder);
            when(parserBuilder.build()).thenReturn(jwtParser);
            when(jwtParser.parseSignedClaims(accessToken)).thenThrow(
                new ExpiredJwtException(new DefaultHeader(new HashMap<>()), claims, "message"));

            authenticationFilter.doFilterInternal(request, response, filterChain);

            verify(jwtService).getTokenFromRequest(request);
            verify(jwtService).getSecretKey();
            Jwts.parser();
            verify(parserBuilder).verifyWith(secretKey);
            verify(parserBuilder).build();
            verify(jwtParser).parseSignedClaims(accessToken);
            verify(filterChain).doFilter(request, response);
        }
    }
}
