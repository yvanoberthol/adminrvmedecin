package com.yvanscoop.gestmedecins.services.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailGlobal {

    @Autowired
    private Environment environment;


    @Autowired
    private JavaMailSender javaMailSender;


    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public void sendMail(String to, String subject, String body){

        SimpleMailMessage mail = new SimpleMailMessage();
        String from = environment.getProperty("spring.mail.username");

        mail.setFrom(from);
        mail.setTo(to);
        mail.setSubject(subject);
        mail.setText(body);

        logger.info("Sending...");

        javaMailSender.send(mail);

        logger.info("Done!");
    }
}
