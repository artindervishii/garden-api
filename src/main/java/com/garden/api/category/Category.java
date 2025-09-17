package com.garden.api.category;

import com.garden.api.common.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@ToString
@Table(name = "categories")
public class Category extends AbstractEntity {

    @Column(name = "name", unique = true, nullable = false)
    private String name;

}
