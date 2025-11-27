package com.garden.api.appointments;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentResponse {
    private Long id;
    private String clientName;
    private String clientPhoneNumber;
    private Long categoryId;
    private LocalDateTime dateTime;
    private String address;
    private AppointmentStatus status;
}
