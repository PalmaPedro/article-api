package com.example.api.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class NotificationService {
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    public void sendNotification(String message) {
        logger.info("Notification: {}",message);
    }
}
