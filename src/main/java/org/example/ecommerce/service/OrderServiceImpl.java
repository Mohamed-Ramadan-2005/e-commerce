package org.example.ecommerce.service;

import lombok.Setter;
import org.example.ecommerce.dto.OrderItemRequestDto;
import org.example.ecommerce.dto.OrderRequestDto;
import org.example.ecommerce.dto.OrderResponseDto;
import org.example.ecommerce.entity.Order;
import org.example.ecommerce.entity.OrderItem;
import org.example.ecommerce.entity.Product;
import org.example.ecommerce.entity.User;
import org.example.ecommerce.enumrate.OrderStatus;
import org.example.ecommerce.error.BusinessException;
import org.example.ecommerce.mapper.OrderMapper;
import org.example.ecommerce.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;
    @Autowired
    private OrderMapper orderMapper;
    @Override
    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto dto,String username) {
        User user = userService.getUserEntityByUsername(username);
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        double totalAmount = 0;
        for(OrderItemRequestDto itemDto : dto.getItems()){
            Product product = productService.getProductEntityById(itemDto.getProductId());
            if(product.getStockQuantity() < itemDto.getQuantity()){
                throw new BusinessException("Insufficient stock for product: " + product.getName());
            }
            product.setStockQuantity(product.getStockQuantity()-itemDto.getQuantity());
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(itemDto.getQuantity());
            orderItem.setPriceAtPurchase(product.getPrice());
            order.addOrderItem(orderItem);
            totalAmount += product.getPrice() * itemDto.getQuantity();
        }
        order.setTotalAmount(totalAmount);
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDto(savedOrder);
    }

    @Override
    public List<OrderResponseDto> getUserOrders(Long userId) {
        User user = userService.getUserEntityById(userId);
        List<Order> orders = orderRepository.findByUserId(user.getId());
        return orders.stream().map(orderMapper::toDto).collect(Collectors.toList());
    }
    @Override
    public List<OrderResponseDto> getUserOrdersByUserName(String username) {
        User user = userService.getUserEntityByUsername(username);
        List<Order> orders = orderRepository.findByUserId(user.getId());
        return orders.stream().map(orderMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<OrderResponseDto> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

}
