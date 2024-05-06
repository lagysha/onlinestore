package io.teamchallenge.core.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.teamchallenge.controller.SecurityController;
import io.teamchallenge.dto.security.SignInRequestDto;
import io.teamchallenge.dto.security.SignInResponseDto;
import io.teamchallenge.dto.security.SignUpRequestDto;
import io.teamchallenge.dto.security.SignUpResponseDto;
import io.teamchallenge.handler.CustomExceptionHandler;
import io.teamchallenge.service.SecurityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class SecurityControllerTest {
    @InjectMocks
    private SecurityController securityController;
    @Mock
    private SecurityService securityService;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private final String REQUEST_MAPPING = "/api/v1";
    private final SignInResponseDto signInResponseDto = SignInResponseDto.builder()
        .accessToken("accessToken")
        .refreshToken("refreshToken")
        .build();

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
            .standaloneSetup(securityController)
            .setControllerAdvice(new CustomExceptionHandler(new DefaultErrorAttributes()))
            .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void signUpUserTest() throws Exception {
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

        String response = mockMvc.perform(post(REQUEST_MAPPING + "/signUp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequestDto)))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn()
            .getResponse()
            .getContentAsString();
        SignUpResponseDto responseDto = objectMapper.readValue(response, SignUpResponseDto.class);

        verify(securityService).signUpUser(eq(signUpRequestDto));
        assertEquals(signUpResponseDto, responseDto);
    }

    @Test
    void signInUserTest() throws Exception {
        SignInRequestDto signInRequestDto = SignInRequestDto.builder()
            .email("test@mail.com")
            .password("Password1234!")
            .build();

        when(securityService.signInUser(signInRequestDto)).thenReturn(signInResponseDto);

        String response = mockMvc.perform(post(REQUEST_MAPPING + "/signIn")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signInRequestDto)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn()
            .getResponse()
            .getContentAsString();
        SignInResponseDto responseDto = objectMapper.readValue(response, SignInResponseDto.class);

        verify(securityService).signInUser(eq(signInRequestDto));
        assertEquals(signInResponseDto, responseDto);
    }

    @Test
    void updateAccessTokenTest() throws Exception {
        String refreshToken = "refreshTokenTest";

        when(securityService.updateAccessToken(refreshToken)).thenReturn(signInResponseDto);

        String response = mockMvc.perform(post(REQUEST_MAPPING + "/updateAccessToken")
                    .param("refreshToken", refreshToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();
        SignInResponseDto responseDto = objectMapper.readValue(response, SignInResponseDto.class);

        verify(securityService).updateAccessToken(eq(refreshToken));
        assertEquals(signInResponseDto, responseDto);
    }
}
