package io.teamchallenge.mapper;

import io.teamchallenge.dto.security.SignUpResponseDto;
import io.teamchallenge.entity.User;
import org.modelmapper.AbstractConverter;

/**
 * Mapper for {@link User}.
 * @author Denys Liubchenko
 */
public class UserToSignUpResponseDtoMapper extends AbstractConverter<User, SignUpResponseDto> {
    /**
     * Converts a {@link User} entity to a {@link SignUpResponseDto} object.
     *
     * @param source the {@link User} entity to be converted.
     * @return a {@link SignUpResponseDto} object representing the converted data.
     */
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
