package io.teamchallenge.validator;

import io.teamchallenge.annotation.ValidOrderRequest;
import io.teamchallenge.dto.order.OrderRequestDto;
import io.teamchallenge.enumerated.DeliveryMethod;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class OrderRequestValidator implements ConstraintValidator<ValidOrderRequest, OrderRequestDto> {
    /**
     * Validates the delivery method and addresses in an {@link OrderRequestDto}.
     *
     * @param value the {@code OrderRequestDto} object to validate.
     * @param context the context in which the constraint is evaluated.
     * @return {@code true} if the delivery method is valid, {@code false} otherwise.
     */
    @Override
    public boolean isValid(OrderRequestDto value, ConstraintValidatorContext context) {
        return (value.getDeliveryMethod().equals(DeliveryMethod.COURIER) && value.getAddress() != null
                && value.getPostAddress() == null)
               || ((value.getDeliveryMethod().equals(DeliveryMethod.NOVA)
                    || value.getDeliveryMethod().equals(DeliveryMethod.UKRPOSHTA)) && value.getPostAddress() != null
                   && value.getAddress() == null);
    }
}
