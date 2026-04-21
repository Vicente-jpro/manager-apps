package com.manager.apps.controller;

import com.manager.apps.dto.response.ApplicationResponse;
import com.manager.apps.service.ClientApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientApplicationController {

    private final ClientApplicationService clientApplicationService;

    @PostMapping("/{id}/applications/{appId}")
    public ResponseEntity<Void> assignApplication(@PathVariable UUID id, @PathVariable UUID appId) {
        clientApplicationService.assignApplication(id, appId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}/applications/{appId}")
    public ResponseEntity<Void> removeApplication(@PathVariable UUID id, @PathVariable UUID appId) {
        clientApplicationService.removeApplication(id, appId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/applications")
    public ResponseEntity<List<ApplicationResponse>> getClientApplications(@PathVariable UUID id) {
        return ResponseEntity.ok(clientApplicationService.getClientApplications(id));
    }
}
