package com.spiceswap.spiceswap.common.util;

import com.spiceswap.spiceswap.exception.ServiceBusinessException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.spiceswap.spiceswap.common.util.Constants.ErrorMessage.SEND_EMAIL_FAILED;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailUtil {
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Async
    public void sendEmail(String email, String subject, String text) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper msg = new MimeMessageHelper(mimeMessage, true);
            msg.setFrom(fromEmail);
            msg.setTo(email);
            msg.setSubject(subject);
            msg.setText(text);
            msg.setSentDate(new Date());
            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            log.error("Error sending email: {}", e.getMessage());
            throw new ServiceBusinessException(SEND_EMAIL_FAILED);
        }
    }
}
