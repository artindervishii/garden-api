package com.garden.api.user.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Value;


@Value
public class ResetPasswordRequest {

    @Size(min = 6,message = "New password must be at least 6 characters long")
    @NotBlank
    String newPassword;

    @NotBlank
    String confirmNewPassword;
}