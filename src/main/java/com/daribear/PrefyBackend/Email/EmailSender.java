package com.daribear.PrefyBackend.Email;

public interface EmailSender {
    void send(String to, String subject,String email);
}
