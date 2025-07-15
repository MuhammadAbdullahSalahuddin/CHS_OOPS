package Stegno;

import java.io.File;
import java.io.IOException;

public interface MediaStegnography<T> {
    void encode(String inputPath, T message, String outputPath) throws IOException;
    void encodeFile(String inputPath, File file, String outputPath) throws IOException;
    T decode(String inputPath) throws IOException;
    File decodeToFile(String inputPath, String outputFilePath) throws IOException;
}