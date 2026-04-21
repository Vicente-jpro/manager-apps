package com.manager.apps.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.manager.apps.entity.Outbox;
import com.manager.apps.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OutboxService {

    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    public void createOutboxEvent(String aggregateId, String aggregateType, String eventType, Object payload) {
        try {
            String payloadJson = objectMapper.writeValueAsString(payload);
            Outbox outbox = Outbox.builder()
                .aggregateId(aggregateId)
                .aggregateType(aggregateType)
                .eventType(eventType)
                .payload(payloadJson)
                .status("NOT_PROCESSED")
                .retryCount(0)
                .createdAt(LocalDateTime.now())
                .build();
            outboxRepository.save(outbox);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize outbox payload", e);
        }
    }

    public List<Outbox> findUnprocessed() {
        return outboxRepository.findByStatus("NOT_PROCESSED");
    }

    public void markProcessed(Outbox outbox) {
        outbox.setStatus("PROCESSED");
        outbox.setProcessedAt(LocalDateTime.now());
        outboxRepository.save(outbox);
    }

    public void markFailed(Outbox outbox, String error) {
        outbox.setStatus("FAILED");
        outbox.setLastError(error);
        outbox.setRetryCount(outbox.getRetryCount() + 1);
        outboxRepository.save(outbox);
    }
}
