package com.ftm.server.application.port;

public interface MailSenderPort {

    void sendEmail(String to, String code);
}
