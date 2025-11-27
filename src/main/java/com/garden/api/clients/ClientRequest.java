package com.garden.api.clients;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ClientRequest {
    private String name;
    private String address;
    private String email;
    private String phoneNumber;
    private ClientStatus status;
    private LocalDate lastContactDate;
}
