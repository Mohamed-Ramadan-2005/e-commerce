package org.example.ecommerce.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
public class OrderRequestDto {
    @NotNull(message = "User ID is required")
    private Long userId;
    @NotEmpty(message = "Order must contain atleast one item")
    @Valid
    private List<OrderItemRequestDto> items;
}
