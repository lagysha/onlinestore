package io.teamchallenge.config;

import io.teamchallenge.security.filter.AccessTokenJwtAuthenticationFilter;
import io.teamchallenge.service.JwtService;
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

import static io.teamchallenge.constant.AppConstant.API_V1;
import static io.teamchallenge.constant.SecurityConstants.ADMIN;
import static io.teamchallenge.constant.SecurityConstants.USER;
import static jakarta.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

@Configuration
@EnableWebSecurity
@EnableGlobalAuthentication
public class SecurityConfig {
    private final List<String> allowedOrigins;
    private final JwtService jwtService;
    private final AuthenticationConfiguration authenticationConfiguration;

    /**
     * Constructor for SecurityConfig.
     *
     * @param allowedOrigins              Array of allowed origins.
     * @param jwtService                  JWT service.
     * @param authenticationConfiguration Authentication configuration
     */
    @Autowired
    public SecurityConfig(@Value("${ALLOWED_ORIGINS}") String[] allowedOrigins, JwtService jwtService,
                          AuthenticationConfiguration authenticationConfiguration) {
        this.allowedOrigins = List.of(allowedOrigins);
        this.jwtService = jwtService;
        this.authenticationConfiguration = authenticationConfiguration;
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
                new AccessTokenJwtAuthenticationFilter(jwtService),
                BasicAuthenticationFilter.class)
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint((req, resp, exc) ->
                    resp.sendError(SC_UNAUTHORIZED, "Please, authorize."))
                .accessDeniedHandler((req, resp, exc) ->
                    resp.sendError(SC_FORBIDDEN, "You don't have authorities.")))
            .authorizeHttpRequests(req -> req
                .requestMatchers(HttpMethod.POST,
                    API_V1 + "/signUp",
                    API_V1 + "/signIn",
                    API_V1 + "/updateAccessToken"
                )
                .permitAll()
                .requestMatchers(HttpMethod.GET,
                    API_V1 + "/categories/{categoryId}/attribute-attributeValues",
                    API_V1 + "/products",
                    API_V1 + "/categories",
                    API_V1 + "/products/{id}",
                    "/hello")
                .permitAll()
                .requestMatchers(HttpMethod.GET,
                    API_V1 + "/cart-items/{product_id}")
                .hasRole(USER)
                .requestMatchers(HttpMethod.POST,
                    API_V1 + "/cart-items/{product_id}")
                .hasRole(USER)
                .requestMatchers(HttpMethod.PATCH,
                    API_V1 + "/cart-items/{product_id}")
                .hasRole(USER)
                .requestMatchers(HttpMethod.DELETE,
                    API_V1 + "/cart-items/{product_id}")
                .hasRole(USER)
                .requestMatchers(
                    API_V1 + "/products")
                .hasRole(ADMIN)
                .anyRequest()
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