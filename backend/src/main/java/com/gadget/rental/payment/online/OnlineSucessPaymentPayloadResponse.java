package com.gadget.rental.payment.online;

public class OnlineSucessPaymentPayloadResponse {

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

    @Override
    public String toString() {
        return "CheckoutResponse{" +
                "checkoutId='" + checkoutId + '\'' +
                ", redirectUrl='" + redirectUrl + '\'' +
                '}';
    }

}
