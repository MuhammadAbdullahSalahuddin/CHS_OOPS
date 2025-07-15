package Cryptography;

import java.io.*;
import java.util.Base64;

public class XORCipher implements Crypto, FileCipher {
    @Override
    public String encrypt(String text, Object key) {
        if (!(key instanceof String)) throw new IllegalArgumentException("XOR cipher requires String key");
        String stringKey = (String) key;
        if (stringKey.isEmpty()) throw new IllegalArgumentException("Key cannot be empty");

        byte[] textBytes = text.getBytes();
        byte[] keyBytes = stringKey.getBytes();
        byte[] result = new byte[textBytes.length];

        for (int i = 0; i < textBytes.length; i++) {
            result[i] = (byte) (textBytes[i] ^ keyBytes[i % keyBytes.length]);
        }

        // Encode to Base64 to ensure the result is a valid string
        return Base64.getEncoder().encodeToString(result);
    }

    @Override
    public String decrypt(String text, Object key) {
        if (!(key instanceof String)) throw new IllegalArgumentException("XOR cipher requires String key");
        String stringKey = (String) key;
        if (stringKey.isEmpty()) throw new IllegalArgumentException("Key cannot be empty");

        // Decode from Base64
        byte[] textBytes;
        try {
            textBytes = Base64.getDecoder().decode(text);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Base64 encoded input", e);
        }

        byte[] keyBytes = stringKey.getBytes();
        byte[] result = new byte[textBytes.length];

        for (int i = 0; i < textBytes.length; i++) {
            result[i] = (byte) (textBytes[i] ^ keyBytes[i % keyBytes.length]);
        }

        // Convert back to string
        return new String(result);
    }

    @Override
    public void encryptFile(String inputFilePath, String outputFilePath, Object key) throws IOException {
        if (!(key instanceof String)) throw new IllegalArgumentException("XOR Cipher requires String key");
        String stringKey = (String) key;
        if (stringKey.isEmpty()) throw new IllegalArgumentException("Key cannot be empty");

        byte[] keyBytes = stringKey.getBytes();
        try (InputStream in = new BufferedInputStream(new FileInputStream(inputFilePath));
             OutputStream out = new BufferedOutputStream(new FileOutputStream(outputFilePath))) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            int keyIndex = 0;
            while ((bytesRead = in.read(buffer)) != -1) {
                for (int i = 0; i < bytesRead; i++) {
                    buffer[i] = (byte) (buffer[i] ^ keyBytes[keyIndex % keyBytes.length]);
                    keyIndex++;
                }
                out.write(buffer, 0, bytesRead);
            }
        }
    }

    @Override
    public void decryptFile(String inputFilePath, String outputFilePath, Object key) throws IOException {
        // XOR is symmetric, so decryption is the same as encryption
        encryptFile(inputFilePath, outputFilePath, key);
    }
}