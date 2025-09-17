package com.garden.api.projects;

import com.garden.api.exceptions.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper mapper;

    public Long addProject(ProjectRequest projectRequest) {
        Project project = mapper.map(projectRequest);
        projectRepository.save(project);
        return project.getId();
    }

    public void updateProject(Long id,ProjectRequest projectRequest) {
        Project project = projectRepository.findById(id).orElseThrow(() -> new RuntimeException("Project not found with id: " + id));
        mapper.map(project,projectRequest);
        projectRepository.save(project);
    }

    public void deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new ResourceNotFoundException("Project with ID: " + id + " not found");
        }
        projectRepository.deleteById(id);
    }

    public ProjectResponse findProjectById(Long id) {
        return projectRepository.findById(id).map(mapper::mapToProjectResponse).orElseThrow(() -> new ResourceNotFoundException("Project with ID: " + id + " not found"));
    }

    public Page<ProjectResponse> findAllByStatus(ProjectStatus status, Pageable pageable) {
        Page<Project> projects = projectRepository.findAllByStatus(status,pageable);
        return projects.map(mapper::mapToProjectResponse);
    }
}
