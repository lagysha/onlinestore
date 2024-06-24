package io.teamchallenge.mapper;

import io.teamchallenge.dto.order.OrderResponseDto;
import io.teamchallenge.entity.Order;
import org.modelmapper.AbstractConverter;

public class OrderToOrderResponseDto extends AbstractConverter<Order, OrderResponseDto> {
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
            .build();
        return orderResponseDto;
    }
}
