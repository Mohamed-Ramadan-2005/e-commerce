package org.example.ecommerce.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
@Setter
@Getter
public class OrderResponseDto {
    private Long id;
    private Long userId;
    private LocalDateTime orderDate;
    private String status;
    private Double totalAmount;
    private List<OrderItemResponseDto> orderItems;
}
