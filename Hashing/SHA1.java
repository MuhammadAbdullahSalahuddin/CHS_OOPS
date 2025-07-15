package Hashing;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class SHA1 implements HashAlgorithm {

    @Override
    public String computeHash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] hashBytes = md.digest(input.getBytes());
            return bytesToHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            return "Error during SHA-1 hash computation: " + e.getMessage();
        }
    }

    @Override
    public String getAlgorithmName() {
        return "SHA-1";
    }

    private String bytesToHex(byte[] hash) {
        StringBuilder hex = new StringBuilder();
        for (byte b : hash) {
            hex.append(String.format("%02X", b));
        }
        return hex.toString();
    }
}