package com.gadget.rental.account.admin;

import java.util.Optional;

import com.gadget.rental.account.verification.EmailVerificationModel;
import com.gadget.rental.account.verification.EmailVerificationRepository;
import com.gadget.rental.account.verification.EmailVerificationType;
import com.gadget.rental.exception.EmailAlreadyBoundException;
import com.gadget.rental.exception.EmailNotVerifiedException;
import com.gadget.rental.exception.EmailVerificationRequestNotExistedException;
import com.gadget.rental.exception.EmailVerificationRoleMismatchException;
import com.gadget.rental.exception.TokenMismatchException;
import com.gadget.rental.exception.UsernameDuplicateException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminAccountService {
    private final AdminAccountRepository adminAccountRepository;
    private final EmailVerificationRepository emailVerificationRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${admin.mail.address}")
    private String adminGmail;

    @Autowired
    AdminAccountService(AdminAccountRepository adminAccountRepository,
            EmailVerificationRepository emailVerificationRepository,
            BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.adminAccountRepository = adminAccountRepository;
        this.emailVerificationRepository = emailVerificationRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public String addAdminAfterVerification(AdminAccountDTO adminDTO) {

        EmailVerificationModel matchingEmail = emailVerificationRepository
                .findEmailVerificationByEmail(adminDTO.email())
                .orElseThrow(() -> new EmailVerificationRequestNotExistedException(
                        "This email is not associated to any verification."));

        if (!matchingEmail.isAccountTypeMatched(EmailVerificationType.ADMIN)) {
            throw new EmailVerificationRoleMismatchException(
                    "Role provided during account creation does not match the expected role.");
        }

        if (matchingEmail.isLinked()) {
            throw new EmailAlreadyBoundException("This email is linked to another account.");
        }

        if (!(matchingEmail.isVerified())) {
            throw new EmailNotVerifiedException("This email is not verified.");
        }

        if (adminDTO.token().compareTo(matchingEmail.getTokenAccountCreation()) != 0) {
            throw new TokenMismatchException("Token mismatch, please try registering again.");
        }

        Optional<AdminAccountModel> matchedAdmin = adminAccountRepository
                .findAdminAccountByUsername(adminDTO.username());

        if (matchedAdmin.isPresent()) {
            throw new UsernameDuplicateException("This username is already taken.");
        }

        emailVerificationRepository.updateEmailVerificationIsLinked(true, matchingEmail.getEmail());
        AdminAccountModel adminAccountModel = new AdminAccountModel();
        adminAccountModel.setUsername(adminDTO.username());
        adminAccountModel.setEmail(adminDTO.email());
        adminAccountModel.setAdminEmail(adminGmail);
        adminAccountModel.setPassword(bCryptPasswordEncoder.encode(adminDTO.password()));
        adminAccountRepository.save(adminAccountModel);

        return String.format("Admin '%s' has successfully added.", adminAccountModel.getUsername());
    }
}
