package com.gadget.rental.payment;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentFundSource {
    private String type;
    private String id;
    private String description;
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

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("details")
    public PaymentFundSourceDetails getDetails() {
        return details;
    }

    public void setDetails(PaymentFundSourceDetails details) {
        this.details = details;
    }
}
