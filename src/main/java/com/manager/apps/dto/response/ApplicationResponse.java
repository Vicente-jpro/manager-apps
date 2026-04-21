package com.manager.apps.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationResponse {
    private UUID idApp;
    private String appName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
