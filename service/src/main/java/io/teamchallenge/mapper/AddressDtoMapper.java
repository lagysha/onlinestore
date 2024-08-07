package io.teamchallenge.mapper;

import io.teamchallenge.dto.address.AddressDto;
import io.teamchallenge.entity.Address;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

/**
 * Mapper for {@link AddressDto}.
 * @author Denys Liubchenko
 */
@Component
public class AddressDtoMapper extends AbstractConverter<Address, AddressDto> {
    /**
     * Converts an {@link Address} object to an {@link AddressDto} entity.
     *
     * @param source {@link Address} object to be converted.
     * @return {@link AddressDto} entity.
     */
    @Override
    protected AddressDto convert(Address source) {
        return AddressDto.builder()
            .city(source.getCity())
            .countryName(source.getCountry().getName())
            .postalCode(source.getPostalCode())
            .addressLine(source.getAddressLine())
            .build();
    }
}