package com.ferdev.cafe.Util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailUtils {

    @Autowired
    private JavaMailSender emailSender;

    public EmailUtils() throws MessagingException {
    }

    public void sendSimpleMessage(String to, String subject, String text, List<String> list) {
        SimpleMailMessage message= new SimpleMailMessage();
        message.setFrom("senderemailms@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        if (list != null && !list.isEmpty()) message.setCc(getCcArray(list));

        emailSender.send(message);
    }

    public String[] getCcArray(List<String> listCc) {
        String[] tempCc = new String[listCc.size()];

        for (int i = 0; i < listCc.size(); i++) {
            tempCc[i] = listCc.get(i);
        }
        return tempCc;
    }

    public void forgotMail(String to, String subject, String password) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom("senderemailms@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        String htmlMsg="<p><b>Your login credentials for Cafe Management System</b><br><b>Email: </b> " + to
                        + " <br><b>Password: </b> " + password
                        + "<br><a href=\"http://localhost:4200/\">Click here to login</a></p>";
        message.setContent(htmlMsg, "text/html");
        emailSender.send(message);
    }

}

