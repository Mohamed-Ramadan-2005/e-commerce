package org.example.ecommerce.service;

import org.example.ecommerce.dto.UserRequestDto;
import org.example.ecommerce.dto.UserResponseDto;
import org.example.ecommerce.entity.User;

public interface UserService {
    UserResponseDto registerUser(UserRequestDto dto);
    User getUserEntityByUsername(String username);
    User getUserEntityById(Long userId);
    void promoteUserToAdmin(Long userId);
}
