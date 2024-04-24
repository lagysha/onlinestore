package io.teamchallenge.service;

import io.teamchallenge.dto.CartItemResponseDto;
import io.teamchallenge.dto.PageableDto;
import io.teamchallenge.entity.cartitem.CartItem;
import io.teamchallenge.entity.cartitem.CartItemId;
import io.teamchallenge.repository.CartItemRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CartItemService {
    private final CartItemRepository cartItemRepository;
    private final ModelMapper modelMapper;

    public PageableDto<CartItemResponseDto> getByUserId(Long userId, Pageable pageable) {
        Page<CartItemId> retrievedCartItemsIds = cartItemRepository.findCartItemIdsByUserId(userId,pageable);
        List<CartItemResponseDto> content = cartItemRepository
            .findAllByIdWithImagesAndProducts(retrievedCartItemsIds.getContent())
            .stream()
            .map(o -> modelMapper.map(o, CartItemResponseDto.class))
            .toList();
        return new PageableDto<>(
            content,
            retrievedCartItemsIds.getTotalElements(),
            retrievedCartItemsIds.getPageable().getPageNumber(),
            retrievedCartItemsIds.getTotalPages());
    }
}
