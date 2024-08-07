package io.teamchallenge.mapper;

import io.teamchallenge.dto.order.OrderItemResponseDto;
import io.teamchallenge.dto.order.OrderResponseDto;
import io.teamchallenge.entity.Order;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderResponseDtoMapper extends AbstractConverter<Order, OrderResponseDto> {
    private final ShortProductResponseDtoMapper shortProductResponseDtoMapper;
    private final AddressDtoMapper addressDtoMapper;
    private final PostAddressDtoMapper postAddressDtoMapper;

    @Override
    protected OrderResponseDto convert(Order source) {
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
                .shortProductResponseDto(shortProductResponseDtoMapper.convert(orderItem.getProduct()))
                .build()).toList())
            .createdAt(source.getCreatedAt())
            .total(source.getOrderItems().stream()
                .map(orderItem -> orderItem.getPrice()
                    .multiply(BigDecimal.valueOf(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add))
            .build();
        if (source.getAddress() != null) {
            orderResponseDto.setAddress(addressDtoMapper.convert(source.getAddress()));
        }
        if (source.getPostAddress() != null) {
            orderResponseDto.setPostAddress(postAddressDtoMapper.convert(source.getPostAddress()));
        }
        return orderResponseDto;
    }
}
