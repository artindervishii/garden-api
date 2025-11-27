package com.garden.api.clients;

import com.garden.api.projects.ProjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class ClientMapper {

    private final ProjectMapper projectMapper;

    public Client toEntity(ClientRequest request) {
        Client client = new Client();
        client.setName(request.getName());
        client.setAddress(request.getAddress());
        client.setEmail(request.getEmail());
        client.setPhoneNumber(request.getPhoneNumber());
        client.setStatus(ClientStatus.ACTIVE);
        client.setLastContactDate(LocalDate.now());
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
            response.setProjects(
                    client.getProjects().stream()
                            .map(projectMapper::mapToProjectResponse)
                            .toList()
            );
        }
        return response;
    }

    public void updateEntity(Client client, ClientRequest request) {
        client.setName(request.getName());
        client.setAddress(request.getAddress());
        client.setEmail(request.getEmail());
        client.setPhoneNumber(request.getPhoneNumber());
        client.setStatus(request.getStatus());
        client.setLastContactDate(request.getLastContactDate());
    }
}
