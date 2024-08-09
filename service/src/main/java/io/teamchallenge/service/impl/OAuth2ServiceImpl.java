package io.teamchallenge.service.impl;

import io.teamchallenge.dto.security.SignInResponseDto;
import io.teamchallenge.entity.User;
import io.teamchallenge.exception.NotFoundException;
import io.teamchallenge.repository.UserRepository;
import io.teamchallenge.service.OAuth2Service;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static io.teamchallenge.constant.ExceptionMessage.USER_NOT_FOUND_BY_EMAIL;

@Service
@RequiredArgsConstructor
public class OAuth2ServiceImpl implements OAuth2Service {
    private final JwtService jwtService;
    private final UserRepository userRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public SignInResponseDto authenticate(Map<String, Object> attributes) {
        String email = attributes.get("email").toString();
        User user = userRepository.findUserByEmail(email).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_BY_EMAIL.formatted(email)));

        return SignInResponseDto.builder()
            .accessToken(jwtService.generateAccessToken(user.getId(), user.getEmail(), user.getRole()))
            .refreshToken(jwtService.generateRefreshToken(user))
            .build();
    }
}
