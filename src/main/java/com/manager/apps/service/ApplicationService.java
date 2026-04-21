package com.manager.apps.service;

import com.manager.apps.dto.request.ApplicationRequest;
import com.manager.apps.dto.response.ApplicationResponse;
import com.manager.apps.entity.Application;
import com.manager.apps.exception.ResourceNotFoundException;
import com.manager.apps.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;

    @Transactional
    public ApplicationResponse createApplication(ApplicationRequest request) {
        Application app = Application.builder()
            .appName(request.getAppName())
            .build();
        return toResponse(applicationRepository.save(app));
    }

    public List<ApplicationResponse> findAll() {
        return applicationRepository.findAll().stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    @Transactional
    public ApplicationResponse updateApplication(UUID id, ApplicationRequest request) {
        Application app = applicationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Application not found with id: " + id));
        app.setAppName(request.getAppName());
        return toResponse(applicationRepository.save(app));
    }

    @Transactional
    public void deleteApplication(UUID id) {
        if (!applicationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Application not found with id: " + id);
        }
        applicationRepository.deleteById(id);
    }

    private ApplicationResponse toResponse(Application app) {
        return ApplicationResponse.builder()
            .idApp(app.getIdApp())
            .appName(app.getAppName())
            .createdAt(app.getCreatedAt())
            .updatedAt(app.getUpdatedAt())
            .build();
    }
}
