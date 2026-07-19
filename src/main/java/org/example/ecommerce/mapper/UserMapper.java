package org.example.ecommerce.mapper;

import org.example.ecommerce.dto.UserRequestDto;
import org.example.ecommerce.dto.UserResponseDto;
import org.example.ecommerce.entity.Role;
import org.example.ecommerce.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Set;
import java.util.stream.Collectors;
@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "orders", ignore = true)
    User toEntity(UserRequestDto dto);
    @Mapping(source = "roles", target = "roles", qualifiedByName = "mapRolesToStrings")
    UserResponseDto toDto(User user);

    @Named("mapRolesToStrings")
    default Set<String> mapRolesToStrings(Set<Role> roles) {
        if (roles == null) {
            return null;
        }
        return roles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }
}
