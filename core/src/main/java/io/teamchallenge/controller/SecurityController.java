package io.teamchallenge.controller;

import io.teamchallenge.dto.security.SignInRequestDto;
import io.teamchallenge.dto.security.SignInResponseDto;
import io.teamchallenge.dto.security.SignUpRequestDto;
import io.teamchallenge.dto.security.SignUpResponseDto;
import io.teamchallenge.service.SecurityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1")
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
    public ResponseEntity<SignUpResponseDto> signUpUser(@Valid @RequestBody SignUpRequestDto signUpRequestDto) {
        return ResponseEntity.status(CREATED).body(securityService.signUpUser(signUpRequestDto));
    }

    /**
     * Endpoint for user sign-in.
     *
     * @param signInRequestDto Request body containing sign-in information.
     * @return SignInResponseDto containing sign-in result.
     */
    @PostMapping("/signIn")
    public ResponseEntity<SignInResponseDto> signInUser(@Valid @RequestBody SignInRequestDto signInRequestDto) {
        return ResponseEntity.status(OK).body(securityService.signInUser(signInRequestDto));
    }
}