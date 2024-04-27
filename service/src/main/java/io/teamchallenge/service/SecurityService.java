package io.teamchallenge.service;

import io.teamchallenge.constant.ExceptionMessage;
import io.teamchallenge.dto.security.SignInRequestDto;
import io.teamchallenge.dto.security.SignInResponseDto;
import io.teamchallenge.dto.security.SignUpRequestDto;
import io.teamchallenge.dto.security.SignUpResponseDto;
import io.teamchallenge.entity.User;
import io.teamchallenge.exception.BadCredentialsException;
import io.teamchallenge.exception.NotFoundException;
import io.teamchallenge.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class SecurityService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Autowired
    public SecurityService(UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    public SignUpResponseDto signUpUser(SignUpRequestDto signUpRequestDto) {
        log.info("user tries to sign up {}", signUpRequestDto);
        User user = modelMapper.map(signUpRequestDto, User.class);
        user.getAddress().setUser(user);
        return modelMapper.map(userRepository.save(user),SignUpResponseDto.class);
    }

    @Transactional(readOnly = true)
    public SignInResponseDto signInUser(SignInRequestDto signInRequestDto) {
        log.info("user tries to sign in {}", signInRequestDto);
        User user = userRepository.findUserByEmail(signInRequestDto.getEmail()).orElseThrow(() -> new NotFoundException(
            ExceptionMessage.USER_NOT_FOUND_BY_EMAIL + signInRequestDto.getEmail()));
        if (!passwordEncoder.matches(signInRequestDto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException(ExceptionMessage.PASSWORD_DOES_NOT_MATCH);
        }
        String accessToken = jwtService.generateAccessToken(user.getId(),user.getEmail(),user.getRole());
        String refreshToken = jwtService.generateRefreshToken(user);
        return new SignInResponseDto(accessToken, refreshToken);
    }
}
