package io.teamchallenge.mapper;

import io.teamchallenge.dto.address.AddressDto;
import io.teamchallenge.dto.order.ShortOrderResponseDto;
import io.teamchallenge.entity.Address;
import io.teamchallenge.entity.Order;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.teamchallenge.util.Utils.getAddressDto;
import static io.teamchallenge.util.Utils.getOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ShortOrderResponseDtoMapperTest {
    @Mock
    private AddressDtoMapper addressDtoMapper;

    @InjectMocks
    private ShortOrderResponseDtoMapper shortOrderResponseDtoMapper;

    @Test
    void convertTest() {
        // Arrange
        Order source = getOrder();
        AddressDto addressDto = getAddressDto();
        ShortOrderResponseDto shortOrderResponseDto = ShortOrderResponseDto.builder()
            .id(source.getId())
            .email(source.getContactInfo().getEmail())
            .firstName(source.getContactInfo().getFirstName())
            .lastName(source.getContactInfo().getLastName())
            .phoneNumber(source.getContactInfo().getPhoneNumber())
            .deliveryMethod(source.getDeliveryMethod())
            .deliveryStatus(source.getDeliveryStatus())
            .isPaid(source.getIsPaid())
            .createdAt(source.getCreatedAt())
            .total(source.getOrderItems().stream()
                .map(orderItem -> orderItem.getPrice()
                    .multiply(BigDecimal.valueOf(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add))
            .address(addressDto)
            .build();

        when(addressDtoMapper.convert(any(Address.class))).thenReturn(addressDto);

        // Act
        ShortOrderResponseDto result = shortOrderResponseDtoMapper.convert(source);

        // Assert
        assertEquals(shortOrderResponseDto, result);
    }
}