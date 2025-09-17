package com.garden.api.clients;

import com.garden.api.projects.Project;
import com.garden.api.projects.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ClientMapper {

    private final ProjectRepository projectRepository;

    public Client toEntity(ClientRequest request) {
        Client client = new Client();
        client.setName(request.getName());
        client.setAddress(request.getAddress());
        client.setEmail(request.getEmail());
        client.setPhoneNumber(request.getPhoneNumber());
        client.setStatus(request.getStatus());
        client.setTotalSpent(request.getTotalSpent());
        client.setLastContactDate(request.getLastContactDate());
        if (request.getProjectIds() != null) {
            client.setProjects(request.getProjectIds()
                    .stream()
                    .map(id -> projectRepository.findById(id).orElse(null))
                    .collect(Collectors.toList()));
        }
        return client;
    }

    public ClientResponse toResponse(Client client) {
        ClientResponse response = new ClientResponse();
        response.setId(client.getId());
        response.setName(client.getName());
        response.setAddress(client.getAddress());
        response.setEmail(client.getEmail());
        response.setPhoneNumber(client.getPhoneNumber());
        response.setStatus(client.getStatus());
        response.setTotalSpent(client.getTotalSpent());
        response.setLastContactDate(client.getLastContactDate());
        if (client.getProjects() != null) {
            response.setProjectTitles(client.getProjects()
                    .stream()
                    .map(Project::getTitle)
                    .collect(Collectors.toList()));
        }
        return response;
    }

    public void updateEntity(Client client, ClientRequest request) {
        client.setName(request.getName());
        client.setAddress(request.getAddress());
        client.setEmail(request.getEmail());
        client.setPhoneNumber(request.getPhoneNumber());
        client.setStatus(request.getStatus());
        client.setTotalSpent(request.getTotalSpent());
        client.setLastContactDate(request.getLastContactDate());
        if (request.getProjectIds() != null) {
            client.setProjects(request.getProjectIds()
                    .stream()
                    .map(id -> projectRepository.findById(id).orElse(null))
                    .collect(Collectors.toList()));
        }
    }
}
