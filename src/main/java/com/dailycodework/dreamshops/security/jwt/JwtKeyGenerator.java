package com.dailycodework.dreamshops.security.jwt;

import java.security.SecureRandom;
import java.util.Base64;

public class JwtKeyGenerator {
    public static String generateKey() {
        SecureRandom random = new SecureRandom();
        byte[] keyBytes = new byte[32]; // 256 бит
        random.nextBytes(keyBytes);
        return Base64.getEncoder().encodeToString(keyBytes);
    }
}
