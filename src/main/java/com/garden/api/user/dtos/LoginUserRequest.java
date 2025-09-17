package com.garden.api.user.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;


@Data
public class LoginUserRequest {

    @NotEmpty(message = "The email address is required.")
    @Email(message = "The email address is invalid.", flags = {Pattern.Flag.CASE_INSENSITIVE})
    private String email;

    @NotBlank(message = "User must have a password")
    @Size(min = 8, max = 15)
    private String password;

}
