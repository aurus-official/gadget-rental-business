package com.gadget.rental.email;

import com.gadget.rental.auth.AuthUserDetailsService;
import com.gadget.rental.auth.jwt.JwtUtil;
import com.gadget.rental.auth.verification.EmailVerificationController;
import com.gadget.rental.auth.verification.EmailVerificationRepository;
import com.gadget.rental.auth.verification.EmailVerificationService;
import com.gadget.rental.configuration.SecurityConfig;
import com.gadget.rental.exception_handler.GlobalExceptionHandler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(EmailVerificationController.class)
@ContextConfiguration(classes = { SecurityConfig.class, EmailVerificationController.class })
@Import(GlobalExceptionHandler.class)
@ActiveProfiles("test")
public class EmailVerificationControllerTests {

    private MockMvc mockMvc;

    @MockitoBean
    EmailVerificationService emailVerificationService;

    @MockitoBean
    EmailVerificationRepository emailVerificationRepository;

    @Autowired
    WebApplicationContext webApplicationContext;

    @MockitoBean
    AuthUserDetailsService authUserDetailsService;

    @MockitoBean
    JwtUtil jwtUtil;

    @MockitoBean
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @BeforeEach
    void setupMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity()).build();
    }

    @Test
    void createEmailVerification_WithValidValuesReturnOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/client/email-verification-requests")
                .content("{\"email\" : \"test@gmail.com\", \"timezone\" : \" Asia/Singapore\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void createEmailVerification_WithInvalidValueReturnBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/client/email-verification-requests")
                .content("{\"email\" : \"test\", \"timezone\" : \" Asia/Singapore\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void createEmailVerification_WithMissingValueReturnBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/client/email-verification-requests")
                .content("{\"email\" : \"test\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void createEmailVerification_WithNullValuesReturnBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/client/email-verification-requests")
                .content("{}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void verifyEmailVerification_WithValidValuesReturnOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/client/email-verification")
                .content("{\"email\" : \"test@gmail.com\", \"code\" : \"666666\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void verifyEmailVerification_WithInvalidValuesReturnBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/client/email-verification")
                .content("{\"email\" : \"test\", \"code\" : \"6666666\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void verifyEmailVerification_WithMissingValueReturnBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/client/email-verification")
                .content("{\"email\" : \"test\", \"code\" : \"\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void verifyEmailVerification_WithNullValuesReturnBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/client/email-verification")
                .content("{}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void resendEmailVerification_WithValidValuesReturnOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/client/email-verification-requests/resend")
                .content("{\"email\" : \"test@gmail.com\", \"timezone\" : \" Asia/Singapore\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void resendEmailVerification_WithInvalidValueReturnBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/client/email-verification-requests/resend")
                .content("{\"email\" : \"test\", \"timezone\" : \" Asia/Singapore\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void resendEmailVerification_WithMissingValueReturnBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/client/email-verification-requests/resend")
                .content("{\"email\" : \"test\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void resendEmailVerification_WithNullValuesReturnBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/client/email-verification-requests/resend")
                .content("{}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
