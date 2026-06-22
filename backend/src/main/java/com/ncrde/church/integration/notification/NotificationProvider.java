package com.ncrde.church.integration.notification;

public interface NotificationProvider {
    void sendSMS(String phoneNumber, String message);
    void sendEmail(String emailAddress, String subject, String body);
}
