package org.example.ecommerce.mapper;

import org.example.ecommerce.dto.response.OrderItemResponseDto;
import org.example.ecommerce.dto.response.OrderResponseDto;
import org.example.ecommerce.entity.Order;
import org.example.ecommerce.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(source = "user.id", target = "userId")
    OrderResponseDto toDto(Order order);

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    OrderItemResponseDto itemToDto(OrderItem orderItem);
}
