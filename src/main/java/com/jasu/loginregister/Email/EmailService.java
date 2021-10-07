package com.jasu.loginregister.Email;
import com.jasu.loginregister.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.List;
import java.util.Locale;


@Service
public class EmailService {


    @Autowired
    private JavaMailSender mailSender;

    public void sendAnEmail(String destinationEmail, String body, String subject) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setText(body);
        message.setSubject(subject);

        message.setTo(destinationEmail);
        mailSender.send(message);
        System.out.println("Mail Send..."+ destinationEmail+ " successfully");
    }

    public void sendMultipleEmail(List<String> listEmail, String body, String subject) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setText(body);
        message.setSubject(subject);

        for (String email: listEmail){
            message.setTo(email);
            mailSender.send(message);
            System.out.println("Mail Send..."+ " successfully");
        }
    }

    public void sendEmailWithAttachment(List<String> listEmail, String body, String subject, String attachment) throws MessagingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper
                = new MimeMessageHelper(mimeMessage, true);

        mimeMessageHelper.setText(body);
        mimeMessageHelper.setSubject(subject);

        FileSystemResource fileSystem
                = new FileSystemResource(new File(attachment));

        mimeMessageHelper.addAttachment(fileSystem.getFilename(),
                fileSystem);

        for (String email: listEmail){
            mimeMessageHelper.setTo(email);
            mailSender.send(mimeMessage);
            System.out.println("Mail Send..."+ email+  " successfully");
        }

    }

    public void sendHtmlEmail(List<String> listEmail, String body, String subject, String link) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();

        boolean multipart = true;

        MimeMessageHelper helper = new MimeMessageHelper(message, multipart, "utf-8");
        helper.setText(body);

        String htmlMsg = "<h3>Im testing send a HTML email</h3>"
                +"<img src='http://www.apache.org/images/asf_logo_wide.gif'>";
        //hien thi mo ta duong link

        message.setContent(htmlMsg, "text/html");//link ung dung
        //vi du link trong cay shopee

        helper.setSubject(subject);


        for (String email: listEmail){
            helper.setTo(email);
            mailSender.send(message);
            System.out.println("Mail Send..."+ email+ " successfully");
        }
    }

}