package io.teamchallenge.service;

import io.teamchallenge.constant.ExceptionMessage;
import io.teamchallenge.dto.cart.CartItemResponseDto;
import io.teamchallenge.dto.cart.CartResponseDto;
import io.teamchallenge.entity.cartitem.CartItem;
import io.teamchallenge.entity.cartitem.CartItemId;
import io.teamchallenge.exception.AlreadyExistsException;
import io.teamchallenge.exception.NotFoundException;
import io.teamchallenge.repository.CartItemRepository;
import io.teamchallenge.repository.ProductRepository;
import io.teamchallenge.repository.UserRepository;
import io.teamchallenge.service.impl.CartItemService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static io.teamchallenge.constant.AppConstant.MAX_NUMBER_OF_UNIQUE_PRODUCTS_IN_CART;
import static io.teamchallenge.util.Utils.getCartItem;
import static io.teamchallenge.util.Utils.getCartItemResponseDto;
import static io.teamchallenge.util.Utils.getCartResponseDto;
import static io.teamchallenge.util.Utils.getPatchRequestDto;
import static io.teamchallenge.util.Utils.getProduct;
import static io.teamchallenge.util.Utils.getUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CartItemServiceTest {

    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CartItemService cartItemService;

    @Test
    void getByUserIdTest() {
        Long userId = 1L;
        Pageable pageable = PageRequest.of(0, MAX_NUMBER_OF_UNIQUE_PRODUCTS_IN_CART);
        var page = new PageImpl<>(List.of(
            new CartItemId(1L, 1L)),
            pageable, 2);
        CartItem cartItem = getCartItem();
        var cartItemResponseDto = getCartItemResponseDto();
        List<CartItem> cartItems = List.of(cartItem);
        var expected = getCartResponseDto();

        when(modelMapper.map(cartItem, CartItemResponseDto.class))
            .thenReturn(cartItemResponseDto);
        when(cartItemRepository
            .findCartItemIdsByUserId(userId, pageable)).thenReturn(page);
        when(cartItemRepository
            .findAllByIdWithImagesAndProducts(page.getContent(), Sort.by("createdAt")))
            .thenReturn(cartItems);

        var actual = cartItemService.getByUserId(userId);

        assertEquals(expected, actual);
        verify(cartItemRepository).findCartItemIdsByUserId(eq(userId), eq(pageable));
        verify(cartItemRepository).findAllByIdWithImagesAndProducts(eq(page.getContent()),
            eq(Sort.by("createdAt")));
        verify(modelMapper).map(eq(cartItem), eq(CartItemResponseDto.class));
    }

    @Test
    void createTest() {
        Long userId = 1L;
        Long productId = 1L;
        CartItemId cartItemId = new CartItemId(userId, productId);
        var product = Optional.of(getProduct());
        var user = getUser();
        var cartItem = CartItem
            .builder()
            .id(cartItemId)
            .user(user)
            .product(product.get())
            .quantity(1)
            .build();
        var savedCartItem = getCartItem();
        var expected = getCartItemResponseDto();

        when(cartItemRepository.findById(cartItemId)).thenReturn(Optional.empty());
        when(productRepository.findByIdWithImages(productId)).thenReturn(product);
        when(userRepository.getReferenceById(userId)).thenReturn(user);
        when(cartItemRepository.persist(cartItem)).thenReturn(savedCartItem);
        when(modelMapper.map(savedCartItem, CartItemResponseDto.class)).thenReturn(expected);

        var actual = cartItemService.create(userId, productId);


        assertEquals(expected, actual);
        verify(cartItemRepository).findById(eq(cartItemId));
        verify(productRepository).findByIdWithImages(eq(productId));
        verify(userRepository).getReferenceById(eq(userId));
        verify(cartItemRepository).persist(eq(cartItem));
        verify(modelMapper).map(eq(savedCartItem), eq(CartItemResponseDto.class));
    }

    @Test
    void createThrowsNotFoundExceptionWhenNonExistingProductIdTest() {
        Long userId = 1L;
        Long productId = 1L;
        CartItemId cartItemId = new CartItemId(userId, productId);

        when(cartItemRepository.findById(cartItemId)).thenReturn(Optional.empty());
        when(productRepository.findByIdWithImages(productId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> cartItemService.create(userId, productId),
            ExceptionMessage.PRODUCT_NOT_FOUND_BY_ID.formatted(1L));

        verify(cartItemRepository).findById(eq(cartItemId));
        verify(productRepository).findByIdWithImages(eq(productId));
    }

    @Test
    void createThrowsAlreadyExistsExceptionWhenCartItemWithThisIdAlreadyExistsTest() {
        Long userId = 1L;
        Long productId = 1L;
        CartItemId cartItemId = new CartItemId(userId, productId);

        when(cartItemRepository.findById(cartItemId)).thenReturn(Optional.of(getCartItem()));

        assertThrows(AlreadyExistsException.class, () -> cartItemService.create(userId, productId),
            ExceptionMessage.CARTITEM_ALREADY_EXISTS.formatted(1L));

        verify(cartItemRepository).findById(eq(cartItemId));
    }

    @Test
    void deleteByCartItemIdTest() {
        Long userId = 1L;
        Long productId = 1L;
        CartItemId cartItemId = new CartItemId(userId, productId);

        when(cartItemRepository.findById(cartItemId)).thenReturn(Optional.of(getCartItem()));
        doNothing().when(cartItemRepository).deleteById(cartItemId);

        cartItemService.deleteByCartItemId(userId, productId);

        verify(cartItemRepository).findById(eq(cartItemId));
        verify(cartItemRepository).deleteById(eq(cartItemId));
    }

    @Test
    void deleteByCartItemIdThrowsNotFoundExceptionWhenNonExistingCartItemIdTest() {
        Long userId = 1L;
        Long productId = 1L;
        CartItemId cartItemId = new CartItemId(userId, productId);

        when(cartItemRepository.findById(cartItemId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> cartItemService.deleteByCartItemId(userId, productId),
            ExceptionMessage.CARTITEM_NOT_FOUND_BY_ID.formatted(1L));

        verify(cartItemRepository).findById(eq(cartItemId));
    }

    @Test
    void patchTest() {
        Long userId = 1L;
        Long productId = 1L;
        CartItemId cartItemId = new CartItemId(userId, productId);
        var retrievedCartItem = getCartItem();
        var expected = getCartItemResponseDto();
        var request = getPatchRequestDto();

        when(cartItemRepository.findById(cartItemId)).thenReturn(Optional.of(retrievedCartItem));
        when(modelMapper.map(retrievedCartItem, CartItemResponseDto.class)).thenReturn(expected);

        var actual = cartItemService.patch(userId, productId, request);

        assertEquals(expected, actual);

        verify(cartItemRepository).findById(eq(cartItemId));
        verify(modelMapper).map(eq(retrievedCartItem), eq(CartItemResponseDto.class));
    }

    @Test
    void patchThrowsNotFoundExceptionWhenNonExistingCartItemIdTest() {
        Long userId = 1L;
        Long productId = 1L;
        CartItemId cartItemId = new CartItemId(userId, productId);
        var request = getPatchRequestDto();

        when(cartItemRepository.findById(cartItemId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> cartItemService.patch(userId, productId, request),
            ExceptionMessage.CARTITEM_NOT_FOUND_BY_ID.formatted(1L));

        verify(cartItemRepository).findById(eq(cartItemId));
    }
}
