package com.garden.api.projects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {



    @Modifying
    @Transactional
    @Query(value = "DELETE FROM project_category pc WHERE pc.categories_id = :categoryId", nativeQuery = true)
    void deleteCategoryFromProjects(@Param("categoryId") Long categoryId);

    @Query("SELECT p FROM Project p WHERE (:status IS NULL OR p.status = :status)")
    Page<Project> findAllByStatus(@Param("status") ProjectStatus status, Pageable pageable);

    Page<Project> findByCategories_Id(Long categoryId, Pageable pageable);
}
