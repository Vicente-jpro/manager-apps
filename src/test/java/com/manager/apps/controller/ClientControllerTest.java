package com.manager.apps.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manager.apps.dto.request.ClientRequest;
import com.manager.apps.dto.response.ClientResponse;
import com.manager.apps.repository.ManagerRepository;
import com.manager.apps.service.ClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClientController.class)
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService clientService;

    @MockBean
    private ManagerRepository managerRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void getClients_success() throws Exception {
        ClientResponse response = ClientResponse.builder()
            .idClient(UUID.randomUUID())
            .clientName("Test")
            .email("test@example.com")
            .status("active")
            .maxDay(30)
            .usedDay(0)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        when(clientService.findClients(any(), any(), any(), any(Pageable.class)))
            .thenReturn(new PageImpl<>(List.of(response)));

        mockMvc.perform(get("/api/clients"))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void createClient_success() throws Exception {
        ClientRequest request = new ClientRequest("Test", "test@example.com", 30, null, null);
        ClientResponse response = ClientResponse.builder()
            .idClient(UUID.randomUUID())
            .clientName("Test")
            .email("test@example.com")
            .status("active")
            .maxDay(30)
            .usedDay(0)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        when(clientService.createClient(any())).thenReturn(response);

        mockMvc.perform(post("/api/clients")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated());
    }
}
