package com.garden.api.role;

import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    public RoleResponse map(Role role) {
        return RoleResponse.builder()
                .id(role.getId())
                .name(role.getName())
                .build();
    }
}