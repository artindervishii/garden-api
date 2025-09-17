package com.garden.api.clients;


import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class ClientController {

    public static final String BASE_PATH_V1 = "/v1/clients";

    private final ClientService clientService;

    @PostMapping(value = BASE_PATH_V1)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> addClient(@RequestBody ClientRequest clientRequest) {
        Long clientId = clientService.addClient(clientRequest);
        return ResponseEntity.ok(clientId);
    }

    @PutMapping(value = BASE_PATH_V1 + "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateClient(@PathVariable Long id, @RequestBody ClientRequest clientRequest) {
        clientService.updateClient(id, clientRequest);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @DeleteMapping(value = BASE_PATH_V1 +"/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(BASE_PATH_V1 + "/{id}")
    public ResponseEntity<ClientResponse> getClientById(@PathVariable Long id) {
        ClientResponse clientResponse = clientService.findClientById(id);
        return new ResponseEntity<>(clientResponse, HttpStatus.OK);
    }

    @GetMapping(BASE_PATH_V1)
    @PreAuthorize("hasRole('ADMIN')")
    public Page<ClientResponse> getAllClients(
            @RequestParam(value = "status", required = false) ClientStatus status,
            @PageableDefault(size = 20, sort = {"createdAt"}, direction = Sort.Direction.DESC) Pageable pageable
    ){
        return clientService.findAllByStatus(status, pageable);
    }
}
