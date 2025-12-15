package com.garden.api.projects;

import com.garden.api.category.Category;
import com.garden.api.clients.Client;
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

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "project_category"
            , joinColumns = @JoinColumn(name = "projects_id")
            , inverseJoinColumns = @JoinColumn(name = "categories_id"))
    @BatchSize(size = 100)
    private List<Category> categories = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "project_images", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "image_url")
    private List<String> images = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "project_videos", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "video_url")
    private List<String> videos = new ArrayList<>();

    @Column(name = "before_image_url")
    private String beforeImage;

    @Column(name = "after_image_url")
    private String afterImage;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProjectStatus status = ProjectStatus.Scheduled;

    private BigDecimal price;

    @Column(nullable = true)
    private Integer displayOrder;

}
