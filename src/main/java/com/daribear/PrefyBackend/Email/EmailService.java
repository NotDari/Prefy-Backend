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

@Service
@AllArgsConstructor
public class EmailService implements EmailSender {
    private final static Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;


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
            System.out.println("Sdad sending!" + to);
            mailSender.send(mimeMessage);
        } catch (MessagingException | UnsupportedEncodingException e){
            logger.error("failed to send email", e);
            throw new IllegalStateException("failed to send email");
        }
    }
}
