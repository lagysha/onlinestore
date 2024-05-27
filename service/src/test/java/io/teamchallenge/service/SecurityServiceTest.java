package io.teamchallenge.service;

import io.teamchallenge.dto.security.SignInRequestDto;
import io.teamchallenge.dto.security.SignInResponseDto;
import io.teamchallenge.dto.security.SignUpRequestDto;
import io.teamchallenge.dto.security.SignUpResponseDto;
import io.teamchallenge.entity.User;
import io.teamchallenge.exception.AlreadyExistsException;
import io.teamchallenge.exception.BadCredentialsException;
import io.teamchallenge.exception.BadTokenException;
import io.teamchallenge.exception.NotFoundException;
import io.teamchallenge.repository.UserRepository;
import io.teamchallenge.util.Utils;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SecurityServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtService jwtService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private SecurityService securityService;

    private final User user = Utils.getUser();

    private final User newUser = Utils.getNewUser();

    private final SignUpRequestDto signUpRequestDto = Utils.getSignUpRequestDto();

    private final SignInRequestDto signInRequestDto = Utils.getSignInRequestDto();
    private final String accessToken = Utils.getAccessToken();
    private final String refreshToken = Utils.getRefreshToken();

    @Test
    void signUpUserTest() {
        SignUpResponseDto signUpResponseDto = Utils.getSignUpResponseDto();

        when(modelMapper.map(signUpRequestDto, User.class)).thenReturn(newUser);
        when(userRepository.existsByEmail(signUpRequestDto.getEmail())).thenReturn(false);
        when(userRepository.existsByPhoneNumber(signUpRequestDto.getPhoneNumber())).thenReturn(false);
        when(userRepository.save(newUser)).thenReturn(user);
        when(modelMapper.map(user, SignUpResponseDto.class)).thenReturn(signUpResponseDto);

        SignUpResponseDto result = securityService.signUpUser(signUpRequestDto);

        verify(userRepository).save(eq(newUser));
        assertEquals(signUpResponseDto, result);
    }

    @Test
    void signUpUserThrowsAlreadyExistsExceptionWhenUserExistsWithSameEmailTest() {
        when(userRepository.existsByEmail(signUpRequestDto.getEmail())).thenReturn(true);

        assertThrows(AlreadyExistsException.class, ()->securityService.signUpUser(signUpRequestDto));
    }

    @Test
    void signUpUserThrowsAlreadyExistsExceptionWhenUserExistsWithSamePhoneNumberTest() {
        when(userRepository.existsByPhoneNumber(signUpRequestDto.getPhoneNumber())).thenReturn(true);

        assertThrows(AlreadyExistsException.class, ()->securityService.signUpUser(signUpRequestDto));
    }

    @Test
    void signInUserTest() {
        SignInResponseDto signInResponseDto = SignInResponseDto.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();

        when(userRepository.findUserByEmail(signInRequestDto.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(signInRequestDto.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtService.generateAccessToken(user.getId(), user.getEmail(), user.getRole())).thenReturn(accessToken);
        when(jwtService.generateRefreshToken(user)).thenReturn(refreshToken);

        SignInResponseDto result = securityService.signInUser(signInRequestDto);

        assertEquals(signInResponseDto, result);
    }

    @Test
    void signInUserThrowsNotFoundExceptionWhenUserNotFoundByEmailTest() {
        when(userRepository.findUserByEmail(signInRequestDto.getEmail())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, ()->securityService.signInUser(signInRequestDto));
    }

    @Test
    void signInUserThrowsBadRequestExceptionWhenPasswordDoesNotMatchTest() {
        when(userRepository.findUserByEmail(signInRequestDto.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(signInRequestDto.getPassword(), user.getPassword())).thenReturn(false);

        assertThrows(BadCredentialsException.class, ()->securityService.signInUser(signInRequestDto));
    }

    @Test
    void updateAccessTokenTest() {
        SignInResponseDto signInResponseDto = SignInResponseDto.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken+"Result")
            .build();

        when(jwtService.getSubjectFromToken(refreshToken)).thenReturn(Optional.of(user.getEmail()));
        when(userRepository.findUserByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.generateAccessToken(user.getId(), user.getEmail(), user.getRole()))
            .thenReturn(signInResponseDto.getAccessToken());
        when(jwtService.generateRefreshToken(user)).thenReturn(signInResponseDto.getRefreshToken());

        SignInResponseDto result = securityService.updateAccessToken(refreshToken);

        assertEquals(signInResponseDto, result);
    }

    @Test
    void updateAccessTokenThrowsBadTokenExceptionWhenTokenDoesNotContainSubjectTest() {
        when(jwtService.getSubjectFromToken(refreshToken)).thenReturn(Optional.empty());

        assertThrows(BadTokenException.class,()->securityService.updateAccessToken(refreshToken));
    }

    @Test
    void updateAccessTokenThrowsNotFoundExceptionWhenUserNotFoundByEmailTest() {
        when(jwtService.getSubjectFromToken(refreshToken)).thenReturn(Optional.of(user.getEmail()));
        when(userRepository.findUserByEmail(user.getEmail())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,()->securityService.updateAccessToken(refreshToken));
    }
}