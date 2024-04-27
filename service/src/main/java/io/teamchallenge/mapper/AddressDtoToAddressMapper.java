package io.teamchallenge.mapper;

import io.teamchallenge.dto.address.AddressDto;
import io.teamchallenge.entity.Address;
import io.teamchallenge.exception.NotFoundException;
import io.teamchallenge.repository.CountryRepository;
import org.modelmapper.AbstractConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static io.teamchallenge.constant.ExceptionMessage.COUNTRY_NOT_FOUND_BY_NAME;

@Component
public class AddressDtoToAddressMapper extends AbstractConverter<AddressDto, Address> {
    private final CountryRepository countryRepository;

    @Autowired
    public AddressDtoToAddressMapper(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }
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
