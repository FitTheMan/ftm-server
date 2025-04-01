package com.ftm.server.application.port.out.smtp;

public interface MailSenderPort {

    void sendEmail(String to, String code);
}
