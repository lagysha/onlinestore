package io.teamchallenge.mapper;

import io.teamchallenge.dto.address.AddressDto;
import io.teamchallenge.entity.Address;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.teamchallenge.util.Utils.getAddress;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class AddressDtoMapperTest {
    @InjectMocks
    private AddressDtoMapper addressDtoMapper;

    @Test
    void convertTest() {
        Address source = getAddress();
        AddressDto expected = AddressDto.builder()
            .city(source.getCity())
            .countryName(source.getCountry().getName())
            .postalCode(source.getPostalCode())
            .addressLine(source.getAddressLine())
            .build();
        AddressDto convert = addressDtoMapper.convert(source);

        assertEquals(expected, convert);
    }
}

