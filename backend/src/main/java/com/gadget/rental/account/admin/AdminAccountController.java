package com.gadget.rental.account.admin;

import jakarta.validation.Valid;

import com.gadget.rental.shared.AccountDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/v1")
public class AdminAccountController {

    private final AdminAccountService adminAccountService;

    @Autowired
    AdminAccountController(AdminAccountService adminAccountService) {
        this.adminAccountService = adminAccountService;
    }

    @PostMapping(path = "/admins")
    ResponseEntity<String> createAdminAccount(@Valid @RequestBody AccountDTO accountDTO,
            @RequestHeader(name = "Authorization") String authHeader) {
        return ResponseEntity.ok(adminAccountService.addAdminAfterVerification(accountDTO, authHeader));
    }
}
