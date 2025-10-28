package com.gadget.rental.payment;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name = "payment_transaction_info")
@Table(name = "payment_transaction_info")
public class PaymentTransactionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "total_price")
    private double totalPrice;

    @Column(name = "reference_number")
    private String referenceNumber;

    @Column(name = "status")
    private PaymentStatus status;

    @Column(name = "email")
    private String email;

    @Column(name = "currency")
    private String currency;

    @Column(name = "valid_from")
    private LocalDateTime validFrom;

    @Column(name = "last_update")
    private LocalDateTime lastUpdate;
}
