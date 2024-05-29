package io.teamchallenge.security.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.teamchallenge.security.token.ClaimsUsernamePasswordAuthenticationToken;
import io.teamchallenge.service.impl.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Optional;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class AccessTokenJwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    /**
     * Performs filtering of the incoming HTTP request.
     *
     * @param request     HTTPServletRequest object.
     * @param response    HTTPServletResponse object.
     * @param filterChain FilterChain object.
     * @throws ServletException If an error occurs during servlet processing.
     * @throws IOException      If an I/O error occurs.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        Optional<String> token = jwtService.getTokenFromRequest(request);
        try {
            if (token.isPresent()) {
                String jwt = token.get();
                SecretKey secretKey = Keys.hmacShaKeyFor(jwtService.getSecretKey().getBytes(StandardCharsets.UTF_8));
                JwtParser jwtParser = Jwts.parser().verifyWith(secretKey).build();

                Claims claims = jwtParser.parseSignedClaims(jwt).getPayload();
                String email = claims.getSubject();
                String role = (String) claims.get("role");
                log.info("user: {}", claims);

                ClaimsUsernamePasswordAuthenticationToken
                    authentication = new ClaimsUsernamePasswordAuthenticationToken(email, "",
                    Collections.singleton(new SimpleGrantedAuthority(role)), claims);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (ExpiredJwtException e) {
            log.error("Token has expired: " + token);
        }
        filterChain.doFilter(request, response);
    }
}