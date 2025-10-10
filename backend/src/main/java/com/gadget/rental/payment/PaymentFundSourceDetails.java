package com.gadget.rental.payment;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentFundSourceDetails {
    private String scheme;
    private String last4;
    private String first6;
    private String masked;
    private String issuer;

    @JsonProperty("scheme")
    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    @JsonProperty("last4")
    public String getLast4() {
        return last4;
    }

    public void setLast4(String last4) {
        this.last4 = last4;
    }

    @JsonProperty("first6")
    public String getFirst6() {
        return first6;
    }

    public void setFirst6(String first6) {
        this.first6 = first6;
    }

    @JsonProperty("masked")
    public String getMasked() {
        return masked;
    }

    public void setMasked(String masked) {
        this.masked = masked;
    }

    @JsonProperty("issuer")
    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }
}
