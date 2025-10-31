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

    @Column(name = "payment_scheme")
    private String paymentScheme;

    @Column(name = "total_price")
    private double totalPrice;

    @Column(name = "transaction_reference_number")
    private String transactionReferenceNumber;

    @Column(name = "request_reference_number")
    private String requestReferenceNumber;

    @Column(name = "checkout_id")
    private String checkoutId;

    @Column(name = "receipt_number")
    private String receiptNumber;

    @Column(name = "status")
    private PaymentStatus status;

    @Column(name = "email")
    private String email;

    @Column(name = "currency")
    private String currency;

    @Column(name = "payment_initiated_at")
    private LocalDateTime paymentInitiatedAt;

    @Column(name = "last_update")
    private LocalDateTime lastUpdate;

    public Long getId() {
        return id;
    }

    public String getPaymentScheme() {
        return paymentScheme;
    }

    public void setPaymentScheme(String paymentScheme) {
        this.paymentScheme = paymentScheme;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public LocalDateTime getPaymentInitiatedAt() {
        return paymentInitiatedAt;
    }

    public void setPaymentInitiatedAt(LocalDateTime paymentInitiatedAt) {
        this.paymentInitiatedAt = paymentInitiatedAt;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransactionReferenceNumber() {
        return transactionReferenceNumber;
    }

    public void setTransactionReferenceNumber(String transactionReferenceNumber) {
        this.transactionReferenceNumber = transactionReferenceNumber;
    }

    public String getRequestReferenceNumber() {
        return requestReferenceNumber;
    }

    public void setRequestReferenceNumber(String requestReferenceNumber) {
        this.requestReferenceNumber = requestReferenceNumber;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public String getCheckoutId() {
        return checkoutId;
    }

    public void setCheckoutId(String checkoutId) {
        this.checkoutId = checkoutId;
    }
}
