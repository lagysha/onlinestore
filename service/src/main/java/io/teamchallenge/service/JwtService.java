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

@Service
public class JwtService {
    @Getter
    private final String secretKey;
    private final int accessTokenValidTimeInMinutes;
    private final int refreshTokenValidTimeInMinutes;

    @Autowired
    public JwtService(@Value("${JWT_TOKEN_KEY}") String secretKey,
                      @Value("${accessTokenValidTimeMin}") int accessTokenValidTimeInMinutes,
                      @Value("${refreshTokenValidTimeMin}") int refreshTokenValidTimeInMinutes) {
        this.secretKey = secretKey;
        this.accessTokenValidTimeInMinutes = accessTokenValidTimeInMinutes;
        this.refreshTokenValidTimeInMinutes = refreshTokenValidTimeInMinutes;
    }

    public Optional<String> getTokenFromRequest(HttpServletRequest request) {
        return Optional
            .ofNullable(request.getHeader("Authorization"))
            .filter(authHeader -> authHeader.startsWith("Bearer "))
            .map(token -> token.substring(7));
    }

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

    public String generateTokenKey() {
        return UUID.randomUUID().toString();
    }
}
