package com.gadget.rental.payment;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentPayload {

    private String id;
    private boolean isPaid;
    private String status;
    private String amount;
    private String currency;
    private boolean canVoid;
    private boolean canRefund;
    private boolean canCapture;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String description;
    private String paymentTokenId;
    private PaymentFundSource fundSource;
    private PaymentReceipt receipt;
    private PaymentMetadata metadata;
    private String approvalCode;
    private String receiptNumber;
    private String requestReferenceNumber;

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("isPaid")
    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("amount")
    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @JsonProperty("currency")
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @JsonProperty("canVoid")
    public boolean isCanVoid() {
        return canVoid;
    }

    public void setCanVoid(boolean canVoid) {
        this.canVoid = canVoid;
    }

    @JsonProperty("canRefund")
    public boolean isCanRefund() {
        return canRefund;
    }

    public void setCanRefund(boolean canRefund) {
        this.canRefund = canRefund;
    }

    @JsonProperty("canCapture")
    public boolean isCanCapture() {
        return canCapture;
    }

    public void setCanCapture(boolean canCapture) {
        this.canCapture = canCapture;
    }

    @JsonProperty("createdAt")
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @JsonProperty("updatedAt")
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("paymentTokenId")
    public String getPaymentTokenId() {
        return paymentTokenId;
    }

    public void setPaymentTokenId(String paymentTokenId) {
        this.paymentTokenId = paymentTokenId;
    }

    @JsonProperty("fundSource")
    public PaymentFundSource getFundSource() {
        return fundSource;
    }

    public void setFundSource(PaymentFundSource fundSource) {
        this.fundSource = fundSource;
    }

    @JsonProperty("receipt")
    public PaymentReceipt getReceipt() {
        return receipt;
    }

    public void setReceipt(PaymentReceipt receipt) {
        this.receipt = receipt;
    }

    @JsonProperty("metadata")
    public PaymentMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(PaymentMetadata metadata) {
        this.metadata = metadata;
    }

    @JsonProperty("approvalCode")
    public String getApprovalCode() {
        return approvalCode;
    }

    public void setApprovalCode(String approvalCode) {
        this.approvalCode = approvalCode;
    }

    @JsonProperty("receiptNumber")
    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    @JsonProperty("requestReferenceNumber")
    public String getRequestReferenceNumber() {
        return requestReferenceNumber;
    }

    public void setRequestReferenceNumber(String requestReferenceNumber) {
        this.requestReferenceNumber = requestReferenceNumber;
    }

    @Override
    public String toString() {
        return "PaymentPayload [id=" + id + ", isPaid=" + isPaid + ", status=" + status + ", amount=" + amount
                + ", canVoid=" + canVoid + ", canRefund=" + canRefund + ", canCapture=" + canCapture + ", createdAt="
                + createdAt + ", updatedAt=" + updatedAt + ", description=" + description + ", paymentTokenId="
                + paymentTokenId + ", fundSource=" + fundSource + ", receipt=" + receipt + ", metadata=" + metadata
                + ", approvalCode=" + approvalCode + ", receiptNumber=" + receiptNumber + ", requestReferenceNumber="
                + requestReferenceNumber + ", getId()=" + getId() + ", isPaid()=" + isPaid() + ", getStatus()="
                + getStatus() + ", getAmount()=" + getAmount() + ", getCurrency()=" + getCurrency() + ", getClass()="
                + getClass() + ", isCanVoid()=" + isCanVoid() + ", isCanRefund()=" + isCanRefund() + ", isCanCapture()="
                + isCanCapture() + ", getCreatedAt()=" + getCreatedAt() + ", getUpdatedAt()=" + getUpdatedAt()
                + ", getDescription()=" + getDescription() + ", getPaymentTokenId()=" + getPaymentTokenId()
                + ", getFundSource()=" + getFundSource() + ", getReceipt()=" + getReceipt() + ", getMetadata()="
                + getMetadata() + ", hashCode()=" + hashCode() + ", getApprovalCode()=" + getApprovalCode()
                + ", getReceiptNumber()=" + getReceiptNumber() + ", getRequestReferenceNumber()="
                + getRequestReferenceNumber() + "]";
    }
}
