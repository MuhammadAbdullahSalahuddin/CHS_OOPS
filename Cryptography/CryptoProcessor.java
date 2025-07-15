package Cryptography;

import java.io.*;

public class CryptoProcessor {
    private final Crypto ceaserCipher = new CeaserCipher();
    private final Crypto xorCipher = new XORCipher();
    private final FileCipher ceaserFileCipher = (FileCipher) ceaserCipher;
    private final FileCipher xorFileCipher = (FileCipher) xorCipher;

    public String ceaserTextEnc(String text, int key) {
        try {
            String encrypted = ceaserCipher.encrypt(text, key);
            return "Encrypted Message: " + encrypted;
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    public String ceaserTextDec(String text, int key) {
        try {
            String decrypted = ceaserCipher.decrypt(text, key);
            return "Decrypted Message: " + decrypted;
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    public String ceaserFileEnc(String inputPath, String outputPath, int key) {
        if (!fileExists(inputPath)) return "Input file does not exist.";
        if (!isWritable(outputPath)) return "Output file path is not writable.";
        try {
            ceaserFileCipher.encryptFile(inputPath, outputPath, key);
            return "File encrypted successfully.";
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    public String ceaserFileDec(String inputPath, String outputPath, int key) {
        if (!fileExists(inputPath)) return "Input file does not exist.";
        if (!isWritable(outputPath)) return "Output file path is not writable.";
        try {
            ceaserFileCipher.decryptFile(inputPath, outputPath, key);
            return "File decrypted successfully.";
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    public String xorTextEnc(String text, String key) {
        try {
            String encrypted = xorCipher.encrypt(text, key);
            return "Encrypted Message: " + encrypted;
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    public String xorTextDec(String text, String key) {
        try {
            String decrypted = xorCipher.decrypt(text, key);
            return "Decrypted Message: " + decrypted;
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    public String xorFileEnc(String inputPath, String outputPath, String key) {
        if (!fileExists(inputPath)) return "Input file does not exist.";
        if (!isWritable(outputPath)) return "Output file path is not writable.";
        try {
            xorFileCipher.encryptFile(inputPath, outputPath, key);
            return "File encrypted successfully.";
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    public String xorFileDec(String inputPath, String outputPath, String key) {
        if (!fileExists(inputPath)) return "Input file does not exist.";
        if (!isWritable(outputPath)) return "Output file path is not writable.";
        try {
            xorFileCipher.decryptFile(inputPath, outputPath, key);
            return "File decrypted successfully.";
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    private boolean fileExists(String path) {
        return new File(path).exists();
    }

    private boolean isWritable(String path) {
        File file = new File(path);
        try {
            if (!file.exists()) {
                return file.createNewFile() && file.delete();
            }
            return file.canWrite();
        } catch (Exception e) {
            return false;
        }
    }
}