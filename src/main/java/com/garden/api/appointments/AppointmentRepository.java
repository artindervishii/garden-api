package com.garden.api.appointments;

import com.garden.api.category.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    Page<Appointment> findAllByStatus(AppointmentStatus status, Pageable pageable);

    List<Appointment> findByDateTimeBetween(LocalDateTime start, LocalDateTime end);

    Appointment[] findByCategory(Category category);
}
