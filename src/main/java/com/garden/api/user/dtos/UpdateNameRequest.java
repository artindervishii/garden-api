package com.garden.api.user.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateNameRequest {

    @NotBlank(message = "Name cannot be blank")
    private String name;
}
