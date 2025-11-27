package com.garden.api.appointments;

import com.garden.api.exceptions.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AppointmentService {

    private final AppointmentRepository repository;
    private final AppointmentMapper mapper;

    public Long addAppointment(AppointmentRequest request) {
        Appointment appointment = mapper.toEntity(request);
        repository.save(appointment);
        return appointment.getId();
    }

    public void updateAppointment(Long id, AppointmentRequest request) {
        Appointment appointment = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment with ID: " + id + " not found"));
        mapper.updateEntity(appointment, request);
        repository.save(appointment);
    }

    public void deleteAppointment(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Appointment with ID: " + id + " not found");
        }
        repository.deleteById(id);
    }

    public AppointmentResponse findAppointmentById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment with ID: " + id + " not found"));
    }

    public Page<AppointmentResponse> findAllByStatus(AppointmentStatus status, Pageable pageable) {
        Page<Appointment> appointments;
        if (status != null) {
            appointments = repository.findAllByStatus(status, pageable);
        } else {
            appointments = repository.findAll(pageable);
        }
        return appointments.map(mapper::toResponse);
    }

}
