package com.manager.apps.service;

import com.manager.apps.dto.request.ClientRequest;
import com.manager.apps.dto.response.ClientResponse;
import com.manager.apps.entity.Client;
import com.manager.apps.exception.DuplicateResourceException;
import com.manager.apps.exception.ResourceNotFoundException;
import com.manager.apps.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ClientService clientService;

    private Client testClient;
    private UUID clientId;

    @BeforeEach
    void setUp() {
        clientId = UUID.randomUUID();
        testClient = Client.builder()
            .idClient(clientId)
            .clientName("Test Client")
            .email("test@example.com")
            .maxDay(30)
            .usedDay(0)
            .status("active")
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    }

    @Test
    void createClient_success() {
        ClientRequest request = new ClientRequest("Test Client", "test@example.com", 30, null, null);
        when(clientRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any())).thenReturn("hashedToken");
        when(clientRepository.save(any())).thenReturn(testClient);

        ClientResponse response = clientService.createClient(request);

        assertNotNull(response);
        assertEquals("Test Client", response.getClientName());
        verify(clientRepository).save(any());
    }

    @Test
    void createClient_duplicateEmail_throwsException() {
        ClientRequest request = new ClientRequest("Test Client", "test@example.com", 30, null, null);
        when(clientRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testClient));

        assertThrows(DuplicateResourceException.class, () -> clientService.createClient(request));
    }

    @Test
    void findById_success() {
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(testClient));

        ClientResponse response = clientService.findById(clientId);

        assertNotNull(response);
        assertEquals(clientId, response.getIdClient());
    }

    @Test
    void findById_notFound_throwsException() {
        UUID unknownId = UUID.randomUUID();
        when(clientRepository.findById(unknownId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> clientService.findById(unknownId));
    }

    @Test
    void softDelete_success() {
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(testClient));
        when(clientRepository.save(any())).thenReturn(testClient);

        clientService.softDelete(clientId);

        assertNotNull(testClient.getDeletedAt());
        verify(clientRepository).save(testClient);
    }
}
