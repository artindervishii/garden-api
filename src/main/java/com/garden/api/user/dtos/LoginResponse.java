package com.garden.api.user.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {

    private String accessToken;
    private String refreshToken;
    private long expiresIn;

}