package com.garden.api.role;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

@Value
@Builder
@Data
public class RoleResponse {

    Long id;

    String name;
}