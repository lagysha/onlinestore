package io.teamchallenge.controller;

import io.teamchallenge.dto.security.SignInRequestDto;
import io.teamchallenge.dto.security.SignInResponseDto;
import io.teamchallenge.dto.security.SignUpRequestDto;
import io.teamchallenge.dto.security.SignUpResponseDto;
import io.teamchallenge.service.impl.SecurityService;
import io.teamchallenge.utils.Utils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
public class SecurityControllerTest {
    public final SignInResponseDto GET_SIGN_IN_RESPONSE_DTO = Utils.getSignInResponseDto();
    @InjectMocks
    private SecurityController securityController;
    @Mock
    private SecurityService securityService;

    @Test
    void signUpUserTest() {
        SignUpRequestDto signUpRequestDto = Utils.getSignUpRequestDto();
        SignUpResponseDto signUpResponseDto = Utils.getSignUpResponseDto();
        when(securityService.signUpUser(signUpRequestDto)).thenReturn(signUpResponseDto);

        ResponseEntity<SignUpResponseDto> signUpResponseDtoResponseEntity =
            securityController.signUpUser(signUpRequestDto);

        verify(securityService).signUpUser(eq(signUpRequestDto));
        assertEquals(CREATED, signUpResponseDtoResponseEntity.getStatusCode());
        assertEquals(signUpResponseDto, signUpResponseDtoResponseEntity.getBody());
    }

    @Test
    void signInUserTest() {
        SignInRequestDto signInRequestDto = Utils.getSignInRequestDto();
        when(securityService.signInUser(signInRequestDto)).thenReturn(GET_SIGN_IN_RESPONSE_DTO);

        ResponseEntity<SignInResponseDto> signInResponseDtoResponseEntity =
            securityController.signInUser(signInRequestDto);

        verify(securityService).signInUser(eq(signInRequestDto));
        assertEquals(OK, signInResponseDtoResponseEntity.getStatusCode());
        assertEquals(GET_SIGN_IN_RESPONSE_DTO, signInResponseDtoResponseEntity.getBody());
    }

    @Test
    void updateAccessTokenTest() {
        String refreshToken = "refreshTokenTest";
        when(securityService.updateAccessToken(refreshToken)).thenReturn(GET_SIGN_IN_RESPONSE_DTO);

        ResponseEntity<SignInResponseDto> signInResponseDtoResponseEntity =
            securityController.updateAccessToken(refreshToken);

        verify(securityService).updateAccessToken(eq(refreshToken));
        assertEquals(OK, signInResponseDtoResponseEntity.getStatusCode());
        assertEquals(GET_SIGN_IN_RESPONSE_DTO, signInResponseDtoResponseEntity.getBody());
    }
}
