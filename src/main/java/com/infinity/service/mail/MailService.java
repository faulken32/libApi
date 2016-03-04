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
    private static final Logger LOG_MAIL = LoggerFactory.getLogger("MAIL");
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
            LOG_MAIL.info("mail send to : " + dest);
            
            
        } catch (MessagingException ex) {
            LOG.error(ex.getMessage());
          
        }

    }
    
    private void setProps() {

        Properties props = new Properties();

        props.put("mail.smtp.host", "127.0.0.1");
//        props.put("mail.smtp.user", "root");
//        props.put("mail.smtp.auth", "false");
//        props.put("mail.smtp.port", "25");
        props.setProperty("mail.from", "cerebros@cerebros-jobs.com"); // @ expediteur

        session = Session.getInstance(props);
        
        
         try {
            message = new MimeMessage(session);

            message.setFrom(new InternetAddress("cerebros@cerebros-jobs.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(this.dest));

            message.setSubject(subject);
            message.setContent(this.contends, "text/html; charset=utf-8");

        } catch (MessagingException ex) {
            LOG.error(ex.getMessage());
        }
        
    }
    
    
   
}
