package com.gadget.rental.account.client;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/v1")
class ClientAccountController {

    private final ClientAccountService clientAccountService;

    @Autowired
    ClientAccountController(ClientAccountService clientAccountService) {
        this.clientAccountService = clientAccountService;
    }

    @PostMapping(path = "/client")
    ResponseEntity<String> registerClientAccount(@Valid @RequestBody ClientAccountDTO clientAccountDTO,
            @RequestHeader(name = "Authorization") String authHeader) {
        return ResponseEntity.ok(clientAccountService.addClientAccountAfterVerification(clientAccountDTO, authHeader));
    }

    @GetMapping(path = "/client/{username}")
    ResponseEntity<String> seeYourNameForTestingOnly(@PathVariable(name = "username") String username) {
        return ResponseEntity.ok().body(username);
    }
}
