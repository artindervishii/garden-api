package com.garden.api.clients;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class ClientResponse {
    private Long id;
    private String name;
    private String address;
    private String email;
    private String phoneNumber;
    private ClientStatus status;
    private BigDecimal totalSpent;
    private LocalDate lastContactDate;
    private List<String> projectTitles;
}
