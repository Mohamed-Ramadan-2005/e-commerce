package org.example.ecommerce.service;

import org.example.ecommerce.dto.OrderRequestDto;
import org.example.ecommerce.dto.OrderResponseDto;

import java.util.List;

public interface OrderService {
    OrderResponseDto createOrder(OrderRequestDto dto);
    List<OrderResponseDto> getUserOrders(Long userId);
    List<OrderResponseDto> getAllOrders();
}
