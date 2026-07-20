package org.example.ecommerce.service;

import org.example.ecommerce.dto.OrderRequestDto;
import org.example.ecommerce.dto.OrderResponseDto;
import org.example.ecommerce.enumrate.OrderStatus;

import java.util.List;

public interface OrderService {
    OrderResponseDto createOrder(OrderRequestDto dto,String username);
    List<OrderResponseDto> getUserOrders(Long userId);
    List<OrderResponseDto> getUserOrdersByUserName(String username);
    List<OrderResponseDto> getAllOrders();
    OrderResponseDto updateOrderStatus(Long orderId, OrderStatus newStatus);
}
