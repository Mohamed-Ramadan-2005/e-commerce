package org.example.ecommerce.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ecommerce.dto.response.JwtResponseDto;
import org.example.ecommerce.dto.request.LoginRequestDto;
import org.example.ecommerce.dto.request.UserRequestDto;
import org.example.ecommerce.dto.response.UserResponseDto;
import org.example.ecommerce.security.JwtUtils;
import org.example.ecommerce.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> registerUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        UserResponseDto userResponseDto = userService.registerUser(userRequestDto);
        return new ResponseEntity<>(userResponseDto,HttpStatus.CREATED);
    }
    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> loginUser(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        return ResponseEntity.ok(new JwtResponseDto(jwt));
    }
    @PatchMapping("/{userId}/promote")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> promoteToAdmin(@PathVariable Long userId) {
        userService.promoteUserToAdmin(userId);
        return ResponseEntity.ok("User promoted to ADMIN successfully.");
    }
}
