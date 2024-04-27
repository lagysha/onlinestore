package io.teamchallenge.security.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.teamchallenge.dto.user.UserVO;
import io.teamchallenge.service.JwtService;
import io.teamchallenge.service.UserAuthorisationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class AccessTokenJwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserAuthorisationService userAuthorisationService;

    /**
     * Constructor for AccessTokenJwtAuthenticationFilter.
     *
     * @param jwtService               Service for JWT operations.
     * @param authenticationManager    Authentication manager for JWT tokens.
     * @param userAuthorisationService Service for user authorization.
     */
    public AccessTokenJwtAuthenticationFilter(JwtService jwtService, AuthenticationManager authenticationManager,
                                              UserAuthorisationService userAuthorisationService) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userAuthorisationService = userAuthorisationService;
    }

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
                log.info("token: {}", jwt);

                Authentication authentication =
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwt, null));
                UserVO userVO =
                    userAuthorisationService.findUserVOByEmail((String) authentication.getPrincipal());
                log.info("user: {}", userVO);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (ExpiredJwtException e) {
            log.info("Token has expired: " + token);
        }
        filterChain.doFilter(request, response);
    }
}