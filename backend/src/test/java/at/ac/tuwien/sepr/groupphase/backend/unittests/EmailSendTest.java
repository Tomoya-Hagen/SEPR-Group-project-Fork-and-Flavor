package at.ac.tuwien.sepr.groupphase.backend.unittests;

import at.ac.tuwien.sepr.groupphase.backend.service.EmailService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles({"test"})
@Transactional
public class EmailSendTest {

    @MockBean
    private EmailService emailService;

    @Test
    public void sendTestMail() {
        String to = "user@email.com";
        String subject = "Test";
        String text = "This is a test email";

        emailService.sendSimpleEmail(to, subject, text);

        Mockito.verify(emailService, Mockito.times(1)).sendSimpleEmail(to, subject, text);
    }
}
