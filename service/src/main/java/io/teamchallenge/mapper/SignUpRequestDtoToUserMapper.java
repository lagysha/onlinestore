package io.teamchallenge.mapper;

import io.teamchallenge.dto.security.SignUpRequestDto;
import io.teamchallenge.entity.User;
import io.teamchallenge.enumerated.Role;
import io.teamchallenge.service.JwtService;
import org.modelmapper.AbstractConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * This class maps a {@link SignUpRequestDto} object to an {@link User} entity.
 */
@Component
public class SignUpRequestDtoToUserMapper extends AbstractConverter<SignUpRequestDto, User> {
    private final PasswordEncoder passwordEncoder;
    private final AddressDtoToAddressMapper addressDtoToAddressMapper;
    private final JwtService jwtService;

    /**
     * Constructor to autowire dependencies.
     *
     * @param passwordEncoder {@link PasswordEncoder} bean used for password hashing.
     * @param addressDtoToAddressMapper {@link AddressDtoToAddressMapper} bean used to convert address.
     * @param jwtService {@link JwtService} bean used for generating refresh token key.
     */
    @Autowired
    public SignUpRequestDtoToUserMapper(PasswordEncoder passwordEncoder,
                                        AddressDtoToAddressMapper addressDtoToAddressMapper, JwtService jwtService) {
        this.passwordEncoder = passwordEncoder;
        this.addressDtoToAddressMapper = addressDtoToAddressMapper;
        this.jwtService = jwtService;
    }

    /**
     * Converts a {@link SignUpRequestDto} object to an {@link User} entity.
     *
     * @param signUpRequestDto The {@link SignUpRequestDto} object to be converted.
     * @return The converted {@link User} object.
     */
    protected User convert(SignUpRequestDto signUpRequestDto) {
        return User.builder()
            .email(signUpRequestDto.getEmail())
            .password(passwordEncoder.encode(signUpRequestDto.getPassword()))
            .phoneNumber(signUpRequestDto.getPhoneNumber())
            .firstName(signUpRequestDto.getFirstName())
            .lastName(signUpRequestDto.getLastName())
            .role(Role.ROLE_USER)
            .address(addressDtoToAddressMapper.convert(signUpRequestDto.getAddress()))
            .refreshTokenKey(jwtService.generateTokenKey())
            .build();
    }
}
