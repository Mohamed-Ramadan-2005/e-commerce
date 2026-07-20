package org.example.ecommerce.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.example.ecommerce.entity.enums.OrderStatus;

@Getter
@Setter
public class OrderStatusUpdateRequestDto {

    @NotNull(message = "Order status cannot be null")
    private OrderStatus status;

}