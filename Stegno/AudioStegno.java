package Stegno;

import java.io.*;
import java.util.Arrays;

public class AudioStegno implements MediaStegnography<Message> {
    private static final int HEADER_SIZE = 44; // WAV header size

    @Override
    public void encode(String inputPath, Message message, String outputPath) throws IOException {
        validateInputs(inputPath, message, outputPath);

        byte[] audioBytes = AudioFileUtil.readWav(inputPath);

        byte[] serializedMessage = serializeMessage(message);
        validateCapacity(audioBytes, serializedMessage);
        embedMessage(audioBytes, serializedMessage);
        AudioFileUtil.writeWav(audioBytes, outputPath);
    }

    @Override
    public void encodeFile(String inputPath, File file, String outputPath) throws IOException {
        validateInputs(inputPath, file, outputPath);
        byte[] audioBytes = AudioFileUtil.readWav(inputPath);
        byte[] fileBytes = readFile(file);
        validateCapacity(audioBytes, fileBytes);
        embedMessage(audioBytes, fileBytes);
        AudioFileUtil.writeWav(audioBytes, outputPath);
    }

    @Override
    public Message decode(String inputPath) throws IOException {
        if (inputPath == null) {
            throw new IOException("Input path must not be null.");
        }
        if (!inputPath.toLowerCase().endsWith(".wav")) {
            throw new IOException("Input file must have .wav extension.");
        }
        byte[] audioBytes = AudioFileUtil.readWav(inputPath);
        byte[] messageBytes = extractPayload(audioBytes);
        return deserializeMessage(messageBytes);
    }

    @Override
    public File decodeToFile(String inputPath, String outputFilePath) throws IOException {
        if (inputPath == null || outputFilePath == null) {
            throw new IOException("Input path and output file path must not be null.");
        }
        byte[] audioBytes = AudioFileUtil.readWav(inputPath);
        byte[] fileBytes = extractPayload(audioBytes);
        return writeFile(outputFilePath, fileBytes);
    }

    // Helper methods
    private void validateInputs(String inputPath, Object payload, String outputPath) throws IOException {
        if (inputPath == null || outputPath == null || payload == null) {
            throw new IOException("Input path, output path, and payload must not be null.");
        }
        if (!inputPath.toLowerCase().endsWith(".wav") || !outputPath.toLowerCase().endsWith(".wav")) {
            throw new IOException("Input and output files must have .wav extension.");
        }
        if (payload instanceof File && !((File) payload).exists()) {
            throw new IOException("File to embed does not exist: " + ((File) payload).getPath());
        }
    }

    private byte[] readFile(File file) throws IOException {
        try (InputStream is = new FileInputStream(file)) {
            return is.readAllBytes();
        }
    }

    private byte[] serializeMessage(Message msg) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(msg);
            return baos.toByteArray();
        }
    }

    private Message deserializeMessage(byte[] data) throws IOException {
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data))) {
            return (Message) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException("Failed to deserialize message: " + e.getMessage(), e);
        }
    }

    private void validateCapacity(byte[] audioBytes, byte[] payload) throws IOException {
        int requiredBits = payload.length * 8;
        int availableBits = audioBytes.length - HEADER_SIZE;
        if (requiredBits > availableBits) {
            throw new IOException("Audio file too small. Required: " + requiredBits +
                    " bits, Available: " + availableBits + " bits.");
        }
    }

    private void embedMessage(byte[] audioBytes, byte[] payload) {
        for (int i = 0; i < payload.length * 8; i++) {
            int bit = (payload[i / 8] >> (7 - (i % 8))) & 1;
            int index = HEADER_SIZE + i;
            audioBytes[index] = (byte) ((audioBytes[index] & 0xFE) | bit);
        }
    }

    private byte[] extractPayload(byte[] audioBytes) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int currentByte = 0;
        int bitCount = 0;

        for (int i = HEADER_SIZE; i < audioBytes.length; i++) {
            int lsb = audioBytes[i] & 1;
            currentByte = (currentByte << 1) | lsb;
            bitCount++;

            if (bitCount == 8) {
                baos.write(currentByte);
                currentByte = 0;
                bitCount = 0;
            }
        }
        return baos.toByteArray();
    }

    private File writeFile(String outputFilePath, byte[] data) throws IOException {
        File outputFile = new File(outputFilePath);
        try (OutputStream os = new FileOutputStream(outputFile)) {
            os.write(data);
        }
        return outputFile;
    }
}