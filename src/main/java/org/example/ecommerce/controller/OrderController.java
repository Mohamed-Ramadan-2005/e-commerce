package org.example.ecommerce.controller;

import jakarta.validation.Valid;
import org.example.ecommerce.dto.OrderRequestDto;
import org.example.ecommerce.dto.OrderResponseDto;
import org.example.ecommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    OrderService orderService;
    @GetMapping("user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderResponseDto>> getUserOrders(@PathVariable Long userId) {
        List<OrderResponseDto> orders = orderService.getUserOrders(userId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderResponseDto>> getAllOrders() {
        List<OrderResponseDto> orders = orderService.getAllOrders();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OrderResponseDto> createOrder(@Valid @RequestBody OrderRequestDto orderRequestDto, Authentication authentication) {
        String username = authentication.getName();
        OrderResponseDto createdOrder = orderService.createOrder(orderRequestDto,username);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }
    @GetMapping("/my-orders")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<OrderResponseDto>> getMyOrders(Authentication authentication) {
        String username = authentication.getName();
        List<OrderResponseDto> orders = orderService.getUserOrdersByUserName(username);
        return ResponseEntity.ok(orders);
    }
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderResponseDto> updateOrderStatus(
            @PathVariable("id") Long orderId,
            @Valid @RequestBody org.example.ecommerce.dto.request.OrderStatusUpdateRequestDto requestDto) {
        OrderResponseDto updatedOrder = orderService.updateOrderStatus(orderId, requestDto.getStatus());
        return ResponseEntity.ok(updatedOrder);
    }
}
