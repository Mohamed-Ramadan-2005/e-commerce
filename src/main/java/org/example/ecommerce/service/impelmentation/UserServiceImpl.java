package org.example.ecommerce.service.impelmentation;

import lombok.RequiredArgsConstructor;
import org.example.ecommerce.dto.request.UserRequestDto;
import org.example.ecommerce.dto.response.UserResponseDto;
import org.example.ecommerce.entity.Role;
import org.example.ecommerce.entity.User;
import org.example.ecommerce.error.BusinessException;
import org.example.ecommerce.mapper.UserMapper;
import org.example.ecommerce.repository.RoleRepository;
import org.example.ecommerce.repository.UserRepository;
import org.example.ecommerce.service.interfaces.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

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
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.addRole(roleRepository.findByName("USER")
                .orElseThrow(()->new BusinessException("Role not found")));
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }
    @Override
    public User getUserEntityByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(()->new BusinessException("User not found with username: " + username));
    }
    @Override
    public User getUserEntityById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(()->new BusinessException("User not found with id: " + userId));
    }
    @Override
    public void promoteUserToAdmin(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("User not found with ID: " + userId));
        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseThrow(() -> new BusinessException("System Error: ADMIN role does not exist."));
        if (user.getRoles().contains(adminRole)) {
            throw new BusinessException("This user already has ADMIN privileges.");
        }
        user.addRole(adminRole);
        user.removeRole(roleRepository.findByName("USER")
                .orElseThrow(() -> new BusinessException("System Error: USER role does not exist.")));
        userRepository.save(user);
    }
}
