import javax.crypto.*;
import java.security.*;
import java.nio.file.*;
import java.nio.ByteBuffer;
import javax.crypto.spec.SecretKeySpec;



public class decrypt {
 

    public static void main(String[] args) throws Exception {

        // Open the file and read the key 
        Path path = Paths.get("D:\\key.dat");
        byte[] key = Files.readAllBytes(path);

        // Open the file and read the ciphertext
        Path path2 = Paths.get("D:\\ciphertext.dat");
        byte[] ciphertext = Files.readAllBytes(path2);
        
        byte[] decryptedtext = new byte[16];  
        
        // Decrypt the ciphertext using ECB mode
        SecretKeySpec keySpecDecode = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, keySpecDecode);
        
        decryptedtext = cipher.doFinal(ciphertext);
        System.out.println("Decrypted message: \n" + new String(decryptedtext));

    }
}
    
