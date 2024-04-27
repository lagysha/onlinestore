package io.teamchallenge.controllers;

import io.teamchallenge.dto.security.SignInRequestDto;
import io.teamchallenge.dto.security.SignInResponseDto;
import io.teamchallenge.dto.security.SignUpRequestDto;
import io.teamchallenge.dto.security.SignUpResponseDto;
import io.teamchallenge.service.SecurityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@Validated
public class SecurityController {
    private final SecurityService securityService;
    @Autowired
    public SecurityController(SecurityService securityService) {
        this.securityService = securityService;
    }

    @PostMapping("/signUp")
    @ResponseStatus(CREATED)
    public SignUpResponseDto signUpUser (@Valid @RequestBody SignUpRequestDto signUpRequestDto) {
        return securityService.signUpUser(signUpRequestDto);
    }

    @GetMapping("/signIn")
    public SignInResponseDto signInUser (@Valid @RequestBody SignInRequestDto signInRequestDto) {
        return securityService.signInUser(signInRequestDto);
    }
}
