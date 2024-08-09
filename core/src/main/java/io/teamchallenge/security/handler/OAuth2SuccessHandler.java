package io.teamchallenge.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.teamchallenge.dto.security.SignInResponseDto;
import io.teamchallenge.service.OAuth2Service;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final OAuth2Service oauth2Service;

    /**
     * Handles successful OAuth2 authentication and returns a JSON response containing the user's sign-in details.
     *
     * @param request the request which caused the successful authentication
     * @param response the response
     * @param authentication the <tt>Authentication</tt> object which was created during
     * the authentication process.
     * @throws IOException exception
     * @throws ServletException exception
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        if (authentication instanceof OAuth2AuthenticationToken token) {
            Map<String, Object> attributes = token.getPrincipal().getAttributes();
            SignInResponseDto successSignInDto = oauth2Service.authenticate(attributes);
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(successSignInDto);
            response.setContentType("application/json");
            PrintWriter writer = response.getWriter();
            writer.write(json);
            writer.flush();
            writer.close();
        } else {
            log.error("OAuth2 authentication failed");
        }
    }
}
