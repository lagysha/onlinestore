package io.teamchallenge.mapper;

import io.teamchallenge.dto.address.AddressDto;
import io.teamchallenge.entity.Address;
import io.teamchallenge.exception.NotFoundException;
import io.teamchallenge.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static io.teamchallenge.constant.ExceptionMessage.COUNTRY_NOT_FOUND_BY_NAME;

/**
 * Mapper for {@link AddressDto}.
 * @author Denys Liubchenko
 */
@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AddressDtoToAddressMapper extends AbstractConverter<AddressDto, Address> {
    private final CountryRepository countryRepository;

    /**
     * Converts an {@link AddressDto} object to an {@link Address} entity.
     *
     * @param addressDto {@link AddressDto} object to be converted.
     * @return {@link Address} entity.
     * @throws NotFoundException if the country specified in the addressDto is not found.
     */
    @Override
    protected Address convert(AddressDto addressDto) {
        return Address.builder()
            .addressLine(addressDto.getAddressLine())
            .city(addressDto.getCity())
            .postalCode(addressDto.getPostalCode())
            .country(countryRepository.findByName(addressDto.getCountryName())
                .orElseThrow(() -> new NotFoundException(COUNTRY_NOT_FOUND_BY_NAME + addressDto.getCountryName())))
            .build();
    }
}