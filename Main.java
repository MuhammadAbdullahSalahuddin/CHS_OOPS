import Cryptography.CryptoProcessor;
import Hashing.HashProcessor;
import Stegno.StegnographyProcessor;
import java.io.*;

public class Main {
    private final CryptoProcessor cryptoProcessor;
    private final StegnographyProcessor stegnoProcessor;
    private final HashProcessor hashProcessor;
    private final MainForm mainForm;

    public Main() {
        this.cryptoProcessor = new CryptoProcessor();
        this.stegnoProcessor = new StegnographyProcessor();
        this.hashProcessor = new HashProcessor();
        this.mainForm = new MainForm(this);
    }

    // Cryptography operations
    public String encryptTextCeaser(String text, int key) {
        return cryptoProcessor.ceaserTextEnc(text, key);
    }

    public String decryptTextCeaser(String text, int key) {
        return cryptoProcessor.ceaserTextDec(text, key);
    }

    public String encryptFileCeaser(String inputPath, String outputPath, int key) {
        return cryptoProcessor.ceaserFileEnc(inputPath, outputPath, key);
    }

    public String decryptFileCeaser(String inputPath, String outputPath, int key) {
        return cryptoProcessor.ceaserFileDec(inputPath, outputPath, key);
    }

    public String encryptTextXOR(String text, String key) {
        return cryptoProcessor.xorTextEnc(text, key);
    }

    public String decryptTextXOR(String text, String key) {
        return cryptoProcessor.xorTextDec(text, key);
    }

    public String encryptFileXOR(String inputPath, String outputPath, String key) {
        return cryptoProcessor.xorFileEnc(inputPath, outputPath, key);
    }

    public String decryptFileXOR(String inputPath, String outputPath, String key) {
        return cryptoProcessor.xorFileDec(inputPath, outputPath, key);
    }

    // Hashing operations
    public String computeHash(String algorithm, String input) {
        try {
            return hashProcessor.compute(algorithm, input);
        } catch (IllegalArgumentException e) {
            return "Hashing error: " + e.getMessage();
        }
    }

    // Steganography operations
    public String encodeStegno(String mediaType, String inputPath, Object payload, String outputPath) {
        try {
            stegnoProcessor.encode(mediaType, inputPath, payload, outputPath);
            return "Steganography encoding successful.";
        } catch (IOException | IllegalArgumentException e) {
            return "Steganography encoding error: " + e.getMessage();
        }
    }

    public Object decodeStegno(String mediaType, String inputPath, String outputFilePath) {
        try {
            return stegnoProcessor.decode(mediaType, inputPath, outputFilePath);
        } catch (IOException | IllegalArgumentException e) {
            return "Steganography decoding error: " + e.getMessage();
        }
    }

    // Start the application
    public void start() {
        mainForm.display(); // Show the GUI
    }

    // Main method
    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }
}