package com.manager.apps.controller;

import com.manager.apps.dto.request.ClientRequest;
import com.manager.apps.dto.request.StatusUpdateRequest;
import com.manager.apps.dto.response.ClientResponse;
import com.manager.apps.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    public ResponseEntity<ClientResponse> createClient(@RequestBody @Valid ClientRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.createClient(request));
    }

    @GetMapping
    public ResponseEntity<Page<ClientResponse>> getClients(
        @RequestParam(required = false) UUID idClient,
        @RequestParam(required = false) String email,
        @RequestParam(required = false) String status,
        Pageable pageable) {
        return ResponseEntity.ok(clientService.findClients(idClient, email, status, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> getClient(@PathVariable UUID id) {
        return ResponseEntity.ok(clientService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientResponse> updateClient(@PathVariable UUID id, @RequestBody @Valid ClientRequest request) {
        return ResponseEntity.ok(clientService.updateClient(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable UUID id) {
        clientService.softDelete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ClientResponse> updateStatus(@PathVariable UUID id, @RequestBody @Valid StatusUpdateRequest request) {
        return ResponseEntity.ok(clientService.updateStatus(id, request));
    }
}
