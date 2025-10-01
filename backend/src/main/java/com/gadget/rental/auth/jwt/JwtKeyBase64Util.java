package com.gadget.rental.auth.jwt;

import java.util.Base64;

public class JwtKeyBase64Util {

    public static byte[] decodeJwtSecretKey(String keyString) {
        return Base64.getDecoder().decode(keyString);
    }

    public static String encodeJwtSecretKey(byte[] keyByte) {
        return Base64.getEncoder().encodeToString(keyByte);
    }

}
