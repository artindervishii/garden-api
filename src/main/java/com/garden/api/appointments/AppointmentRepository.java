package com.garden.api.appointments;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    Page<Appointment> findAllByStatus(AppointmentStatus status, Pageable pageable);
    Page<Appointment> findAllByClientId(Long clientId, Pageable pageable);

}
