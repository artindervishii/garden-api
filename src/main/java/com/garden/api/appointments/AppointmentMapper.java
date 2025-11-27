package com.garden.api.appointments;

import com.garden.api.category.Category;
import com.garden.api.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppointmentMapper {

    private final CategoryRepository categoryRepository;

    public Appointment toEntity(AppointmentRequest request) {
        Appointment appointment = new Appointment();

        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(() -> new RuntimeException("Category not found with ID: " + request.getCategoryId()));
        appointment.setCategory(category);
        appointment.setClientName(request.getClientName());
        appointment.setClientPhoneNumber(request.getClientPhoneNumber());
        appointment.setDateTime(request.getDateTime());
        appointment.setAddress(request.getAddress());
        appointment.setStatus(request.getStatus() != null ? request.getStatus() : AppointmentStatus.SCHEDULED);

        return appointment;
    }

    public AppointmentResponse toResponse(Appointment appointment) {
        AppointmentResponse response = new AppointmentResponse();
        response.setId(appointment.getId());
        response.setClientName(appointment.getClientName());
        response.setClientPhoneNumber(appointment.getClientPhoneNumber());
        if(appointment.getCategory() != null){
            response.setCategoryId(appointment.getCategory().getId());
        }
        response.setDateTime(appointment.getDateTime());
        response.setAddress(appointment.getAddress());
        response.setStatus(appointment.getStatus());
        return response;
    }

    public void updateEntity(Appointment appointment, AppointmentRequest request) {

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found with ID: " + request.getCategoryId()));
            appointment.setCategory(category);
        }
        appointment.setClientName(request.getClientName());
        appointment.setClientPhoneNumber(request.getClientPhoneNumber());
        appointment.setDateTime(request.getDateTime());
        appointment.setAddress(request.getAddress());
        appointment.setStatus(request.getStatus());
    }
}
