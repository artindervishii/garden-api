package com.garden.api.projects;

import com.garden.api.clients.Client;
import com.garden.api.clients.ClientService;
import com.garden.api.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper mapper;
    private final ClientService clientService;

    public Long addProject(ProjectRequest projectRequest) {
        Project project = mapper.map(projectRequest);
        projectRepository.save(project);

        Client client = project.getClient();
        if (client != null) {
            clientService.updateClientTotalSpent(client);
        }
        return project.getId();
    }

    public void updateProject(Long id, ProjectRequest projectRequest) {
        Project project = projectRepository.findById(id).orElseThrow(() -> new RuntimeException("Project not found with id: " + id));
        mapper.map(project, projectRequest);
        projectRepository.save(project);

        Client client = project.getClient();
        if (client != null) {
            clientService.updateClientTotalSpent(client);
        }

    }

    public void deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new ResourceNotFoundException("Project with ID: " + id + " not found");
        }
        projectRepository.deleteById(id);

        Project project = projectRepository.findById(id).orElseThrow(() -> new RuntimeException("Project not found with id: " + id));

        Client client = project.getClient();
        if (client != null) {
            clientService.updateClientTotalSpent(client);
        }
    }

    public ProjectResponse findProjectById(Long id) {
        return projectRepository.findById(id).map(mapper::mapToProjectResponse).orElseThrow(() -> new ResourceNotFoundException("Project with ID: " + id + " not found"));
    }

    public Page<ProjectResponse> findAllByStatus(ProjectStatus status, Pageable pageable) {
        Page<Project> projects = projectRepository.findAllByStatus(status, pageable);
        return projects.map(mapper::mapToProjectResponse);
    }

    public Page<ProjectResponse> findProjectsByCategory(Long categoryId, Pageable pageable) {
        Page<Project> projects;
        projects = projectRepository.findByCategories_Id(categoryId, pageable);
        return projects.map(mapper::mapToProjectResponse);
    }

    @Transactional
    public String uploadProjectImage(Long projectId, MultipartFile file) {

        String baseUrl = "https://api.garten-er.de";

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project with ID " + projectId + " not found"));

        try {
            String uploadDir = System.getProperty("user.dir") + "/uploads/projects/images/";
            File folder = new File(uploadDir);
            if (!folder.exists()) folder.mkdirs();

            String filename = file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir + filename);

            file.transferTo(filePath.toFile());

            String url = baseUrl + "/images/projects/" + filename;

            project.getImages().add(url);
            projectRepository.save(project);

            System.out.println("Saved file to: " + filePath.toAbsolutePath());

            return url;

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image", e);
        }
    }

    @Transactional
    public String uploadProjectVideo(Long projectId, MultipartFile file) {

        String baseUrl = "https://api.garten-er.de";
        //test

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project with ID " + projectId + " not found"));

        try {
            String uploadDir = System.getProperty("user.dir") + "/uploads/projects/videos/";
            File folder = new File(uploadDir);
            if (!folder.exists()) folder.mkdirs();

            String filename = file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir + filename);
            file.transferTo(filePath.toFile());

            String url = baseUrl + "/videos/projects/" + filename;

            project.getVideos().add(url);
            projectRepository.save(project);

            return url;

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload video", e);
        }
    }

    @Transactional
    public void deleteProjectImage(Long projectId, String imageUrl) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project with ID " + projectId + " not found"));
        project.getImages().removeIf(url -> url.equals(imageUrl));
        projectRepository.save(project);

        try {
            String filename = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
            String uploadDir = System.getProperty("user.dir") + "/uploads/projects/images/";
            Path filePath = Paths.get(uploadDir + filename);

            if (Files.exists(filePath)) {
                Files.delete(filePath);
                System.out.println("Deleted file: " + filePath.toAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("Failed to delete image file: " + e.getMessage());
        }
    }

    @Transactional
    public void deleteProjectVideo(Long projectId, String videoUrl) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project with ID " + projectId + " not found"));
        project.getVideos().removeIf(url -> url.equals(videoUrl));
        projectRepository.save(project);

        try {
            String filename = videoUrl.substring(videoUrl.lastIndexOf("/") + 1);
            String uploadDir = System.getProperty("user.dir") + "/uploads/projects/videos/";
            Path filePath = Paths.get(uploadDir + filename);

            if (Files.exists(filePath)) {
                Files.delete(filePath);
                System.out.println("Deleted file: " + filePath.toAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("Failed to delete video file: " + e.getMessage());
        }
    }

    public List<Project> findFeaturedProjects() {
        return projectRepository.findByStatusAndDisplayOrderIsNotNullOrderByDisplayOrderAsc(ProjectStatus.Completed);
    }

    @Transactional
    public void updateFeaturedProjectsOrder(List<Long> projectIds) {
        List<Project> allProjects = projectRepository.findAll();
        for (Project project : allProjects) {
            project.setDisplayOrder(null);
        }
        projectRepository.saveAll(allProjects);

        int maxFeatured = Math.min(projectIds.size(), 8);
        for (int i = 0; i < maxFeatured; i++) {
            Long projectId = projectIds.get(i);
            Project project = projectRepository.findById(projectId)
                    .orElseThrow(() -> new EntityNotFoundException("Project not found: " + projectId));
            project.setDisplayOrder(i + 1);
        }
        projectRepository.saveAll(allProjects.stream()
                .filter(p -> p.getDisplayOrder() != null)
                .collect(Collectors.toList()));
    }

}
