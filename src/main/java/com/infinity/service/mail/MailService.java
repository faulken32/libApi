/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.infinity.service.mail;


import com.infinity.service.abstractService.AbstractService;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 *
 * @author t311372
 */
@Service
public class MailService extends AbstractService {

    private static final Logger LOG = LoggerFactory.getLogger(MailService.class);
    private Session session;
    private MimeMessage message;
    private String dest;
    private String contends;
    private String subject;

   
   
    
   

    public void send(final String dest, final String contends, final String subject) {

        this.dest = dest;
        this.contends = contends;
        this.subject = subject;
        this.setProps();

        try {
            Transport.send(message);
        } catch (MessagingException ex) {
            LOG.error(ex.getMessage());
        }

    }
    
    private void setProps() {

        Properties props = new Properties();

        props.put("mail.smtp.host", "smtp.phpnet.org");
        props.put("mail.smtp.user", "contact@infinity-web.fr");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "8025");
        props.setProperty("mail.from", "contact@infinity-web.fr"); // @ expediteur

        session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("contact@infinity-web.fr", "kanekane32");
                    }
                });
        try {
            message = new MimeMessage(session);

            message.setFrom(new InternetAddress("contact@infinity-web.fr"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(this.dest));

            message.setSubject(subject);
            message.setContent(this.contends, "text/html; charset=utf-8");

        } catch (MessagingException ex) {
            LOG.error(ex.getMessage());
        }
    }
    
    
   
}
