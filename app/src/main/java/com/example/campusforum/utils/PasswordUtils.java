package com.example.campusforum.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Locale;

public final class PasswordUtils {

    private static final String HASH_ALGORITHM = "SHA-256";
    private static final String HASH_PREFIX = "sha256";
    private static final String HASH_SEPARATOR = ":";
    private static final int SALT_LENGTH_BYTES = 16;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private PasswordUtils() {
    }

    public static String hashPassword(String password) {
        return hashValue(password);
    }

    public static boolean verifyPassword(String password, String storedHash) {
        return verifyValue(password, storedHash);
    }

    public static String hashSecurityAnswer(String answer) {
        return hashValue(normalizeSecurityAnswer(answer));
    }

    public static boolean verifySecurityAnswer(String answer, String storedHash) {
        return verifyValue(normalizeSecurityAnswer(answer), storedHash);
    }

    private static boolean verifyValue(String rawValue, String storedHash) {
        if (rawValue == null || storedHash == null || storedHash.isEmpty()) {
            return false;
        }

        String[] parts = storedHash.split(HASH_SEPARATOR);
        if (parts.length != 3 || !HASH_PREFIX.equals(parts[0])) {
            return false;
        }

        byte[] salt = hexToBytes(parts[1]);
        byte[] inputHash = hexToBytes(hashValue(rawValue, salt));
        byte[] expectedHash = hexToBytes(parts[2]);
        return inputHash.length == expectedHash.length && MessageDigest.isEqual(inputHash, expectedHash);
    }

    private static String hashValue(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Value to hash must not be null");
        }

        byte[] salt = new byte[SALT_LENGTH_BYTES];
        SECURE_RANDOM.nextBytes(salt);
        return HASH_PREFIX + HASH_SEPARATOR + bytesToHex(salt) + HASH_SEPARATOR + hashValue(value, salt);
    }

    private static String hashValue(String value, byte[] salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
            digest.update(salt);
            byte[] hashedBytes = digest.digest(value.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(HASH_ALGORITHM + " is not available", e);
        }
    }

    private static String normalizeSecurityAnswer(String answer) {
        if (answer == null) {
            throw new IllegalArgumentException("Security answer must not be null");
        }
        return answer.trim().toLowerCase(Locale.ROOT);
    }

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        char[] hexAlphabet = "0123456789abcdef".toCharArray();
        int charIndex = 0;
        for (byte value : bytes) {
            int unsignedValue = value & 0xff;
            hexChars[charIndex++] = hexAlphabet[unsignedValue >>> 4];
            hexChars[charIndex++] = hexAlphabet[unsignedValue & 0x0f];
        }
        return new String(hexChars);
    }

    private static byte[] hexToBytes(String hex) {
        if (hex.length() % 2 != 0) {
            return new byte[0];
        }

        byte[] bytes = new byte[hex.length() / 2];
        for (int index = 0; index < hex.length(); index += 2) {
            int byteIndex = index / 2;
            int high = Character.digit(hex.charAt(index), 16);
            int low = Character.digit(hex.charAt(index + 1), 16);
            if (high < 0 || low < 0) {
                return new byte[0];
            }
            bytes[byteIndex] = (byte) ((high << 4) + low);
        }
        return bytes;
    }
}
