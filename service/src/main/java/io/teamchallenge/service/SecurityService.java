package io.teamchallenge.service;

import io.teamchallenge.constant.ExceptionMessage;
import io.teamchallenge.dto.security.SignInRequestDto;
import io.teamchallenge.dto.security.SignInResponseDto;
import io.teamchallenge.dto.security.SignUpRequestDto;
import io.teamchallenge.dto.security.SignUpResponseDto;
import io.teamchallenge.entity.User;
import io.teamchallenge.exception.AlreadyExistsException;
import io.teamchallenge.exception.BadCredentialsException;
import io.teamchallenge.exception.NotFoundException;
import io.teamchallenge.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for user security-related operations such as sign-up and sign-in.
 */
@Service
@Slf4j
@Transactional
public class SecurityService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    /**
     * Constructor for SecurityService.
     *
     * @param userRepository  The user repository.
     * @param jwtService      The JWT service.
     * @param passwordEncoder The password encoder.
     * @param modelMapper     The ModelMapper.
     */
    @Autowired
    public SecurityService(UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder,
                           ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    /**
     * Registers a new user.
     *
     * @param signUpRequestDto The sign-up request DTO containing user details.
     * @return The sign-up response DTO containing user information.
     */
    public SignUpResponseDto signUpUser(SignUpRequestDto signUpRequestDto) {
        log.info("User tries to sign up {}", signUpRequestDto);
        if (userRepository.existsByEmail(signUpRequestDto.getEmail())) {
            throw new AlreadyExistsException(
                ExceptionMessage.USER_WITH_EMAIL_ALREADY_EXISTS.formatted(signUpRequestDto.getEmail()));
        }
        if (userRepository.existsByPhoneNumber(signUpRequestDto.getPhoneNumber())) {
            throw new AlreadyExistsException(
                ExceptionMessage.USER_WITH_PHONE_NUMBER_ALREADY_EXISTS.formatted(signUpRequestDto.getPhoneNumber()));
        }
        User user = modelMapper.map(signUpRequestDto, User.class);
        return modelMapper.map(userRepository.save(user), SignUpResponseDto.class);
    }

    /**
     * Authenticates a user.
     *
     * @param signInRequestDto The sign-in request DTO containing user credentials.
     * @return The sign-in response DTO containing access and refresh tokens.
     * @throws BadCredentialsException if the provided credentials are invalid.
     * @throws NotFoundException       if the user is not found.
     */
    @Transactional(readOnly = true)
    public SignInResponseDto signInUser(SignInRequestDto signInRequestDto) {
        log.info("User tries to sign in {}", signInRequestDto);
        User user = userRepository.findUserByEmail(signInRequestDto.getEmail()).orElseThrow(() -> new NotFoundException(
            ExceptionMessage.USER_NOT_FOUND_BY_EMAIL + signInRequestDto.getEmail()));
        if (!passwordEncoder.matches(signInRequestDto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException(ExceptionMessage.PASSWORD_DOES_NOT_MATCH);
        }
        String accessToken = jwtService.generateAccessToken(user.getId(), user.getEmail(), user.getRole());
        String refreshToken = jwtService.generateRefreshToken(user);
        return new SignInResponseDto(accessToken, refreshToken);
    }
}