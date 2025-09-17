package com.garden.api.user.dtos;

import com.garden.api.role.RoleMapper;
import com.garden.api.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final RoleMapper roleMapper;

    public UserResponse mapToDto(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .roles(user.getRoles().stream().map(roleMapper::map).collect(Collectors.toList()))
                .build();
    }
}
