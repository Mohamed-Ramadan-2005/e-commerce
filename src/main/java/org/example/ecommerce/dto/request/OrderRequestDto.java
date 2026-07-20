package org.example.ecommerce.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
public class OrderRequestDto {
    @NotEmpty(message = "Order must contain atleast one item")
    @Valid
    private List<OrderItemRequestDto> items;
}
