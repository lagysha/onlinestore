package io.teamchallenge.mapper;

import io.teamchallenge.dto.security.SignUpResponseDto;
import io.teamchallenge.entity.User;
import org.modelmapper.AbstractConverter;

public class UserToSignUpResponseDtoMapper extends AbstractConverter<User, SignUpResponseDto> {
    @Override
    protected SignUpResponseDto convert(User source) {
        return SignUpResponseDto.builder()
            .id(source.getId())
            .email(source.getEmail())
            .firstName(source.getFirstName())
            .lastName(source.getLastName())
            .build();
    }
}
