package com.manager.apps.service;

import com.manager.apps.dto.response.ApplicationResponse;
import com.manager.apps.entity.Application;
import com.manager.apps.entity.Client;
import com.manager.apps.entity.ClientApplication;
import com.manager.apps.exception.DuplicateResourceException;
import com.manager.apps.exception.ResourceNotFoundException;
import com.manager.apps.repository.ApplicationRepository;
import com.manager.apps.repository.ClientApplicationRepository;
import com.manager.apps.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientApplicationService {

    private final ClientRepository clientRepository;
    private final ApplicationRepository applicationRepository;
    private final ClientApplicationRepository clientApplicationRepository;

    @Transactional
    public void assignApplication(UUID clientId, UUID appId) {
        Client client = clientRepository.findById(clientId)
            .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + clientId));
        Application app = applicationRepository.findById(appId)
            .orElseThrow(() -> new ResourceNotFoundException("Application not found with id: " + appId));

        if (clientApplicationRepository.findByClientIdClientAndApplicationIdApp(clientId, appId).isPresent()) {
            throw new DuplicateResourceException("Application already assigned to this client");
        }

        ClientApplication ca = ClientApplication.builder()
            .client(client)
            .application(app)
            .build();
        clientApplicationRepository.save(ca);
    }

    @Transactional
    public void removeApplication(UUID clientId, UUID appId) {
        clientApplicationRepository.deleteByClientIdClientAndApplicationIdApp(clientId, appId);
    }

    public List<ApplicationResponse> getClientApplications(UUID clientId) {
        if (!clientRepository.existsById(clientId)) {
            throw new ResourceNotFoundException("Client not found with id: " + clientId);
        }
        return clientApplicationRepository.findByClientIdClient(clientId).stream()
            .map(ca -> ApplicationResponse.builder()
                .idApp(ca.getApplication().getIdApp())
                .appName(ca.getApplication().getAppName())
                .createdAt(ca.getApplication().getCreatedAt())
                .updatedAt(ca.getApplication().getUpdatedAt())
                .build())
            .collect(Collectors.toList());
    }
}
