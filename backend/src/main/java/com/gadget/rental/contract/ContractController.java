package com.gadget.rental.contract;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "/v1")
public class ContractController {
    private ContractService contractService;

    ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    @PostMapping(path = "/contract")
    ResponseEntity<String> approveBookingByRequestReferenceNumber(
            @RequestPart("contract") MultipartFile contract, @RequestPart("email") String email) {
        String status = contractService.sendContract(email, contract);
        return ResponseEntity.ok(status);
    }
}
