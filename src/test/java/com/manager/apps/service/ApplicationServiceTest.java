package com.manager.apps.service;

import com.manager.apps.dto.request.ApplicationRequest;
import com.manager.apps.dto.response.ApplicationResponse;
import com.manager.apps.entity.Application;
import com.manager.apps.exception.ResourceNotFoundException;
import com.manager.apps.repository.ApplicationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {

    @Mock
    private ApplicationRepository applicationRepository;

    @InjectMocks
    private ApplicationService applicationService;

    private Application testApp;
    private UUID appId;

    @BeforeEach
    void setUp() {
        appId = UUID.randomUUID();
        testApp = Application.builder()
            .idApp(appId)
            .appName("Test App")
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    }

    @Test
    void createApplication_success() {
        ApplicationRequest request = new ApplicationRequest("Test App");
        when(applicationRepository.save(any())).thenReturn(testApp);

        ApplicationResponse response = applicationService.createApplication(request);

        assertNotNull(response);
        assertEquals("Test App", response.getAppName());
    }

    @Test
    void findAll_success() {
        when(applicationRepository.findAll()).thenReturn(List.of(testApp));

        List<ApplicationResponse> responses = applicationService.findAll();

        assertFalse(responses.isEmpty());
        assertEquals(1, responses.size());
    }

    @Test
    void deleteApplication_notFound_throwsException() {
        UUID unknownId = UUID.randomUUID();
        when(applicationRepository.existsById(unknownId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> applicationService.deleteApplication(unknownId));
    }
}
