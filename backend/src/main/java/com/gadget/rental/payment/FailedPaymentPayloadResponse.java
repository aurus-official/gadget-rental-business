package com.gadget.rental.payment;

import java.util.List;

public class FailedPaymentPayloadResponse {

    private String code;
    private String message;
    private List<ErrorParameter> parameters;

    public FailedPaymentPayloadResponse(String code, String message, List<ErrorParameter> parameters) {
        this.code = code;
        this.message = message;
        this.parameters = parameters;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ErrorParameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<ErrorParameter> parameters) {
        this.parameters = parameters;
    }

    public static class ErrorParameter {

        private String description;
        private String field;

        public ErrorParameter(String description, String field) {
            this.description = description;
            this.field = field;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }
    }

}
