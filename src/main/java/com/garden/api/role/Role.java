package com.garden.api.role;

import com.garden.api.common.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "roles")
public class Role extends AbstractEntity {

    @Column(unique = true, nullable = false)
    private String name;

}
