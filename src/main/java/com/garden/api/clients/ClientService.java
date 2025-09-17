package com.garden.api.clients;

import com.garden.api.exceptions.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

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

    public void deleteClient(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new ResourceNotFoundException("Client with ID: " + id + " not found");
        }
        clientRepository.deleteById(id);
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
}
