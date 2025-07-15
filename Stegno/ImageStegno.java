package Stegno;

import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

public class ImageStegno implements MediaStegnography<Message> {
    @Override
    public void encode(String originalImagePath, Message message, String outputImagePath) throws IOException {
        validateInputs(originalImagePath, message, outputImagePath);
        BufferedImage image = loadImage(originalImagePath);
        byte[] messageBytes = serializeMessage(message);
        validateCapacity(image, messageBytes);
        embedMessage(image, messageBytes);
        saveImage(image, outputImagePath);
    }

    @Override
    public void encodeFile(String originalImagePath, File file, String outputImagePath) throws IOException {
        validateInputs(originalImagePath, file, outputImagePath);
        BufferedImage image = loadImage(originalImagePath);
        byte[] fileBytes = readFile(file);
        validateCapacity(image, fileBytes);
        embedMessage(image, fileBytes);
        saveImage(image, outputImagePath);
    }

    @Override
    public Message decode(String imagePath) throws IOException {
        if (imagePath == null) {
            throw new IOException("Image path must not be null.");
        }
        BufferedImage image = loadImage(imagePath);
        byte[] messageBytes = extractPayload(image);
        return deserializeMessage(messageBytes);
    }

    @Override
    public File decodeToFile(String imagePath, String outputFilePath) throws IOException {
        if (imagePath == null || outputFilePath == null) {
            throw new IOException("Image path and output file path must not be null.");
        }
        BufferedImage image = loadImage(imagePath);
        byte[] fileBytes = extractPayload(image);
        return writeFile(outputFilePath, fileBytes);
    }

    // Helper methods
    private void validateInputs(String originalImagePath, Object payload, String outputImagePath) throws IOException {
        if (originalImagePath == null || payload == null || outputImagePath == null) {
            throw new IOException("All parameters must be non-null.");
        }
        if (payload instanceof File && !((File) payload).exists()) {
            throw new IOException("File to embed does not exist: " + ((File) payload).getPath());
        }
    }

    private BufferedImage loadImage(String path) throws IOException {
        try {
            BufferedImage image = ImageIO.read(new File(path));
            if (image == null) {
                throw new IOException("Could not read image from path: " + path);
            }
            return image;
        } catch (IOException e) {
            throw new IOException("Image loading failed: " + e.getMessage(), e);
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

    private void validateCapacity(BufferedImage image, byte[] payload) throws IOException {
        int requiredBits = payload.length * 8;
        int imageCapacity = image.getWidth() * image.getHeight();
        if (requiredBits > imageCapacity) {
            throw new IOException("Image size is insufficient. Required: " + requiredBits +
                    " bits, Available: " + imageCapacity + " bits.");
        }
    }

    private void embedMessage(BufferedImage image, byte[] payload) {
        int payloadIndex = 0;
        outer:
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                if (payloadIndex >= payload.length * 8) break outer;
                int rgb = image.getRGB(x, y);
                int blue = rgb & 0xFF;
                int bit = (payload[payloadIndex / 8] >> (7 - (payloadIndex % 8))) & 1;
                blue = (blue & 0xFE) | bit;
                int updatedRGB = (rgb & 0xFFFFFF00) | blue;
                image.setRGB(x, y, updatedRGB);
                payloadIndex++;
            }
        }
    }

    private void saveImage(BufferedImage image, String outputImagePath) throws IOException {
        try {
            File outputFile = new File(outputImagePath);
            boolean success = ImageIO.write(image, "png", outputFile);
            if (!success) {
                throw new IOException("Failed to save encoded image to: " + outputImagePath);
            }
        } catch (IOException e) {
            throw new IOException("Image saving failed: " + e.getMessage(), e);
        }
    }

    private byte[] extractPayload(BufferedImage image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int currentByte = 0;
        int bitCount = 0;

        outer:
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int rgb = image.getRGB(x, y);
                int blue = rgb & 0xFF;
                int bit = blue & 1;
                currentByte = (currentByte << 1) | bit;
                bitCount++;

                if (bitCount == 8) {
                    baos.write(currentByte);
                    currentByte = 0;
                    bitCount = 0;
                }
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