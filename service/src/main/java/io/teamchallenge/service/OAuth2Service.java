package io.teamchallenge.service;

import io.teamchallenge.dto.security.SignInResponseDto;
import java.util.Map;

public interface OAuth2Service {
    /**
     * Authenticates user using google authentication.
     *
     * @param attributes    map of attributes
     * @return {@link SignInResponseDto}
     */
    SignInResponseDto authenticate(Map<String, Object> attributes);
}
