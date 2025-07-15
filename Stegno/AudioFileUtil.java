package Stegno;

import java.io.*;

class AudioFileUtil {
    static byte[] readWav(String filePath) throws IOException {
        if (filePath == null) {
            throw new IllegalArgumentException("File path must not be null.");
        }
        File file = new File(filePath);
        byte[] data = new byte[(int) file.length()];
        try (BufferedInputStream bufInput = new BufferedInputStream(new FileInputStream(file))) {
            bufInput.read(data);
        }
        return data;
    }

    static void writeWav(byte[] data, String filePath) throws IOException {
        if (data == null || filePath == null) {
            throw new IllegalArgumentException("Data and file path must not be null.");
        }
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath))) {
            bos.write(data);
        }
    }
}