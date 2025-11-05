package com.gadget.rental.payment.online;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gadget.rental.exception.CheckoutFailedException;

import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

public class OnlinePaymentResponseErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return (response.getStatusCode().is4xxClientError() ||
                response.getStatusCode().is5xxServerError());
    }

    @Override
    public void handleError(URI url, HttpMethod method, ClientHttpResponse response) throws IOException {
        String body = new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            OnlineFailedPaymentPayloadResponseDTO failedPaymentPayloadResponse = objectMapper.readValue(body,
                    OnlineFailedPaymentPayloadResponseDTO.class);
            throw new CheckoutFailedException(failedPaymentPayloadResponse.getMessage());

        } catch (Exception e) {
            throw new CheckoutFailedException(body);
        }
    }

}
