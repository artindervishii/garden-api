package com.garden.api.user.dtos;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterUserRequest {

    @NotEmpty(message = "The email address is required.")
    @Email(message = "The email address is invalid.", flags = {Pattern.Flag.CASE_INSENSITIVE})
    private String email;

    @NotBlank(message = "User must have a password")
    @Size(min = 8, max = 15)
    private String password;

    @NotBlank(message = "Full name can't be empty")
    @Pattern(regexp = "^[a-zA-Z]+(?:\\s+[a-zA-Z]+)*$", message = "Full name must contain only alphabetic characters")
    @Size(min = 3, max = 15, message = "Full name must be at least 3 characters long")
    private String name;


}
