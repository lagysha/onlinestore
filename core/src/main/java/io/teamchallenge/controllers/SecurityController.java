package io.teamchallenge.controllers;

import io.teamchallenge.dto.security.SignInRequestDto;
import io.teamchallenge.dto.security.SignInResponseDto;
import io.teamchallenge.dto.security.SignUpRequestDto;
import io.teamchallenge.dto.security.SignUpResponseDto;
import io.teamchallenge.service.SecurityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@Validated
public class SecurityController {
    private final SecurityService securityService;

    /**
     * Constructor for SecurityController.
     *
     * @param securityService SecurityService instance to handle security operations.
     */
    @Autowired
    public SecurityController(SecurityService securityService) {
        this.securityService = securityService;
    }

    /**
     * Endpoint for user sign-up.
     *
     * @param signUpRequestDto Request body containing sign-up information.
     * @return SignUpResponseDto containing sign-up result.
     */
    @PostMapping("/signUp")
    @ResponseStatus(CREATED)
    public SignUpResponseDto signUpUser(@Valid @RequestBody SignUpRequestDto signUpRequestDto) {
        return securityService.signUpUser(signUpRequestDto);
    }

    /**
     * Endpoint for user sign-in.
     *
     * @param signInRequestDto Request body containing sign-in information.
     * @return SignInResponseDto containing sign-in result.
     */
    @GetMapping("/signIn")
    public SignInResponseDto signInUser(@Valid @RequestBody SignInRequestDto signInRequestDto) {
        return securityService.signInUser(signInRequestDto);
    }
}