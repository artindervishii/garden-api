package com.garden.api.projects;

import com.garden.api.category.Category;
import com.garden.api.common.AbstractEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.BatchSize;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "projects")
public class Project extends AbstractEntity {

    @Column(nullable = false)
    private String title;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "project_category"
            , joinColumns = @JoinColumn(name = "projects_id")
            , inverseJoinColumns = @JoinColumn(name = "categories_id"))
    @BatchSize(size = 100)
    private List<Category> categories = new ArrayList<>();

    @Column(nullable = false)
    private String image;

    @Column(columnDefinition = "TEXT")
    private String description;

    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProjectStatus status = ProjectStatus.Scheduled;

}
