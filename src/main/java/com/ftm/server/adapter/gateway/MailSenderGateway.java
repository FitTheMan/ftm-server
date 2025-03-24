package com.ftm.server.adapter.gateway;

public interface MailSenderGateway {

    void sendEmail(String to, String code);
}
