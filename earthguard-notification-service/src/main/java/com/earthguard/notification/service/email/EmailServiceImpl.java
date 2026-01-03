package com.earthguard.notification.service.email;

import com.earthguard.notification.dto.EarthquakeEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${earthguard.email.enabled:false}")
    private boolean emailEnabled;

    @Override
    public void sendEarthquakeAlert(EarthquakeEvent event, String recipient) {
        if (!emailEnabled) {
            log.info("📧 EMAIL (MOCK): Would send earthquake alert to {}", recipient);
            log.info("   Subject: {}", EmailTemplate.getSubject(event));
            log.info("   Earthquake: {} at {}", event.getMagnitude(), event.getLocation());
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(recipient);
            helper.setSubject(EmailTemplate.getSubject(event));
            helper.setText(EmailTemplate.getHtmlBody(event), true);

            mailSender.send(message);

            log.info("✅ Email sent successfully to {}", recipient);

        } catch (MessagingException e) {
            log.error("❌ Failed to send email to {}: {}", recipient, e.getMessage(), e);
        }
    }

    @Override
    public void sendBulkAlert(EarthquakeEvent event, String[] recipients) {
        log.info("📧 Sending bulk alert to {} recipients", recipients.length);

        for (String recipient : recipients) {
            try {
                sendEarthquakeAlert(event, recipient);
            } catch (Exception e) {
                log.error("Failed to send email to {}", recipient, e);
            }
        }

        log.info("✅ Bulk alert completed");
    }
}