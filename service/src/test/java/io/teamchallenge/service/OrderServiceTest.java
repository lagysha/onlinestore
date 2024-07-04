package io.teamchallenge.service;

import io.teamchallenge.dto.order.OrderRequestDto;
import io.teamchallenge.entity.Address;
import io.teamchallenge.entity.Order;
import io.teamchallenge.entity.Product;
import io.teamchallenge.entity.User;
import io.teamchallenge.exception.ConflictException;
import io.teamchallenge.exception.ForbiddenException;
import io.teamchallenge.repository.CartItemRepository;
import io.teamchallenge.repository.OrderRepository;
import io.teamchallenge.repository.ProductRepository;
import io.teamchallenge.repository.UserRepository;
import io.teamchallenge.util.Utils;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private OrderService orderService;

    @Test
    void createWithPrincipalTest() {
        OrderRequestDto orderRequestDto = Utils.getOrderRequestDtoCourier();
        String email = "test@mail.com";
        Principal principal = () -> email;
        Order order = Utils.getUnsavedOrder();
        User user = Utils.getUser();
        Product product = Utils.getProduct();
        List<Long> productIds = List.of(product.getId());
        List<Product> products = List.of(product);
        Order savedOrder = Utils.getOrder();

        when(modelMapper.map(orderRequestDto.getAddress(), Address.class)).thenReturn(Utils.getAddress());
        when(userRepository.findUserByEmail(email)).thenReturn(Optional.ofNullable(user));
        when(userRepository.existsByEmail(orderRequestDto.getEmail())).thenReturn(false);
        when(userRepository.existsByPhoneNumber(orderRequestDto.getPhoneNumber())).thenReturn(false);
        when(productRepository.findAllById(productIds)).thenReturn(products);
        when(orderRepository.save(order)).thenReturn(savedOrder);

        Long actual = orderService.create(orderRequestDto, principal);

        assertEquals(savedOrder.getId(), actual);
        verify(modelMapper).map(orderRequestDto.getAddress(), Address.class);
        verify(userRepository).findUserByEmail(email);
        verify(userRepository).existsByEmail(orderRequestDto.getEmail());
        verify(userRepository).existsByPhoneNumber(orderRequestDto.getPhoneNumber());
        verify(productRepository).findAllById(productIds);
        verify(orderRepository).save(order);
    }

    @Test
    void createWithPrincipalThrowsForbiddenExceptionWhenUserUsesEmailOfAnotherUserTest() {
        OrderRequestDto orderRequestDto = Utils.getOrderRequestDtoCourier();
        String email = "test@mail.com";
        Principal principal = () -> email;
        User user = Utils.getUser();

        when(modelMapper.map(orderRequestDto.getAddress(), Address.class)).thenReturn(Utils.getAddress());
        when(userRepository.findUserByEmail(email)).thenReturn(Optional.ofNullable(user));
        when(userRepository.existsByEmail(orderRequestDto.getEmail())).thenReturn(true);

        assertThrows(ConflictException.class, ()-> orderService.create(orderRequestDto, principal));
        verify(modelMapper).map(orderRequestDto.getAddress(), Address.class);
        verify(userRepository).findUserByEmail(email);
        verify(userRepository).existsByEmail(orderRequestDto.getEmail());
    }

    @Test
    void createWithPrincipalThrowsForbiddenExceptionWhenUserUsesPhoneNumberOfAnotherUserTest() {
        OrderRequestDto orderRequestDto = Utils.getOrderRequestDtoCourier();
        String email = "test@mail.com";
        Principal principal = () -> email;
        User user = Utils.getUser();

        when(userRepository.findUserByEmail(email)).thenReturn(Optional.ofNullable(user));
        when(userRepository.existsByEmail(orderRequestDto.getEmail())).thenReturn(false);
        when(userRepository.existsByPhoneNumber(orderRequestDto.getPhoneNumber())).thenReturn(true);

        assertThrows(ForbiddenException.class, ()-> orderService.create(orderRequestDto, principal));
        verify(userRepository).findUserByEmail(email);
        verify(userRepository).existsByEmail(orderRequestDto.getEmail());
        verify(userRepository).existsByPhoneNumber(orderRequestDto.getPhoneNumber());
    }

    @Test
    void createWithoutPrincipalTest() {
        OrderRequestDto orderRequestDto = Utils.getOrderRequestDtoCourier();
        Principal principal = null;
        Order order = Utils.getUnsavedOrder();
        Product product = Utils.getProduct();
        List<Long> productIds = List.of(product.getId());
        List<Product> products = List.of(product);
        Order savedOrder = Utils.getOrder();

        when(modelMapper.map(orderRequestDto.getAddress(), Address.class)).thenReturn(Utils.getAddress());
        when(userRepository.existsByEmail(orderRequestDto.getEmail())).thenReturn(false);
        when(userRepository.existsByPhoneNumber(orderRequestDto.getPhoneNumber())).thenReturn(false);
        when(productRepository.findAllById(productIds)).thenReturn(products);
        when(orderRepository.save(order)).thenReturn(savedOrder);

        Long actual = orderService.create(orderRequestDto, principal);

        assertEquals(savedOrder.getId(), actual);
        verify(modelMapper).map(orderRequestDto.getAddress(), Address.class);
        verify(userRepository).existsByEmail(orderRequestDto.getEmail());
        verify(userRepository).existsByPhoneNumber(orderRequestDto.getPhoneNumber());
        verify(productRepository).findAllById(productIds);
        verify(orderRepository).save(order);
    }

    @Test
    void createWithoutPrincipalThrowsForbiddenExceptionWhenUserUsesEmailOfAnotherUserTest() {
        OrderRequestDto orderRequestDto = Utils.getOrderRequestDtoCourier();
        Principal principal = null;

        when(userRepository.existsByEmail(orderRequestDto.getEmail())).thenReturn(true);

        assertThrows(ForbiddenException.class, ()-> orderService.create(orderRequestDto, principal));
        verify(userRepository).existsByEmail(orderRequestDto.getEmail());
    }

    @Test
    void createWithoutPrincipalThrowsForbiddenExceptionWhenUserUsesPhoneNumberOfAnotherUserTest() {
        OrderRequestDto orderRequestDto = Utils.getOrderRequestDtoCourier();
        Principal principal = null;

        when(userRepository.existsByEmail(orderRequestDto.getEmail())).thenReturn(false);
        when(userRepository.existsByPhoneNumber(orderRequestDto.getPhoneNumber())).thenReturn(true);

        assertThrows(ForbiddenException.class, ()-> orderService.create(orderRequestDto, principal));
        verify(userRepository).existsByEmail(orderRequestDto.getEmail());
        verify(userRepository).existsByPhoneNumber(orderRequestDto.getPhoneNumber());
    }
}