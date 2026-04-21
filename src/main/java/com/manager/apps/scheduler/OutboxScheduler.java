package com.manager.apps.scheduler;

import com.manager.apps.entity.Client;
import com.manager.apps.entity.Outbox;
import com.manager.apps.kafka.EmailEventProducer;
import com.manager.apps.repository.ClientRepository;
import com.manager.apps.service.OutboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxScheduler {

    private final OutboxService outboxService;
    private final ClientRepository clientRepository;
    private final EmailEventProducer emailEventProducer;

    @Scheduled(cron = "0 0 8 * * ?")
    public void scheduleEmailReminders() {
        log.info("Checking clients near limit for email reminders");
        List<Client> clients = clientRepository.findClientsNearLimit();
        for (Client client : clients) {
            Map<String, Object> payload = new HashMap<>();
            payload.put("email", client.getEmail());
            payload.put("subject", "Approaching day limit");
            payload.put("body", "Dear " + client.getClientName() + ", you are 5 days away from your limit.");
            payload.put("clientId", client.getIdClient().toString());
            outboxService.createOutboxEvent(
                client.getIdClient().toString(),
                "Client",
                "EMAIL_REMINDER",
                payload
            );
        }
    }

    @Scheduled(cron = "0 */5 * * * ?")
    public void processOutbox() {
        log.info("Processing outbox events");
        List<Outbox> unprocessed = outboxService.findUnprocessed();
        for (Outbox outbox : unprocessed) {
            try {
                emailEventProducer.sendEmailEvent(outbox.getPayload());
                outboxService.markProcessed(outbox);
            } catch (Exception e) {
                log.error("Failed to process outbox event {}: {}", outbox.getId(), e.getMessage());
                outboxService.markFailed(outbox, e.getMessage());
            }
        }
    }
}
