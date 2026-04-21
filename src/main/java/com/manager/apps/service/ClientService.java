package com.manager.apps.service;

import com.manager.apps.dto.request.ClientRequest;
import com.manager.apps.dto.request.StatusUpdateRequest;
import com.manager.apps.dto.response.ClientResponse;
import com.manager.apps.entity.Client;
import com.manager.apps.exception.DuplicateResourceException;
import com.manager.apps.exception.ResourceNotFoundException;
import com.manager.apps.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public ClientResponse createClient(ClientRequest request) {
        if (clientRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Client with email " + request.getEmail() + " already exists");
        }
        String rawToken = UUID.randomUUID().toString();
        String hashedToken = passwordEncoder.encode(rawToken);

        Client client = Client.builder()
            .clientName(request.getClientName())
            .email(request.getEmail())
            .token(hashedToken)
            .maxDay(request.getMaxDay())
            .usedDay(0)
            .status("active")
            .createdBy(request.getCreatedBy())
            .build();

        Client saved = clientRepository.save(client);
        ClientResponse response = toResponse(saved);
        response.setRawToken(rawToken);
        return response;
    }

    public Page<ClientResponse> findClients(UUID idClient, String email, String status, Pageable pageable) {
        Specification<Client> spec = Specification.where(null);
        if (idClient != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("idClient"), idClient));
        }
        if (email != null && !email.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("email"), email));
        }
        if (status != null && !status.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
        }
        return clientRepository.findAll(spec, pageable).map(this::toResponse);
    }

    public ClientResponse findById(UUID id) {
        Client client = clientRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));
        return toResponse(client);
    }

    @Transactional
    public ClientResponse updateClient(UUID id, ClientRequest request) {
        Client client = clientRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));

        client.setClientName(request.getClientName());
        client.setEmail(request.getEmail());
        client.setMaxDay(request.getMaxDay());
        client.setUpdatedBy(request.getUpdatedBy());

        return toResponse(clientRepository.save(client));
    }

    @Transactional
    public void softDelete(UUID id) {
        Client client = clientRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));
        client.setDeletedAt(LocalDateTime.now());
        clientRepository.save(client);
    }

    @Transactional
    public ClientResponse updateStatus(UUID id, StatusUpdateRequest request) {
        Client client = clientRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));
        client.setStatus(request.getStatus());
        return toResponse(clientRepository.save(client));
    }

    private ClientResponse toResponse(Client client) {
        return ClientResponse.builder()
            .idClient(client.getIdClient())
            .clientName(client.getClientName())
            .email(client.getEmail())
            .maxDay(client.getMaxDay())
            .usedDay(client.getUsedDay())
            .status(client.getStatus())
            .createdAt(client.getCreatedAt())
            .updatedAt(client.getUpdatedAt())
            .build();
    }
}
