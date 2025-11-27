package com.garden.api.reviews;

import com.garden.api.category.Category;
import com.garden.api.common.AbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "reviews")
public class Review extends AbstractEntity {

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String location;

    @Column(nullable = true)
    private String email;

    @Column(nullable = false)
    private int stars;

    @Column(nullable = false, length = 2000)
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    private ReviewStatus status;
}
