package com.gadget.rental.client;

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
class ClientController {

    private final ClientService clientService;

    @Autowired
    ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping(path = "/test")
    ResponseEntity<String> getTest() {
        return ResponseEntity.ok("TESTING");
    }

    @PostMapping(path = "/users")
    ResponseEntity<String> registerNewClientAccount(@Valid @RequestBody ClientDTO clientDTO) {
        return ResponseEntity.ok(clientService.addNewClientAfterVerification(clientDTO));
    }

    @GetMapping(path = "/users/{username}")
    ResponseEntity<String> seeYourNameForTestingOnly(@PathVariable(name = "username") String username) {
        return ResponseEntity.ok().body(username);
    }
}
