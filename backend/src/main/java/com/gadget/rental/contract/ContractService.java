package com.gadget.rental.contract;

import jakarta.mail.MessagingException;

import com.gadget.rental.shared.EmailSenderService;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ContractService {

    private EmailSenderService emailSenderService;

    public ContractService(EmailSenderService emailSenderService) {
        this.emailSenderService = emailSenderService;
    }

    public String sendContract(String receiverAddress, MultipartFile contract) {
        try {
            emailSenderService.sendContract(receiverAddress, contract);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return String.format("Contract for '%s' has been sent.", receiverAddress);
    }
}
