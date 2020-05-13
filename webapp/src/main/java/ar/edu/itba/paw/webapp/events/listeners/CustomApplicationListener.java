package ar.edu.itba.paw.webapp.events.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class CustomApplicationListener implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomApplicationListener.class);
    private final AtomicBoolean ran = new AtomicBoolean();

    @Override
    @Async
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (!this.ran.getAndSet(true)) {
            LOGGER.info("Application started");
        }
    }
}
