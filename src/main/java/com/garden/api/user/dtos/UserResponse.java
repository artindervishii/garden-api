package com.garden.api.user.dtos;

import com.garden.api.role.RoleResponse;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class UserResponse {

    Long id;

    List<RoleResponse> roles;

    String name;

    String email;

}
