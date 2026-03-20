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
            log.debug("Email disabled - skipping alert to {} for earthquake {}", recipient, event.getEarthquakeId());
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
            log.info("Email sent to {} for earthquake {}", recipient, event.getEarthquakeId());

        } catch (MessagingException e) {
            log.error("Failed to send email to {}: {}", recipient, e.getMessage());
        }
    }

    @Override
    public void sendBulkAlert(EarthquakeEvent event, String[] recipients) {
        log.info("Sending bulk alert to {} recipients", recipients.length);
        for (String recipient : recipients) {
            sendEarthquakeAlert(event, recipient);
        }
    }
}
