package org.example.ecommerce.service.interfaces;

import org.example.ecommerce.dto.request.OrderRequestDto;
import org.example.ecommerce.dto.response.OrderResponseDto;
import org.example.ecommerce.entity.enums.OrderStatus;

import java.util.List;

public interface OrderService {
    OrderResponseDto createOrder(OrderRequestDto dto,String username);
    List<OrderResponseDto> getUserOrders(Long userId);
    List<OrderResponseDto> getUserOrdersByUserName(String username);
    List<OrderResponseDto> getAllOrders();
    OrderResponseDto updateOrderStatus(Long orderId, OrderStatus newStatus);
}
