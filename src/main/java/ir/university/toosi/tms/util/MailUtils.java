package ir.university.toosi.tms.util;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by farzad on 7/26/14.
 */
public class MailUtils {
    public static void sendEmail(String userEmailAddress, String content, String subject) {
        Properties props = System.getProperties();
        String host = "smtp.gmail.com";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", "mahshid.danapour@gmail.com");
        props.put("mail.smtp.password", "!2945230100@");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);


        try {
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(userEmailAddress));
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        try {
            message.setSubject(subject);
            message.setText(content);
            Transport transport;
            transport = session.getTransport("smtp");
            transport.connect(host, "mahshid.danapour@gmail.com", "!2945230100@");
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (MessagingException e) {
           e.printStackTrace();
        }
    }
}
