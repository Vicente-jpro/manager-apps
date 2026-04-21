package com.manager.apps.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClientResponse {
    private UUID idClient;
    private String clientName;
    private String email;
    private String rawToken;
    private Integer maxDay;
    private Integer usedDay;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
