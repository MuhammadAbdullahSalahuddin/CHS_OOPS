package Cryptography;

interface Crypto{

    public String encrypt(String text, Object key);
    public String decrypt(String text, Object key);

    
}