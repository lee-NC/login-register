package com.jasu.loginregister.Email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class VerifyEmailService extends EmailService {

    @Autowired
    private JavaMailSender emailSender;


}
