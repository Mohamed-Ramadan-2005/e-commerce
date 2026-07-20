package org.example.ecommerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.ecommerce.dto.request.ProductRequestDto;
import org.example.ecommerce.dto.response.ProductResponseDto;
import org.example.ecommerce.service.impelmentation.CustomerUserDetailsService;
import org.example.ecommerce.service.interfaces.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.example.ecommerce.security.JwtUtils;
@WebMvcTest(controllers = ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();
    @MockitoBean
    private ProductService productService;
    @MockitoBean
    private JwtUtils jwtUtils;
    @MockitoBean
    private CustomerUserDetailsService customerUserDetailsService;
    private ProductRequestDto requestDto;
    private ProductResponseDto responseDto;

    @BeforeEach
    void setUp() {
        requestDto = new ProductRequestDto();
        requestDto.setName("Gaming Mouse");
        requestDto.setPrice(50.0);
        requestDto.setStockQuantity(100);

        responseDto = new ProductResponseDto();
        responseDto.setId(1L);
        responseDto.setName("Gaming Mouse");
        responseDto.setPrice(50.0);
        responseDto.setStockQuantity(100);
    }

    @Test
    void createProduct_ShouldReturn201CreatedAndProduct() throws Exception {
        when(productService.createProduct(any(ProductRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated()) // Expect HTTP 201
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Gaming Mouse"))
                .andExpect(jsonPath("$.price").value(50.0));
    }

    @Test
    void getProductById_WhenExists_ShouldReturn200OkAndProduct() throws Exception {
        when(productService.getProductById(1L)).thenReturn(responseDto);

        mockMvc.perform(get("/products/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Expect HTTP 200
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Gaming Mouse"));
    }
}