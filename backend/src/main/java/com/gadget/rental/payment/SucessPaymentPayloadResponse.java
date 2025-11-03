package com.gadget.rental.payment;

public class SucessPaymentPayloadResponse {

    private String checkoutId;
    private String redirectUrl;

    public String getCheckoutId() {
        return checkoutId;
    }

    public void setCheckoutId(String checkoutId) {
        this.checkoutId = checkoutId;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    // Override toString() method for easier printing (optional)
    @Override
    public String toString() {
        return "CheckoutResponse{" +
                "checkoutId='" + checkoutId + '\'' +
                ", redirectUrl='" + redirectUrl + '\'' +
                '}';
    }

}
