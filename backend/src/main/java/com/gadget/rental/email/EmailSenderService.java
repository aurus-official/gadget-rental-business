package com.gadget.rental.email;

import jakarta.mail.Message.RecipientType;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

    private JavaMailSender emailSender;

    @Autowired
    public EmailSenderService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Async
    public void sendClientVerificationCode(String receiverAddress, String code) throws MessagingException {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        mimeMessage.setFrom("gadgetrentalmock@gmail.com");
        mimeMessage.setRecipient(RecipientType.TO, new InternetAddress(receiverAddress));
        mimeMessage.setSubject("Verification Code");
        String content = String.format(
                """
                        <div style="padding-top:2em; width:100%%">
                            <div>
                                <h1 style="text-align: left; padding: 0; margin: 0;">You Are Almost There!</h1>
                                <div style="height: 80%% ; width: 100%%; display: flex; align-items: center; flex-direction: column;">
                                    <h6 style="padding-top: 0.5em;font-weight:normal; font-size: 0.8rem">
                                        Please verify your client account with the 6-digit code below. <br />
                                        This verification code will expire in 3 minutes for your security. <br />
                                        If you didn't request sign up for this account, you can safely ignore this email.
                                    </h6>
                                </div>
                            </div>
                            <h4 style="color: white; width: 70%% ; margin: 0; padding: 2em 0; background-color: #7b2d26; letter-spacing: 1em; font-size: 1.75rem; text-align: center; font-weight: bolder;">%s</h4>
                        </div>
                        """,
                code);
        mimeMessage.setContent(content, "text/html; charset=utf-8");
        emailSender.send(mimeMessage);
    }

    @Async
    public void sendAdminVerificationCode(String code) throws MessagingException {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        mimeMessage.setFrom("gadgetrentalmock@gmail.com");
        mimeMessage.setRecipient(RecipientType.TO, new InternetAddress("gadgetrentalmock@gmail.com"));
        mimeMessage.setSubject("Verification Code");
        String content = String.format(
                """
                        <div style="padding-top:2em; width:100%%">
                            <div>
                                <h1 style="text-align: left; padding: 0; margin: 0;">You Are Almost There!</h1>
                                <div style="height: 80%% ; width: 100%%; display: flex; align-items: center; flex-direction: column;">
                                    <h6 style="padding-top: 0.5em;font-weight:normal; font-size: 0.8rem">
                                        Please verify your admin account with the 6-digit code below. <br />
                                        This verification code will expire in 3 minutes for your security. <br />
                                        If you didn't request sign up for this account, you can safely ignore this email.
                                    </h6>
                                </div>
                            </div>
                            <h4 style="color: white; width: 70%% ; margin: 0; padding: 2em 0; background-color: #7b2d26; letter-spacing: 1em; font-size: 1.75rem; text-align: center; font-weight: bolder;">%s</h4>
                        </div>
                        """,
                code);
        mimeMessage.setContent(content, "text/html; charset=utf-8");
        emailSender.send(mimeMessage);
    }
}
