package com.garden.api.clients;

import com.garden.api.projects.Project;
import com.garden.api.common.AbstractEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.BatchSize;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "clients")
public class Client extends AbstractEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private String address;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = true)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClientStatus status = ClientStatus.ACTIVE;

    @OneToMany(mappedBy = "client")
    @BatchSize(size = 50)
    private List<Project> projects = new ArrayList<>();

    @Column(name = "total_spent")
    private BigDecimal totalSpent = BigDecimal.ZERO;

    @Column(name = "last_contact_date")
    private LocalDate lastContactDate;

}
