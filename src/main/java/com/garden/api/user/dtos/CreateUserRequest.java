package com.garden.api.user.dtos;

import lombok.Value;

import java.util.List;


@Value
public class CreateUserRequest {

    String name;

    String email;

    String password;

    List<String> role;


}
