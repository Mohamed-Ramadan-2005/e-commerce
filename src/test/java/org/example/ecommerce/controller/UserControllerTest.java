package org.example.ecommerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.ecommerce.dto.request.UserRequestDto;
import org.example.ecommerce.security.JwtUtils;
import org.example.ecommerce.service.implementation.CustomerUserDetailsService;
import org.example.ecommerce.service.interfaces.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)

public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtUtils jwtUtils;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private CustomerUserDetailsService customerUserDetailsService;

    private UserRequestDto registrationDto;

    @BeforeEach
    void setUp() {
        registrationDto = new UserRequestDto();
        registrationDto.setUsername("developer");
        registrationDto.setPassword("RawPassword123");
        registrationDto.setEmail("dev@store.com");
        registrationDto.setFirstName("Backend");
        registrationDto.setLastName("Engineer");
    }

    @Test
    void registerUser_ShouldReturn201Created() throws Exception {
        when(userService.registerUser(any(UserRequestDto.class))).thenReturn(null);

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void promoteUserToAdmin_ShouldReturn200Ok() throws Exception {
        doNothing().when(userService).promoteUserToAdmin(anyLong());

        mockMvc.perform(patch("/users/1/promote")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}