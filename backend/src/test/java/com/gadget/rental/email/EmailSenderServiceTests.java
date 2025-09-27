package com.gadget.rental.email;

import java.util.Properties;

import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gadget.rental.auth.verification.EmailSenderService;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class EmailSenderServiceTests {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailSenderService emailSenderService;

    @Test
    void sendEmail_shouldCallSendMimeMail() throws MessagingException {
        MimeMessage mimeMessage = new MimeMessage(Session.getDefaultInstance(new Properties()));
        Mockito.when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        emailSenderService.sendClientVerificationCode("me@gmail.com", "555555");
        Mockito.verify(javaMailSender).send(Mockito.any(MimeMessage.class));
    }
}
