package com.garden.api.appointments;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AppointmentResponse {
    private Long id;
    private Long clientId;
    private String clientName;
    private String serviceType;
    private LocalDate date;
    private LocalTime time;
    private String address;
    private String assignedStaff;
    private AppointmentStatus status;
}
