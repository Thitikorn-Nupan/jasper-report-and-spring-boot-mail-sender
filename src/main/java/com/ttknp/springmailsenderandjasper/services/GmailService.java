package com.ttknp.springmailsenderandjasper.services;

import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;

/**
     if you wanna use this Service (Bean) you have to inject first
     The EmailSender class has a constructor that takes an instance of JavaMailSender as a parameter.
     It also has a sendEmail method that sends an email with the specified email address, subject, and content.
     We utilize the MimeMessageHelper class to set up the email message
     <br>
     <h3>About class</h3>
     MailSender interface: the top-level interface that provides basic functionality for sending simple emails
     JavaMailSender interface: the subinterface of the above MailSender. It supports MIME messages and is mostly used in conjunction with the MimeMessageHelper class for the creation of a MimeMessage. It’s recommended to use the MimeMessagePreparator mechanism with this interface.
     JavaMailSenderImpl class provides an implementation of the JavaMailSender interface. It supports the MimeMessage and SimpleMailMessage.
     SimpleMailMessage class: used to create a simple mail message including the from, to, cc, subject and text fields
     MimeMessagePreparator interface provides a callback interface for the preparation of MIME messages.
     MimeMessageHelper class: helper class for the creation of MIME messages. It offers support for images, typical mail attachments and text content in an HTML layout.
*/
@Service
public class GmailService {

    private static Logger log = LoggerFactory.getLogger(GmailService.class);
    private final JavaMailSender mailSender; // this dependency have to inject before uses
    private final OrderItemService orderItemService;
    private final String FROM_SMTP = "thitikorn-n@rmutp.ac.th";
    private final String FROM_PERSONAL = "THITIKORN NUPAN (OWN BOT)";

    @Autowired
    public GmailService(JavaMailSender mailSender, OrderItemService orderItemService) {
        this.mailSender = mailSender;
        this.orderItemService = orderItemService;
    }

    public Boolean sendEmailContentAsPdfFile(String subject, String email) {
        try {
            long startTime = System.nanoTime();
            // ** work with JavaMailSender
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            // ** set up email below
            messageHelper.setTo(email);
            // Pass 'true' for HTML content
            messageHelper.setText("Sending The Pdf File", true);
            messageHelper.setFrom(FROM_SMTP, FROM_PERSONAL);
            messageHelper.setSubject(subject);
            HashMap<String, byte[]> pdfFile = orderItemService.getOrderItemsHasMapReport();
            List<String> keySet = pdfFile.keySet().stream().toList();
            // Add the byte array as an attachment
            messageHelper.addAttachment(keySet.get(0), new ByteArrayResource(pdfFile.get(keySet.get(0))));
            mailSender.send(mimeMessage);
            long endTime = System.nanoTime();
            long duration = (endTime - startTime);
            long durationInMs = duration / 1000000;
            log.debug("durationInMs => {}",durationInMs); // durationInMs => (about) 54244 if i use thread it will take (about) 23 because it's no need to wait response of mail
            return true;
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            return false;
        }

    }

}
