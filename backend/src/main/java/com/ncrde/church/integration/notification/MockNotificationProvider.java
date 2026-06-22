package com.ncrde.church.integration.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MockNotificationProvider implements NotificationProvider {

    private static final Logger logger = LoggerFactory.getLogger(MockNotificationProvider.class);

    @Override
    public void sendSMS(String phoneNumber, String message) {
        logger.info("============== MOCK SMS GATEWAY ==============");
        logger.info("To: {}", phoneNumber);
        logger.info("Message: {}", message);
        logger.info("===============================================");
    }

    @Override
    public void sendEmail(String emailAddress, String subject, String body) {
        logger.info("============= MOCK EMAIL GATEWAY =============");
        logger.info("To: {}", emailAddress);
        logger.info("Subject: {}", subject);
        logger.info("Body: {}", body);
        logger.info("===============================================");
    }
}
