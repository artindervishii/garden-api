package com.garden.api.clients;

import com.garden.api.exceptions.ResourceNotFoundException;
import com.garden.api.projects.Project;
import com.garden.api.projects.ProjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Objects;

@AllArgsConstructor
@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final ProjectRepository projectRepository;

    public Long addClient(ClientRequest clientRequest) {
        Client client = clientMapper.toEntity(clientRequest);
        clientRepository.save(client);
        return client.getId();
    }

    public void updateClient(Long id, ClientRequest clientRequest) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client with ID: " + id + " not found"));
        clientMapper.updateEntity(client, clientRequest);
        clientRepository.save(client);
    }

    @Transactional
    public void deleteClient(Long id) {
        Client client = clientRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Client with ID: " + id + " not found"));
        for (Project project : client.getProjects()) {
            project.setClient(null);
        }
        projectRepository.saveAll(client.getProjects());

        clientRepository.delete(client);
    }


    public ClientResponse findClientById(Long id) {
        return clientRepository.findById(id)
                .map(clientMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Client with ID: " + id + " not found"));
    }

    public Page<ClientResponse> findAllByStatus(ClientStatus status, Pageable pageable) {
        Page<Client> clients;
        if (status != null) {
            clients = clientRepository.findAllByStatus(status, pageable);
        } else {
            clients = clientRepository.findAll(pageable);
        }
        return clients.map(clientMapper::toResponse);
    }

    public void updateClientTotalSpent(Client client) {
        BigDecimal total = client.getProjects().stream()
                .map(Project::getPrice)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        client.setTotalSpent(total);
        clientRepository.save(client);
    }

}
