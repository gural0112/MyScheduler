package com.mgreg.myscheduler.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailServiceImpl implements  NotifyService {

    @Autowired
    private JavaMailSender emailSender;



    public EmailServiceImpl(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    public void sendNotification(MessageInfo messageInfo) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(messageInfo.getFrom());
        message.setTo(messageInfo.getTo());
        message.setSubject(messageInfo.getSubject());
        message.setText(messageInfo.getText());
        emailSender.send(message);
    }
}
