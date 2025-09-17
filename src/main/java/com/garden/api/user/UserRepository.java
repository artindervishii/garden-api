package com.garden.api.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    User findByResetPasswordToken(String token);

    Page<User> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = :roleName ORDER BY u.createdAt DESC")
    Page<User> findAllByRoleNameOrdered(@Param("roleName") String roleName, Pageable pageable);

    boolean existsByName(String name);


    @Query("""
       SELECT u
       FROM User u
       JOIN u.roles r
       WHERE (:roleName IS NULL OR r.name = :roleName)
         AND (:search IS NULL OR :search = '' OR 
              LOWER(u.name) LIKE LOWER(CONCAT('%', :search, '%')) OR
              LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%')))
       ORDER BY u.createdAt DESC
       """)
    Page<User> findAllByRoleAndName(
            @Param("roleName") String roleName,
            @Param("search") String search,
            Pageable pageable
    );


}
