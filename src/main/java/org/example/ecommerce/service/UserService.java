package org.example.ecommerce.service;

import org.example.ecommerce.dto.UserRequestDto;
import org.example.ecommerce.dto.UserResponseDto;
import org.example.ecommerce.entity.User;

public interface UserService {
    UserResponseDto registerUser(UserRequestDto dto);
    User getUserEntityById(Long id);
}
