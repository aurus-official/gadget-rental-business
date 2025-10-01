package com.gadget.rental.auth.jwt;

import java.security.SecureRandom;
import java.util.UUID;

public class JwtKeyGenerator {

    public static byte[] generateJwtSecretKey() {
        byte[] secretKey = new byte[32];
        new SecureRandom().nextBytes(secretKey);

        return secretKey;
    }

    public static String generateJwtKeyId(String month, String year) {
        String keyId = String.format("key%s%s%s", month, year, UUID.randomUUID().toString());
        return keyId;
    }
}
