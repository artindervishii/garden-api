package com.garden.api.appointments;

import com.garden.api.category.Category;
import com.garden.api.common.AbstractEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "appointments")
public class Appointment extends AbstractEntity {

    @Column(nullable = false)
    private String clientName;

    @Column(nullable = false)
    private String clientPhoneNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = true)
    private Category category;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    @Column(nullable = false)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status = AppointmentStatus.SCHEDULED;
}
