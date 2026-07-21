package org.example.ecommerce.service;

import org.example.ecommerce.dto.request.UserRequestDto;
import org.example.ecommerce.entity.Role;
import org.example.ecommerce.entity.User;
import org.example.ecommerce.mapper.UserMapper;
import org.example.ecommerce.repository.RoleRepository;
import org.example.ecommerce.repository.UserRepository;
import org.example.ecommerce.service.implementation.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    private UserRequestDto registrationDto;
    private User existingUser;

    @BeforeEach
    void setUp() {
        registrationDto = new UserRequestDto();
        registrationDto.setUsername("developer");
        registrationDto.setPassword("RawPassword123");
        registrationDto.setEmail("dev@store.com");
        registrationDto.setFirstName("Backend");
        registrationDto.setLastName("Engineer");

        existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("developer");
        existingUser.setPassword("EncodedPassword123");

        existingUser.addRole(new Role(1L, "USER", new HashSet<>()));
    }

    @Test
    void registerUser_ShouldEncodePasswordAndSave() {
        when(userRepository.existsByUsername("developer")).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("HashedPasswordXYZ");

        User mappedUser = new User();
        mappedUser.setUsername("developer");
        mappedUser.setEmail("dev@store.com");
        mappedUser.setPassword("RawPassword123");
        when(userMapper.toEntity(any(UserRequestDto.class))).thenReturn(mappedUser);

        Role userRole = new Role(1L, "USER", new HashSet<>());
        when(roleRepository.findByName(anyString())).thenReturn(Optional.of(userRole));

        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        userService.registerUser(registrationDto);

        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertEquals("developer", savedUser.getUsername());
        assertEquals("HashedPasswordXYZ", savedUser.getPassword());
        assertEquals("dev@store.com", savedUser.getEmail());

        boolean hasUserRole = savedUser.getRoles().stream()
                .anyMatch(role -> role.getName().equals("USER"));
        assertTrue(hasUserRole);
    }

    @Test
    void registerUser_WhenUsernameExists_ShouldThrowException() {
        when(userRepository.existsByUsername("developer")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> {
            userService.registerUser(registrationDto);
        });

        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void promoteUser_WhenUserExists_ShouldUpdateRoleToAdmin() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(existingUser));

        Role adminRole = new Role(2L, "ADMIN", new HashSet<>());
        when(roleRepository.findByName("ADMIN")).thenReturn(Optional.of(adminRole));

        Role exactUserRoleInstance = existingUser.getRoles().iterator().next();
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(exactUserRoleInstance));

        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        userService.promoteUserToAdmin(1L);

        verify(userRepository).save(userCaptor.capture());
        User updatedUser = userCaptor.getValue();

        boolean hasAdminRole = updatedUser.getRoles().stream()
                .anyMatch(role -> role.getName().equals("ADMIN"));
        assertTrue(hasAdminRole);

        boolean hasUserRole = updatedUser.getRoles().stream()
                .anyMatch(role -> role.getName().equals("USER"));
        assertFalse(hasUserRole);
    }
    @Test
    void promoteUser_WhenUserDoesNotExist_ShouldThrowException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            userService.promoteUserToAdmin(99L);
        });

        verify(userRepository, never()).save(any(User.class));
    }
}