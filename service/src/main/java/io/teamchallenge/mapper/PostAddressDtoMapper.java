package io.teamchallenge.mapper;

import io.teamchallenge.dto.PostAddressDto;
import io.teamchallenge.entity.PostAddress;
import io.teamchallenge.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Mapper for {@link PostAddressDto}.
 * @author Denys Liubchenko
 */
@Component
public class PostAddressDtoMapper extends AbstractConverter<PostAddress, PostAddressDto> {
    /**
     * Converts an {@link PostAddress} object to an {@link PostAddressDto} entity.
     *
     * @param source {@link PostAddress} object to be converted.
     * @return {@link PostAddressDto} entity.
     */
    @Override
    protected PostAddressDto convert(PostAddress source) {
        return PostAddressDto.builder()
            .city(source.getCity())
            .department(source.getDepartment())
            .build();
    }
}