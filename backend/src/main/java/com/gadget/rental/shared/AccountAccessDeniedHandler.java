package com.gadget.rental.shared;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

public class AccountAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {
        String errorMessage = ErrorMessageBodyUtil.generateErrorMessageBody("Access denied.",
                HttpStatus.FORBIDDEN);
        response.getWriter().write(errorMessage);
        response.setStatus(HttpStatus.FORBIDDEN.value());
    }
}
