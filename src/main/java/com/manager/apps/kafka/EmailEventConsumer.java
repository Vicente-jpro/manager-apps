package com.manager.apps.kafka;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.manager.apps.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailEventConsumer {

    private final EmailService emailService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${kafka.topic.email-events}", groupId = "email-consumer-group")
    public void consume(String message) {
        try {
            Map<String, Object> payload = objectMapper.readValue(message, new TypeReference<Map<String, Object>>() {});
            String to = (String) payload.get("email");
            String subject = (String) payload.getOrDefault("subject", "Notification");
            String body = (String) payload.getOrDefault("body", "");
            emailService.sendEmail(to, subject, body);
        } catch (Exception e) {
            log.error("Failed to process email event: {}", e.getMessage());
        }
    }
}
