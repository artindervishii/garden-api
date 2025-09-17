package com.garden.api.appointments;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AppointmentRequest {
    private Long clientId; // link to Client
    private String serviceType;
    private LocalDate date;
    private LocalTime time;
    private String address;
    private String assignedStaff;
    private AppointmentStatus status;
}
