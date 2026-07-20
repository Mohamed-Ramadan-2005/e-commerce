package org.example.ecommerce.service;

import org.example.ecommerce.dto.request.OrderItemRequestDto;
import org.example.ecommerce.dto.request.OrderRequestDto;
import org.example.ecommerce.entity.Order;
import org.example.ecommerce.entity.Product;
import org.example.ecommerce.entity.User;
import org.example.ecommerce.mapper.OrderMapper;
import org.example.ecommerce.repository.OrderRepository;
import org.example.ecommerce.service.impelmentation.OrderServiceImpl;
import org.example.ecommerce.service.interfaces.ProductService;
import org.example.ecommerce.service.interfaces.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductService productService;

    @Mock
    private UserService userService;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Captor
    private ArgumentCaptor<Order> orderCaptor;

    private User mockUser;
    private Product mockProduct;
    private OrderRequestDto requestDto;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testuser");

        mockProduct = new Product();
        mockProduct.setId(10L);
        mockProduct.setName("Mechanical Keyboard");
        mockProduct.setPrice(100.0);
        mockProduct.setStockQuantity(50);

        OrderItemRequestDto itemDto = new OrderItemRequestDto();
        itemDto.setProductId(10L);
        itemDto.setQuantity(2);

        requestDto = new OrderRequestDto();
        requestDto.setItems(List.of(itemDto));
    }

    @Test
    void createOrder_ShouldCalculateTotalAndSaveOrder() {
        when(userService.getUserEntityByUsername(anyString())).thenReturn(mockUser);
        when(productService.getProductEntityById(10L)).thenReturn(mockProduct);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        orderService.createOrder(requestDto, mockUser.getUsername());

        verify(orderRepository).save(orderCaptor.capture());
        Order savedOrder = orderCaptor.getValue();

        assertNotNull(savedOrder);
        assertEquals(mockUser, savedOrder.getUser());
        assertEquals(200.0, savedOrder.getTotalAmount());
        assertEquals(1, savedOrder.getOrderItems().size());
        assertEquals(48, mockProduct.getStockQuantity());
    }

    @Test
    void createOrder_WhenInsufficientStock_ShouldThrowException() {
        requestDto.getItems().get(0).setQuantity(100);

        when(userService.getUserEntityByUsername(anyString())).thenReturn(mockUser);
        when(productService.getProductEntityById(10L)).thenReturn(mockProduct);

        assertThrows(RuntimeException.class, () -> {
            orderService.createOrder(requestDto, mockUser.getUsername());
        });

        verify(orderRepository, never()).save(any(Order.class));
    }
}