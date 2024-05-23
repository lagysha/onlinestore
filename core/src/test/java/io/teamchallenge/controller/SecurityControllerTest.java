package io.teamchallenge.controller;

import io.teamchallenge.dto.security.SignInRequestDto;
import io.teamchallenge.dto.security.SignInResponseDto;
import io.teamchallenge.dto.security.SignUpRequestDto;
import io.teamchallenge.dto.security.SignUpResponseDto;
import io.teamchallenge.service.SecurityService;
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
    @InjectMocks
    private SecurityController securityController;
    @Mock
    private SecurityService securityService;
    private final String REQUEST_MAPPING = "/api/v1";
    private final SignInResponseDto signInResponseDto = SignInResponseDto.builder()
        .accessToken("accessToken")
        .refreshToken("refreshToken")
        .build();


    @Test
    void signUpUserTest(){
        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
            .email("test@mail.com")
            .firstName("John")
            .lastName("Doe")
            .phoneNumber("0123456789")
            .password("Password1234!")
            .build();
        SignUpResponseDto signUpResponseDto = SignUpResponseDto.builder()
            .id(1L)
            .email("test@mail.com")
            .firstName("John")
            .lastName("Doe")
            .build();
        when(securityService.signUpUser(signUpRequestDto)).thenReturn(signUpResponseDto);

        ResponseEntity<SignUpResponseDto> signUpResponseDtoResponseEntity =
            securityController.signUpUser(signUpRequestDto);

        verify(securityService).signUpUser(eq(signUpRequestDto));
        assertEquals(CREATED, signUpResponseDtoResponseEntity.getStatusCode());
        assertEquals(signUpResponseDto, signUpResponseDtoResponseEntity.getBody());
    }

    @Test
    void signInUserTest() {
        SignInRequestDto signInRequestDto = SignInRequestDto.builder()
            .email("test@mail.com")
            .password("Password1234!")
            .build();

        when(securityService.signInUser(signInRequestDto)).thenReturn(signInResponseDto);

        ResponseEntity<SignInResponseDto> signInResponseDtoResponseEntity =
            securityController.signInUser(signInRequestDto);

        verify(securityService).signInUser(eq(signInRequestDto));
        assertEquals(OK, signInResponseDtoResponseEntity.getStatusCode());
        assertEquals(signInResponseDto, signInResponseDtoResponseEntity.getBody());
    }

    @Test
    void updateAccessTokenTest() {
        String refreshToken = "refreshTokenTest";

        when(securityService.updateAccessToken(refreshToken)).thenReturn(signInResponseDto);

        ResponseEntity<SignInResponseDto> signInResponseDtoResponseEntity =
            securityController.updateAccessToken(refreshToken);

        verify(securityService).updateAccessToken(eq(refreshToken));
        assertEquals(OK, signInResponseDtoResponseEntity.getStatusCode());
        assertEquals(signInResponseDto, signInResponseDtoResponseEntity.getBody());
    }
}
