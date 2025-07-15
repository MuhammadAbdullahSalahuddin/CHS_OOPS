package Cryptography;

import java.io.*;

 interface FileCipher extends Crypto{
    
   public void encryptFile(String inputFilePath, String outputFilePath, Object key) throws IOException;
   public void decryptFile(String inputFilePath, String outputFilePath, Object key) throws IOException;
}