package com.garden.api.projects;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class ProjectController {

    public static final String BASE_PATH_V1 = "/v1/projects";

    private final ProjectService projectService;
    private final ProjectMapper mapper;

    @PostMapping(value = BASE_PATH_V1)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> addProject(@Valid @RequestBody ProjectRequest projectRequest) {
        Long projectId = projectService.addProject(projectRequest);
        return ResponseEntity.ok(projectId);
    }

    @PutMapping(value = BASE_PATH_V1 + "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateProject(@PathVariable Long id, @Valid @RequestBody ProjectRequest projectRequest) {
        projectService.updateProject(id,projectRequest);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @DeleteMapping(value = BASE_PATH_V1 +"/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(BASE_PATH_V1 + "/{id}")
    public ResponseEntity<ProjectResponse> getProjectById(@PathVariable Long id) {
        ProjectResponse projectResponse = projectService.findProjectById(id);
        return new ResponseEntity<>(projectResponse, HttpStatus.OK);
    }

    @GetMapping(BASE_PATH_V1)
    public Page<ProjectResponse> getAllProjects(
            @RequestParam(value = "status", required = false) ProjectStatus status,
            @PageableDefault(size = 20, sort = {"createdAt"}, direction = Sort.Direction.DESC)  Pageable pageable
    ){
        return projectService.findAllByStatus(status, pageable);
    }

    @GetMapping(BASE_PATH_V1 + "/category/{categoryId}")
    public Page<ProjectResponse> getProjectsByCategory(
            @PathVariable Long categoryId,
            @PageableDefault(size = 20, sort = {"createdAt"}, direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return projectService.findProjectsByCategory(categoryId, pageable);
    }

    @PostMapping(BASE_PATH_V1 + "/{projectId}/upload-image")
    public ResponseEntity<String> uploadProjectImage(
            @PathVariable Long projectId,
            @RequestParam("file") MultipartFile file) {
        String url = projectService.uploadProjectImage(projectId, file);
        return ResponseEntity.ok(url);
    }


    @PostMapping(BASE_PATH_V1 + "/{projectId}/upload-video")
    public ResponseEntity<String> uploadProjectVideo(
            @PathVariable Long projectId,
            @RequestParam("file") MultipartFile file) {
        String url = projectService.uploadProjectVideo(projectId, file);
        return ResponseEntity.ok(url);
    }

    @DeleteMapping(BASE_PATH_V1 + "/{projectId}/images")
    public ResponseEntity<?> deleteProjectImage(
            @PathVariable Long projectId,
            @RequestParam("url") String imageUrl) {
        projectService.deleteProjectImage(projectId, imageUrl);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(BASE_PATH_V1 + "/{projectId}/videos")
    public ResponseEntity<?> deleteProjectVideo(
            @PathVariable Long projectId,
            @RequestParam("url") String videoUrl) {
        projectService.deleteProjectVideo(projectId, videoUrl);
        return ResponseEntity.ok().build();
    }

    @GetMapping(BASE_PATH_V1 + "/featured")
    public List<ProjectResponse> getFeaturedProjects() {
        List<Project> featuredProjects = projectService.findFeaturedProjects();
        return featuredProjects.stream()
                .map(mapper::mapToProjectResponse)
                .collect(Collectors.toList());
    }


    @PutMapping(BASE_PATH_V1 + "/featured/order")
    public ResponseEntity<?> updateFeaturedProjectsOrder(@RequestBody List<Long> projectIds) {
        try {
            projectService.updateFeaturedProjectsOrder(projectIds);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to update order: " + e.getMessage());
        }
    }

}
