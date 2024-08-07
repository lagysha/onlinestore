package io.teamchallenge.mapper;

import io.teamchallenge.dto.PostAddressDto;
import io.teamchallenge.entity.PostAddress;
import org.modelmapper.AbstractConverter;

public class PostAddressMapper extends AbstractConverter<PostAddressDto, PostAddress> {
    /**
     * Converts a {@link PostAddressDto} object to a {@link PostAddress} object.
     * This method creates a new {@link PostAddress} object using the values from the given {@link PostAddressDto}.
     * Specifically, it sets the {@code city} and {@code department} fields.
     *
     * @param source the {@link PostAddressDto} object to convert.
     * @return a new {@link PostAddress} object populated with the values from the {@code source}.
     */
    @Override
    protected PostAddress convert(PostAddressDto source) {
        return PostAddress.builder()
            .city(source.getCity())
            .department(source.getDepartment())
            .build();
    }
}
