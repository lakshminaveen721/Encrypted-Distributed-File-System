import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.Files;
import java.util.Base64;

public class FileHandler {

    private static final String ALGORITHM = "AES";
    private static final byte[] keyValue = "A1B2C3D4E5F6G7H8".getBytes(); // 16-byte key

    public static SecretKeySpec getSecretKey() {
        return new SecretKeySpec(keyValue, ALGORITHM);
    }

    public static byte[] encrypt(byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey());
        return cipher.doFinal(data);
    }

    public static byte[] decrypt(byte[] encrypted) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey());
        return cipher.doFinal(encrypted);
    }

    public static void saveEncryptedFile(String filename, byte[] content) throws Exception {
        byte[] encrypted = encrypt(content);
        try (FileOutputStream fos = new FileOutputStream(filename)) {
            fos.write(encrypted);
        }
    }

    public static byte[] readDecryptedFile(String filename) throws Exception {
        byte[] encrypted = Files.readAllBytes(new File(filename).toPath());
        return decrypt(encrypted);
    }

    public static void updateFile(String filename, byte[] newContent) throws Exception {
        saveEncryptedFile(filename, newContent);
    }

    public static void deleteFile(String filename) {
        File file = new File(filename);
        if (file.exists()) file.delete();
    }

    public static boolean fileExists(String filename) {
        return new File(filename).exists();
    }
}