package com.gadget.rental.client;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(path = "/v1")
@RestController
public class ClientController {

    @GetMapping(path = "/test")
    ResponseEntity<String> getTest() {
        return ResponseEntity.ok("TESTING");
    }
}
