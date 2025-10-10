package com.gadget.rental.payment;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentReceipt {

    private String transactionId;
    private String receiptNo;
    private String approvalCode;
    private String approvalCodeDuplicate;

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

    @JsonProperty("approval_code")
    public String getApprovalCode() {
        return approvalCode;
    }

    public void setApprovalCode(String approvalCode) {
        this.approvalCode = approvalCode;
    }

    @JsonProperty("approvalCode")
    public String getApprovalCodeDuplicate() {
        return approvalCodeDuplicate;
    }

    public void setApprovalCodeDuplicate(String approvalCodeDuplicate) {
        this.approvalCodeDuplicate = approvalCodeDuplicate;
    }
}
