package com.manager.apps.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${kafka.topic.email-events}")
    private String topic;

    public void sendEmailEvent(String payload) {
        log.info("Sending email event to topic: {}", topic);
        kafkaTemplate.send(topic, payload);
    }
}
