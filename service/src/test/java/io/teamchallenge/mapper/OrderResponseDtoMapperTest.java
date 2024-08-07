package io.teamchallenge.mapper;

import io.teamchallenge.dto.address.AddressDto;
import io.teamchallenge.dto.order.OrderItemResponseDto;
import io.teamchallenge.dto.order.OrderResponseDto;
import io.teamchallenge.dto.product.ShortProductResponseDto;
import io.teamchallenge.entity.Address;
import io.teamchallenge.entity.Order;
import io.teamchallenge.entity.Product;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.teamchallenge.util.Utils.getAddressDto;
import static io.teamchallenge.util.Utils.getOrder;
import static io.teamchallenge.util.Utils.getShortProductResponseDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderResponseDtoMapperTest {
    @Mock
    private ShortProductResponseDtoMapper shortProductResponseDtoMapper;

    @Mock
    private AddressDtoMapper addressDtoMapper;

    @Mock
    private PostAddressDtoMapper postAddressDtoMapper;

    @InjectMocks
    private OrderResponseDtoMapper orderResponseDtoMapper;

    @Test
    void convertTest() {
        // Arrange
        Order source = getOrder();
        ShortProductResponseDto shortProductResponseDto = getShortProductResponseDto();
        AddressDto addressDto = getAddressDto();
        OrderResponseDto orderResponseDto = OrderResponseDto.builder()
            .id(source.getId())
            .email(source.getContactInfo().getEmail())
            .firstName(source.getContactInfo().getFirstName())
            .lastName(source.getContactInfo().getLastName())
            .phoneNumber(source.getContactInfo().getPhoneNumber())
            .deliveryMethod(source.getDeliveryMethod())
            .deliveryStatus(source.getDeliveryStatus())
            .isPaid(source.getIsPaid())
            .orderItems(source.getOrderItems().stream().map(orderItem -> OrderItemResponseDto.builder()
                .quantity(orderItem.getQuantity())
                .price(orderItem.getPrice())
                .shortProductResponseDto(shortProductResponseDto)
                .build()).toList())
            .createdAt(source.getCreatedAt())
            .total(source.getOrderItems().stream()
                .map(orderItem -> orderItem.getPrice()
                    .multiply(BigDecimal.valueOf(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add))
            .address(addressDto)
            .build();

        when(shortProductResponseDtoMapper.convert(any(Product.class))).thenReturn(shortProductResponseDto);
        when(addressDtoMapper.convert(any(Address.class))).thenReturn(addressDto);

        // Act
        OrderResponseDto result = orderResponseDtoMapper.convert(source);

        // Assert
        assertEquals(orderResponseDto, result);
    }
}