package org.example.ecommerce.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;
@Setter
@Getter
public class UserResponseDto {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private Set<String> roles;
}
