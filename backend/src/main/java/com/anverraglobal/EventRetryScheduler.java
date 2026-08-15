package com.anverraglobal;

import com.anverraglobal.commission.event.CommissionConfiguredEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.modulith.events.IncompleteEventPublications;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Component
public class EventRetryScheduler {

    private static final Logger log = LoggerFactory.getLogger(EventRetryScheduler.class);
    private final IncompleteEventPublications incompleteEvents;

    public EventRetryScheduler(IncompleteEventPublications incompleteEvents) {
        this.incompleteEvents = incompleteEvents;
    }

    @Scheduled(initialDelayString = "${app.retry.initial-delay:60000}", fixedDelayString = "${app.retry.fixed-delay:60000}")
    public void retryIncompleteEvents() {
        log.debug("Resubmitting incomplete event publications for CommissionConfiguredEvent...");
        
        Instant twentyFourHoursAgo = Instant.now().minus(Duration.ofHours(24));

        incompleteEvents.resubmitIncompletePublications((eventPublication) -> 
            eventPublication.getEvent() instanceof CommissionConfiguredEvent &&
            eventPublication.getPublicationDate().isAfter(twentyFourHoursAgo)
        );
    }
}
