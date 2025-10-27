package com.gadget.rental.shared;

import java.util.Base64;

public class Base64Util {
    public static byte[] decodeBase64(String cred) {
        return Base64.getDecoder().decode(cred);
    }

    public static String encodeBase64(byte[] credByte) {
        return Base64.getEncoder().encodeToString(credByte);
    }
}
