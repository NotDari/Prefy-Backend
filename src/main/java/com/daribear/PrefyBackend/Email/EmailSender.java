package com.daribear.PrefyBackend.Email;

/**
 * Interface for sending emails where implementations send an email with the receipient, subject and content.
 */
public interface EmailSender {
    //the default send function where implementations send the email with the receipient, subject and content.
    void send(String to, String subject,String email);
}
