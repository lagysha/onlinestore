package io.teamchallenge.config;

import static io.teamchallenge.constant.SecurityConstants.ADMIN;
import io.teamchallenge.security.filter.AccessTokenJwtAuthenticationFilter;
import io.teamchallenge.service.JwtService;
import io.teamchallenge.service.UserAuthorizationService;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import static io.teamchallenge.constant.SecurityConstants.USER;
import static jakarta.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

@Configuration
@EnableWebSecurity
@EnableGlobalAuthentication
public class SecurityConfig {
    private final List<String> allowedOrigins;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtService jwtService;
    private final UserAuthorizationService userAuthorizationService;

    /**
     * Constructor for SecurityConfig.
     *
     * @param allowedOrigins              Array of allowed origins.
     * @param authenticationConfiguration Authentication configuration.
     * @param jwtService                  JWT service.
     * @param userAuthorizationService    User authorisation service.
     */
    @Autowired
    public SecurityConfig(@Value("${ALLOWED_ORIGINS}") String[] allowedOrigins,
                          AuthenticationConfiguration authenticationConfiguration, JwtService jwtService,
                          UserAuthorizationService userAuthorizationService) {
        this.allowedOrigins = List.of(allowedOrigins);
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtService = jwtService;
        this.userAuthorizationService = userAuthorizationService;
    }

    /**
     * Configures the security filter chain.
     *
     * @param http HttpSecurity object.
     * @return SecurityFilterChain object.
     * @throws Exception If an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.cors(cors -> cors
                .configurationSource(configSource -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(allowedOrigins);
                    config.setAllowedMethods(Collections.singletonList("*"));
                    config.setAllowedHeaders(List.of("Access-Control-Allow-Origin", "Access-Control-Allow-Headers",
                        "X-Requested-With", "Origin", "Content-Type", "Accept", "Authorization"));
                    config.setAllowCredentials(true);
                    config.setMaxAge(3600L);
                    return config;
                }))
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterAfter(
                new AccessTokenJwtAuthenticationFilter(jwtService, authenticationManager(), userAuthorizationService),
                BasicAuthenticationFilter.class)
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint((req, resp, exc) ->
                    resp.sendError(SC_UNAUTHORIZED, "Please, authorize."))
                .accessDeniedHandler((req, resp, exc) ->
                    resp.sendError(SC_FORBIDDEN, "You don't have authorities.")))
            .authorizeHttpRequests(req -> req
                .requestMatchers(HttpMethod.POST,
                    "/api/v1/signUp",
                    "/api/v1/signIn",
                    "/api/v1/updateAccessToken"
                )
                .permitAll()
                .requestMatchers(HttpMethod.GET,
                    "/api/v1/cart-items/{user_id}",
                    "/api/v1/products",
                    "/api/v1/products/{id}",
                    "/hello")
                .hasRole(USER)
                .requestMatchers(HttpMethod.POST,
                    "/api/v1/cart-items/{user_id}/{product_id}")
                .hasRole(USER)
                .requestMatchers(HttpMethod.DELETE,
                    "/api/v1/cart-items/{user_id}/{product_id}")
                .hasRole(USER)
                .requestMatchers(
                    "/api/v1/products")
                .hasRole(ADMIN)
            ).build();
    }

    /**
     * Provides an AuthenticationManager bean.
     *
     * @return AuthenticationManager object.
     * @throws Exception If an error occurs while obtaining the authentication manager.
     */
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Provides a PasswordEncoder bean.
     *
     * @return BCryptPasswordEncoder object.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}