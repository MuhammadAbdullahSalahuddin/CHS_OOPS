package Cryptography;

import java.io.*;

 class CeaserCipher implements Crypto, FileCipher{
    
    public String encrypt(String text, Object key){

        if(!(key instanceof Integer)){ throw new IllegalArgumentException("Cryptography.CeaserCipher requires Integer variable key");}

        int integerkey= (int) key;
        StringBuilder result = new StringBuilder();
        text = text.toLowerCase();

        
        for (int i = 0; i < text.length(); i++) {

            char c = text.charAt(i);
            if (Character.isLetter(c)) {

                char ch = (char) (((c - 'a' + integerkey) % 26) + 'a');
                result.append(ch);
            } else {
                result.append(c);

            }

        }

        return result.toString();

    }


    public String decrypt(String text, Object key){
        if (!(key instanceof Integer)) throw new IllegalArgumentException("Ceaser Cipher requires integer variable key");
        int integerkey= (int) key;
        StringBuilder result = new StringBuilder();
        text = text.toLowerCase();

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            if (Character.isLetter(c)) {
                char ch = (char) (((c - 'a' - integerkey) % 26 + 26) % 26 + 'a');
                result.append(ch);
            } else {
                result.append(c);

            }

        }
        return result.toString();
    }

    
    public void encryptFile(String inputFilePath, String outputFilePath, Object key) throws IOException{
        if(!(key instanceof  Integer)) throw new IllegalArgumentException("Ceaser Cipher requires Integer for Encryption of file");

        int intkey= (int) key;
        try(BufferedReader reader= new BufferedReader(new FileReader(inputFilePath));
            PrintWriter writer = new PrintWriter(outputFilePath)){

                String line;
                while ((line = reader.readLine() ) !=null){
                     String encyrptedLine = encrypt(line, intkey);
                     writer.write(encyrptedLine);
                     writer.println();

                }
            }
     }



     public void decryptFile(String inputFilePath, String outputFilePath, Object key) throws IOException{
        if(!(key instanceof  Integer)) throw new IllegalArgumentException("Ceaser Cipher requires Integer for Decyrption of file");

        int intkey= (int) key;
        try(BufferedReader reader= new BufferedReader(new FileReader(inputFilePath));
            PrintWriter writer = new PrintWriter(outputFilePath)){

                String line;
                while ((line = reader.readLine() ) !=null){
                     String encyrptedLine = decrypt(line, intkey);
                     writer.write(encyrptedLine);
                     writer.println();

                }
            }
     }

}