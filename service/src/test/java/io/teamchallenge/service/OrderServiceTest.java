package io.teamchallenge.service;

import io.teamchallenge.dto.order.OrderFilterDto;
import io.teamchallenge.dto.order.OrderRequestDto;
import io.teamchallenge.dto.order.OrderResponseDto;
import io.teamchallenge.dto.order.ShortOrderResponseDto;
import io.teamchallenge.dto.pageable.PageableDto;
import io.teamchallenge.dto.user.UserVO;
import io.teamchallenge.entity.Address;
import io.teamchallenge.entity.Order;
import io.teamchallenge.entity.Product;
import io.teamchallenge.entity.User;
import io.teamchallenge.enumerated.DeliveryStatus;
import io.teamchallenge.exception.ConflictException;
import io.teamchallenge.exception.ForbiddenException;
import io.teamchallenge.repository.CartItemRepository;
import io.teamchallenge.repository.CustomOrderRepository;
import io.teamchallenge.repository.OrderRepository;
import io.teamchallenge.repository.ProductRepository;
import io.teamchallenge.repository.UserRepository;
import io.teamchallenge.service.impl.OrderService;
import io.teamchallenge.util.Utils;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static io.teamchallenge.util.Utils.getOrder;
import static io.teamchallenge.util.Utils.getOrderResponseDto;
import static io.teamchallenge.util.Utils.getShortOrderResponseDto;
import static io.teamchallenge.util.Utils.getShortOrderResponseDtoPageableDto;
import static io.teamchallenge.util.Utils.getUserVO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private CustomOrderRepository customOrderRepository;
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
        Order savedOrder = getOrder();

        when(modelMapper.map(orderRequestDto.getAddress(), Address.class)).thenReturn(Utils.getAddress());
        when(userRepository.findUserByEmail(email)).thenReturn(Optional.ofNullable(user));
        when(userRepository.existsByEmail(orderRequestDto.getEmail())).thenReturn(false);
        when(userRepository.existsByPhoneNumber(orderRequestDto.getPhoneNumber())).thenReturn(false);
        when(productRepository.findAllById(productIds)).thenReturn(products);
        when(orderRepository.save(order)).thenReturn(savedOrder);
        doNothing().when(cartItemRepository).deleteByUserId(user.getId());

        Long actual = orderService.create(orderRequestDto, principal);

        assertEquals(savedOrder.getId(), actual);
        verify(modelMapper).map(orderRequestDto.getAddress(), Address.class);
        verify(userRepository).findUserByEmail(email);
        verify(userRepository).existsByEmail(orderRequestDto.getEmail());
        verify(userRepository).existsByPhoneNumber(orderRequestDto.getPhoneNumber());
        verify(productRepository).findAllById(productIds);
        verify(orderRepository).save(order);
        verify(cartItemRepository).deleteByUserId(user.getId());
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

        assertThrows(ConflictException.class, () -> orderService.create(orderRequestDto, principal));
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

        assertThrows(ForbiddenException.class, () -> orderService.create(orderRequestDto, principal));
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
        Order savedOrder = getOrder();

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

        assertThrows(ForbiddenException.class, () -> orderService.create(orderRequestDto, principal));
        verify(userRepository).existsByEmail(orderRequestDto.getEmail());
    }

    @Test
    void createWithoutPrincipalThrowsForbiddenExceptionWhenUserUsesPhoneNumberOfAnotherUserTest() {
        OrderRequestDto orderRequestDto = Utils.getOrderRequestDtoCourier();
        Principal principal = null;

        when(userRepository.existsByEmail(orderRequestDto.getEmail())).thenReturn(false);
        when(userRepository.existsByPhoneNumber(orderRequestDto.getPhoneNumber())).thenReturn(true);

        assertThrows(ForbiddenException.class, () -> orderService.create(orderRequestDto, principal));
        verify(userRepository).existsByEmail(orderRequestDto.getEmail());
        verify(userRepository).existsByPhoneNumber(orderRequestDto.getPhoneNumber());
    }

    @Test
    void getByIdTest() {
        Long orderId = 1L;
        Order order = getOrder();
        UserVO user = getUserVO();
        OrderResponseDto orderResponseDto = getOrderResponseDto();

        when(orderRepository.findByIdFetchData(orderId)).thenReturn(Optional.of(order));
        when(userRepository.findVOByOrdersId(orderId)).thenReturn(Optional.of(user));
        when(modelMapper.map(order, OrderResponseDto.class)).thenReturn(orderResponseDto);

        OrderResponseDto result = orderService.getById(orderId);

        assertEquals(orderResponseDto, result);
        assertEquals(user, result.getUser());
        verify(orderRepository).findByIdFetchData(orderId);
        verify(userRepository).findVOByOrdersId(orderId);
        verify(productRepository).findAllByIdWithImages(anyList());
        verify(modelMapper).map(order, OrderResponseDto.class);
    }

    @Test
    void setDeliveryStatusTest() {
        Long orderId = 1L;
        DeliveryStatus status = DeliveryStatus.COMPLETED;
        Order order = getOrder();
        order.setDeliveryStatus(DeliveryStatus.PROCESSING);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        orderService.setDeliveryStatus(orderId, status);

        assertEquals(status, order.getDeliveryStatus());
        verify(orderRepository).findById(orderId);
    }

    @Test
    void setDeliveryStatusThrowsConflictExceptionTest() {
        Long orderId = 1L;
        DeliveryStatus status = DeliveryStatus.COMPLETED;
        Order order = getOrder();
        order.setDeliveryStatus(DeliveryStatus.COMPLETED);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        assertThrows(ConflictException.class, () -> orderService.setDeliveryStatus(orderId, status));
        verify(orderRepository).findById(orderId);
    }

    @Test
    void cancelOrderTest() {
        Long orderId = 1L;
        Long userId = 1L;
        Order order = getOrder();

        when(userRepository.userHasOrderWithId(userId, orderId)).thenReturn(true);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        orderService.cancelOrder(orderId, userId);

        verify(userRepository).userHasOrderWithId(userId, orderId);
        verify(orderRepository).findById(orderId);
    }

    @Test
    void cancelOrderThrowsForbiddenExceptionTest() {
        Long orderId = 1L;
        Long userId = 1L;

        when(userRepository.userHasOrderWithId(userId, orderId)).thenReturn(false);

        assertThrows(ForbiddenException.class, () -> orderService.cancelOrder(orderId, userId));
        verify(userRepository).userHasOrderWithId(userId, orderId);
    }

    @Test
    void getAllByFilterTest() {
        OrderFilterDto filterParametersDto = new OrderFilterDto();
        Pageable pageable = PageRequest.of(0, 10);
        Order order = getOrder();
        Page<Order> page = new PageImpl<>(Collections.singletonList(order), pageable,1);
        ShortOrderResponseDto shortOrderResponseDto = getShortOrderResponseDto();
        PageableDto<ShortOrderResponseDto> expectedResponse = getShortOrderResponseDtoPageableDto();

        when(customOrderRepository.findAllByFilterParameters(filterParametersDto, pageable)).thenReturn(page);
        when(modelMapper.map(order, ShortOrderResponseDto.class)).thenReturn(shortOrderResponseDto);

        PageableDto<ShortOrderResponseDto> result = orderService.getAllByFilter(filterParametersDto, pageable);

        assertEquals(expectedResponse, result);
        verify(customOrderRepository).findAllByFilterParameters(filterParametersDto, pageable);
        verify(modelMapper).map(order, ShortOrderResponseDto.class);
    }

}