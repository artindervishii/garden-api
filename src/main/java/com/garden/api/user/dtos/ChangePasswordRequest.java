package com.garden.api.user.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Value;

@Value
public class ChangePasswordRequest {

    @NotBlank(message = "User must enter the old password")
    String oldPassword;

    @NotBlank(message = "User must enter a new password")
    String newPassword;
}