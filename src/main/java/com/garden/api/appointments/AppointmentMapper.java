package com.garden.api.appointments;

import com.garden.api.clients.Client;
import com.garden.api.clients.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppointmentMapper {

    private final ClientRepository clientRepository;

    public Appointment toEntity(AppointmentRequest request) {
        Appointment appointment = new Appointment();

        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found with ID: " + request.getClientId()));

        appointment.setClient(client);
        appointment.setServiceType(request.getServiceType());
        appointment.setDate(request.getDate());
        appointment.setTime(request.getTime());
        appointment.setAddress(request.getAddress());
        appointment.setAssignedStaff(request.getAssignedStaff());
        appointment.setStatus(request.getStatus() != null ? request.getStatus() : AppointmentStatus.SCHEDULED);

        return appointment;
    }

    public AppointmentResponse toResponse(Appointment appointment) {
        AppointmentResponse response = new AppointmentResponse();
        response.setId(appointment.getId());
        response.setClientId(appointment.getClient().getId());
        response.setClientName(appointment.getClient().getName());
        response.setServiceType(appointment.getServiceType());
        response.setDate(appointment.getDate());
        response.setTime(appointment.getTime());
        response.setAddress(appointment.getAddress());
        response.setAssignedStaff(appointment.getAssignedStaff());
        response.setStatus(appointment.getStatus());
        return response;
    }

    public void updateEntity(Appointment appointment, AppointmentRequest request) {
        if (request.getClientId() != null) {
            Client client = clientRepository.findById(request.getClientId())
                    .orElseThrow(() -> new RuntimeException("Client not found with ID: " + request.getClientId()));
            appointment.setClient(client);
        }
        appointment.setServiceType(request.getServiceType());
        appointment.setDate(request.getDate());
        appointment.setTime(request.getTime());
        appointment.setAddress(request.getAddress());
        appointment.setAssignedStaff(request.getAssignedStaff());
        appointment.setStatus(request.getStatus());
    }
}
