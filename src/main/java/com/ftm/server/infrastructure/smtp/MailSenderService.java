package com.ftm.server.infrastructure.smtp;

import com.ftm.server.adapter.gateway.MailSenderGateway;
import com.ftm.server.common.annotation.InfraService;
import com.ftm.server.common.exception.CustomException;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

@RequiredArgsConstructor
@InfraService
public class MailSenderService implements MailSenderGateway {

    private final JavaMailSender mailSender;

    @Override
    public void sendEmail(String to, String code) {
        MimeMessage message = mailSender.createMimeMessage();

        String mailContent =
                """
    <html>
        <body style="text-align:center; font-family:Arial, sans-serif;">
            <h1>핏더맨 이메일 인증 코드입니다</h1>
            <div style="font-size:24px; margin:20px 0; color:#4CAF50;"><strong>%s</strong></div>
            <p>위 인증 코드를 입력하여 이메일 인증을 완료해 주세요.</p>
            <br/>
            <hr/>
            <p style="font-size:12px; color:#888;">이 메일은 핏더맨 시스템에 의해 자동 발송되었습니다.</p>
            <p style="font-size:12px; color:#888;">문의:ftmanserver@gmail.com</p>
        </body>
    </html>
"""
                        .formatted(code);
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject("[핏더맨] 이메일 인증을 완료해주세요!");
            helper.setText(mailContent, true);
            mailSender.send(message);
        } catch (MailException | MessagingException e) {
            throw new CustomException((ErrorResponseCode.FAIL_TO_SEND_EMAIL), e.getMessage());
        }
    }
}
