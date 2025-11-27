package com.garden.api.category;

import com.garden.api.common.AbstractEntity;
import com.garden.api.projects.Project;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "categories")
public class Category extends AbstractEntity {

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToMany(mappedBy = "categories",cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Project> projects;
}
