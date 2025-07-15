package Hashing;

public class HashProcessor {

    public static String compute(String algorithm, String input) {
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException("Input cannot be null or empty");
        }

        HashAlgorithm hasher;
        switch (algorithm) { // Remove toUpperCase()
            case "Hashing.MD5":    hasher = new MD5();    break;
            case "Hashing.SHA1":   hasher = new SHA1();   break;
            case "Hashing.SHA256": hasher = new SHA256(); break;
            default:
                throw new IllegalArgumentException("Unsupported algorithm: " + algorithm);
        }

        return hasher.computeHash(input);
    }
}