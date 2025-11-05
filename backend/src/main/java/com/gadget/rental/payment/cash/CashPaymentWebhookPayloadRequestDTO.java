package com.gadget.rental.payment.cash;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CashPaymentWebhookPayloadRequestDTO {
    private String id;
    private boolean isPaid;
    private String status;
    private String amount;
    private String currency;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String description;
    private PaymentFundSource fundSource;
    private PaymentReceipt receipt;
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

    public static class PaymentFundSource {
        private String type;
        private String id;
        private PaymentFundSourceDetails details;

        @JsonProperty("type")
        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        @JsonProperty("id")
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "PaymentFundSource [type=" + type + ", id=" + id + ", details="
                    + details + ", getType()=" + getType() + ", getId()=" + getId() + "]";
        }

        public PaymentFundSourceDetails getDetails() {
            return details;
        }

        public void setDetails(PaymentFundSourceDetails details) {
            this.details = details;
        }
    }

    public static class PaymentFundSourceDetails {
        private String scheme;

        @JsonProperty("scheme")
        public String getScheme() {
            return scheme;
        }

        public void setScheme(String scheme) {
            this.scheme = scheme;
        }

    }

    public static class PaymentReceipt {

        private String transactionId;
        private String receiptNo;

        @JsonProperty("transactionId")
        public String getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(String transactionId) {
            this.transactionId = transactionId;
        }

        @JsonProperty("receiptNo")
        public String getReceiptNo() {
            return receiptNo;
        }

        public void setReceiptNo(String receiptNo) {
            this.receiptNo = receiptNo;
        }

    }

    @Override
    public String toString() {
        return "CashPaymentWebhookPayloadRequestDTO [id=" + id + ", isPaid=" + isPaid + ", status=" + status
                + ", amount=" + amount + ", currency=" + currency + ", createdAt=" + createdAt + ", updatedAt="
                + updatedAt + ", description=" + description + ", fundSource=" + fundSource + ", receipt=" + receipt
                + ", receiptNumber=" + receiptNumber + ", requestReferenceNumber=" + requestReferenceNumber
                + ", getId()=" + getId() + ", isPaid()=" + isPaid() + ", getStatus()=" + getStatus() + ", getAmount()="
                + getAmount() + ", getCurrency()=" + getCurrency() + ", getCreatedAt()=" + getCreatedAt()
                + ", getUpdatedAt()=" + getUpdatedAt() + ", getDescription()=" + getDescription() + ", getFundSource()="
                + getFundSource() + ", getReceipt()=" + getReceipt() + ", getReceiptNumber()=" + getReceiptNumber()
                + ", getRequestReferenceNumber()=" + getRequestReferenceNumber() + "]";
    }

}
