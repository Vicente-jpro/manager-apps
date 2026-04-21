package com.manager.apps.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientRequest {
    @NotBlank
    private String clientName;
    @Email
    @NotBlank
    private String email;
    @NotNull
    @Positive
    private Integer maxDay;
    private String createdBy;
    private String updatedBy;
}
