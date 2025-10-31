package com.gadget.rental.account.admin;

import com.gadget.rental.auth.verification.EmailVerificationModel;
import com.gadget.rental.auth.verification.EmailVerificationRepository;
import com.gadget.rental.exception.AccountCreationTokenMismatchException;
import com.gadget.rental.exception.EmailAlreadyBoundException;
import com.gadget.rental.exception.EmailNotVerifiedException;
import com.gadget.rental.exception.EmailVerificationRequestNotFoundException;
import com.gadget.rental.exception.EmailVerificationRoleMismatchException;
import com.gadget.rental.shared.AccountDTO;
import com.gadget.rental.shared.AccountType;

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

    public String addAdminAfterVerification(AccountDTO accountDTO, String authHeader) {

        EmailVerificationModel matchingEmail = emailVerificationRepository
                .findEmailVerificationByEmail(accountDTO.email())
                .orElseThrow(() -> new EmailVerificationRequestNotFoundException(
                        "This email is not associated to any verification."));

        if (matchingEmail.isLinked()) {
            throw new EmailAlreadyBoundException("This email is linked to another account.");
        }

        if (!(matchingEmail.isVerified())) {
            throw new EmailNotVerifiedException("This email is not verified.");
        }

        if (!(matchingEmail.isAuthTokenMatched(authHeader))) {
            throw new AccountCreationTokenMismatchException("Token mismatch, please try registering again.");
        }

        if (!matchingEmail.isAccountTypeMatched(AccountType.ADMIN)) {
            throw new EmailVerificationRoleMismatchException(
                    "Role provided during account creation does not match the expected role.");
        }

        emailVerificationRepository.updateEmailVerificationIsLinked(true, matchingEmail.getEmail());
        AdminAccountModel adminAccountModel = new AdminAccountModel();
        adminAccountModel.setEmail(accountDTO.email());
        adminAccountModel.setAdminEmail(adminGmail);
        adminAccountModel.setPassword(bCryptPasswordEncoder.encode(accountDTO.password()));
        adminAccountRepository.save(adminAccountModel);

        return String.format("Admin '%s' has successfully added.", adminAccountModel.getEmail());
    }
}
