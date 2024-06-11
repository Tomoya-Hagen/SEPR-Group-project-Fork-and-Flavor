package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import at.ac.tuwien.sepr.groupphase.backend.config.MailProperties;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MailProperties mailProperties;

    @Override
    public void sendSimpleEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Fork And Flavour " + subject);
        message.setText("Hallo werter Benutzer! \n \n" + text + "\n \nAutomatisierte Grüße, \ndein javamail.JavaMailSender \n" + mailProperties.getFrontendUrl() + " \n \nPS: Bitte antworten Sie nicht auf dieses Email.");
        message.setFrom(mailProperties.getFromAddress());

        mailSender.send(message);
    }

}
