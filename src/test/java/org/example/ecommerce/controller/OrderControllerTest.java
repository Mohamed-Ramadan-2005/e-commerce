package org.example.ecommerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.ecommerce.dto.request.OrderItemRequestDto;
import org.example.ecommerce.dto.request.OrderRequestDto;
import org.example.ecommerce.security.JwtUtils;
import org.example.ecommerce.service.impelmentation.CustomerUserDetailsService;
import org.example.ecommerce.service.interfaces.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OrderController.class)
@AutoConfigureMockMvc(addFilters = false) // Bypass security filters for the test
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private JwtUtils jwtUtils;

    @MockitoBean
    private CustomerUserDetailsService customerUserDetailsService;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    private OrderRequestDto requestDto;
    private Principal mockPrincipal;

    @BeforeEach
    void setUp() {
        OrderItemRequestDto itemDto = new OrderItemRequestDto();
        itemDto.setProductId(10L);
        itemDto.setQuantity(2);

        requestDto = new OrderRequestDto();
        requestDto.setItems(List.of(itemDto));
        mockPrincipal = new UsernamePasswordAuthenticationToken("testuser", "password");
    }

    @Test
    void createOrder_ShouldReturn201Created() throws Exception {
        when(orderService.createOrder(any(OrderRequestDto.class), eq("testuser"))).thenReturn(null);

        mockMvc.perform(post("/orders")
                        .principal(mockPrincipal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated());
    }
}