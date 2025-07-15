package Stegno;

import java.io.File;
import java.io.IOException;

public final class StegnographyProcessor {
    private final ImageStegno imageStegno;
    private final AudioStegno audioStegno;

    public StegnographyProcessor() {
        this.imageStegno = new ImageStegno();
        this.audioStegno = new AudioStegno();
    }

    // Main encode method called by GUI
    public void encode(String mediaType, String inputPath, Object payload, String outputPath)
            throws IOException, IllegalArgumentException {

        switch (mediaType.toLowerCase()) {
            case "image":
                handleImageEncode(inputPath, payload, outputPath);
                break;
            case "audio":
                handleAudioEncode(inputPath, payload, outputPath);
                break;
            default:
                throw new IllegalArgumentException("Unsupported media type: " + mediaType);
        }
    }

    // Main decode method called by GUI
    public Object decode(String mediaType, String inputPath, String outputFilePath)
            throws IOException, IllegalArgumentException {

        switch (mediaType.toLowerCase()) {
            case "image":
                return outputFilePath != null ?
                        imageStegno.decodeToFile(inputPath, outputFilePath) :
                        imageStegno.decode(inputPath);
            case "audio":
                return outputFilePath != null ?
                        audioStegno.decodeToFile(inputPath, outputFilePath) :
                        audioStegno.decode(inputPath);
            default:
                throw new IllegalArgumentException("Unsupported media type: " + mediaType);
        }
    }

    // ==================== PRIVATE HELPERS ====================
    private void handleImageEncode(String inputPath, Object payload, String outputPath)
            throws IOException {
        if (payload instanceof String) {
            imageStegno.encode(inputPath, createMessage((String) payload, "User"), outputPath);
        } else if (payload instanceof File) {
            imageStegno.encodeFile(inputPath, (File) payload, outputPath);
        } else {
            throw new IllegalArgumentException("Image steganography only supports String or File payload");
        }
    }

    private void handleAudioEncode(String inputPath, Object payload, String outputPath)
            throws IOException {
        if (payload instanceof String) {
            audioStegno.encode(inputPath, createMessage((String) payload, "User"), outputPath);
        } else if (payload instanceof File) {
            audioStegno.encodeFile(inputPath, (File) payload, outputPath);
        } else {
            throw new IllegalArgumentException("Audio steganography only supports String or File payload");
        }
    }

    public Message createMessage(String text, String author) {
        return new Message(text, author);
    }
}