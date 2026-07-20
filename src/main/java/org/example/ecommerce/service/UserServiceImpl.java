package org.example.ecommerce.service;

import org.example.ecommerce.dto.UserRequestDto;
import org.example.ecommerce.dto.UserResponseDto;
import org.example.ecommerce.entity.User;
import org.example.ecommerce.error.BusinessException;
import org.example.ecommerce.mapper.UserMapper;
import org.example.ecommerce.repository.RoleRepository;
import org.example.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    UserMapper userMapper;
    @Autowired
    private RoleRepository roleRepository;
    @Override
    @Transactional
    public UserResponseDto registerUser(UserRequestDto dto) {
        if(userRepository.existsByUsername(dto.getUsername())){
            throw new BusinessException("Username already exists");
        }
        if(userRepository.existsByEmail(dto.getEmail())){
            throw new BusinessException("Email already exists");
        }
        User user = userMapper.toEntity(dto);
        user.addRole(roleRepository.findByName("USER")
                .orElseThrow(()->new BusinessException("Role not found")));
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }
    @Override
    public User getUserEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(()->new BusinessException("User not found with id: " + id));
    }
}
