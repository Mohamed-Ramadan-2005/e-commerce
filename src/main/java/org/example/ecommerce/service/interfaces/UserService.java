package org.example.ecommerce.service.interfaces;

import org.example.ecommerce.dto.request.UserRequestDto;
import org.example.ecommerce.dto.response.UserResponseDto;
import org.example.ecommerce.entity.User;

public interface UserService {
    UserResponseDto registerUser(UserRequestDto dto);
    User getUserEntityByUsername(String username);
    User getUserEntityById(Long userId);
    void promoteUserToAdmin(Long userId);
}
