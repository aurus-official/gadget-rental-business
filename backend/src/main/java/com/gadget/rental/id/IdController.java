package com.gadget.rental.id;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/v1")
public class IdController {

    private IdService idService;

    public IdController(IdService idService) {
        this.idService = idService;
    }

    @GetMapping(path = "/id-approvals/{requestReferenceNumber}")
    ResponseEntity<List<byte[]>> getIdImagesByRequestReferenceNumber(
            @PathVariable("requestReferenceNumber") String requestReferenceNumber) {
        List<byte[]> idImages = idService.getIdImagesByRequestReferenceNumber(requestReferenceNumber);

        return ResponseEntity.ok(idImages);
    }

    @PostMapping(path = "/id-approvals/{requestReferenceNumber}")
    ResponseEntity<String> approveBookingByRequestReferenceNumber(
            @PathVariable("requestReferenceNumber") String requestReferenceNumber) {
        System.out.println(requestReferenceNumber);
        String status = idService.approveBookingByRequestReferenceNumber(requestReferenceNumber);

        return ResponseEntity.ok(status);
    }
}
