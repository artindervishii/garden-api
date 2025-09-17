package com.garden.api.user.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DeleteUserRequest {

    @NotBlank(message = "Password cannot be blank!")
    private String password;

}
