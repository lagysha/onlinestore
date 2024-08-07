package io.teamchallenge.mapper;

import io.teamchallenge.dto.address.AddressDto;
import io.teamchallenge.entity.Address;
import io.teamchallenge.entity.Country;
import io.teamchallenge.repository.CountryRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.teamchallenge.util.Utils.getAddress;
import static io.teamchallenge.util.Utils.getAddressDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AddressMapperTest {
    @InjectMocks
    private AddressMapper addressMapper;

    @Mock
    private CountryRepository countryRepository;

    @Test
    void convertTest() {
        AddressDto addressDto = getAddressDto();
        Country country = getAddress().getCountry();
        when(countryRepository.findByName(addressDto.getCountryName())).thenReturn(Optional.ofNullable(country));
        Address expected = Address.builder()
            .addressLine(addressDto.getAddressLine())
            .city(addressDto.getCity())
            .postalCode(addressDto.getPostalCode())
            .country(country)
            .build();

        Address convert = addressMapper.convert(addressDto);

        assertEquals(expected, convert);
    }
}