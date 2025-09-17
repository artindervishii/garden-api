package com.garden.api.user.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Value;


@Value
public class ForgotPasswordRequest {

    @NotBlank(message = "User must have an email")
    @Email(message = "Invalid email")
    String email;
}
