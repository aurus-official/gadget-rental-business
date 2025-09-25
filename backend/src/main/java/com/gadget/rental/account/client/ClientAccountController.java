package com.gadget.rental.account.client;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(path = "/v1")
@RestController
class ClientAccountController {

    private final ClientAccountService clientAccountService;

    @Autowired
    ClientAccountController(ClientAccountService clientAccountService) {
        this.clientAccountService = clientAccountService;
    }

    @PostMapping(path = "/users")
    ResponseEntity<String> registerClientAccount(@Valid @RequestBody ClientAccountDTO clientAccountDTO) {
        return ResponseEntity.ok(clientAccountService.addClientAccountAfterVerification(clientAccountDTO));
    }

    @GetMapping(path = "/users/{username}")
    ResponseEntity<String> seeYourNameForTestingOnly(@PathVariable(name = "username") String username) {
        return ResponseEntity.ok().body(username);
    }
}
