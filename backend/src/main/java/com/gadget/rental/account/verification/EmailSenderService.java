package com.gadget.rental.account.verification;

import jakarta.mail.Message.RecipientType;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

    private JavaMailSender emailSender;

    @Value("${admin.mail.address}")
    private String adminGmail;

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
                            <h3 style="font-weight:normal;padding:0; margin:0;">Code : </h3>
                            <h2 style="margin:0; padding:0;">%s</h2>
                        </div>
                        """,
                code);
        mimeMessage.setContent(content, "text/html; charset=utf-8");
        emailSender.send(mimeMessage);
    }

    @Async
    public void sendAdminVerificationCode(String code) throws MessagingException {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        mimeMessage.setFrom(adminGmail);
        mimeMessage.setRecipient(RecipientType.TO, new InternetAddress(adminGmail));
        mimeMessage.setSubject("Verification Code");
        String content = String.format(
                """
                        <div style="padding-top:1em; width:100%%">
                            <div>
                                <h1 style="text-align: left; padding: 0; margin: 0;">You Are Almost There!</h1>
                                <div style="height: 50%% ; width: 100%%; display: flex; align-items: center; flex-direction: column;">
                                    <h6 style="font-weight:normal; font-size: 0.8rem">
                                        Please verify your admin account with the 6-digit code below. <br />
                                        This verification code will expire in 3 minutes for your security. <br/>
                                        If you didn't request sign up for this account, you can safely ignore this email.
                                    </h6>
                                </div>
                            </div>
                            <h3 style="font-weight:normal;padding:0; margin:0;">Code : </h3>
                            <h2 style="margin:0; padding:0;">%s</h2>
                        </div>
                        """,
                code);
        mimeMessage.setContent(content, "text/html; charset=utf-8");
        emailSender.send(mimeMessage);
    }
}
