package com.manager.apps.scheduler;

import com.manager.apps.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class UsedDayScheduler {

    private final ClientRepository clientRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void incrementUsedDay() {
        log.info("Running used_day increment scheduler");
        int updated = clientRepository.incrementUsedDay();
        log.info("Updated used_day for {} clients", updated);
    }
}
