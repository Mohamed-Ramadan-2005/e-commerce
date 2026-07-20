package org.example.ecommerce.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderItemRequestDto {
    @NotNull(message = "Product ID is required")
    private Long productId;
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be greater than or equal to 1")
    private Integer quantity;

}
