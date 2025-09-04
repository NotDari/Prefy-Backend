package com.daribear.PrefyBackend.Email;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

/**
 * An implementation of the EmailSender interface.
 * Sends an email to the recipient, with the correct sender with the correct content, throwing an error if one occurs.
 * Uses JavaMailSender.
 */
@Service
@AllArgsConstructor
public class EmailService implements EmailSender {
    private final JavaMailSender mailSender;

    /**
     * Sends the email, to the correct receipient with the correct subject, and with the right content.
     * Throws an error if one occurs.
     *
     * @param to recipient of the email
     * @param subject subject of the email
     * @param email content of the email
     */
    @Override
    @Async
    public void send(String to, String subject, String email) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "utf-8");
            mimeMessageHelper.setText(email, true);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setFrom(new InternetAddress("support@daribear.com", "Prefy Support"));
            mailSender.send(mimeMessage);
        } catch (MessagingException | UnsupportedEncodingException e){
            throw new IllegalStateException("failed to send email");
        }
    }
}
