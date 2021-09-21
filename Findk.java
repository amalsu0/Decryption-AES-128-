import javax.crypto.*;
import java.security.*;
import java.nio.file.*;
import java.nio.ByteBuffer;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

public class findk {      
  
        // To calculate the time 
    public static long elapsedTime ;
    public static long  start = System.currentTimeMillis();
    
    public static void main(String[] args) throws Exception {
         
        // The known part of plaintext
        byte[] Salam = ("Salam").getBytes(StandardCharsets.UTF_8); 
        
        // Open and read the files as arrays of bytes
        Path path = Paths.get("D:\\partial-key.dat");
        byte[] partialkey = Files.readAllBytes(path);
  
        Path path2 = Paths.get("D:\\ciphertext2.dat");
        byte[] ciphertext = Files.readAllBytes(path2);
      
        byte[] guessed_key = new byte[16];
       
        // Copy the first known 12 bytes from the partial key
        int i=0;
        while(i<12){
            guessed_key[i]=partialkey[i];
            i++;
        }
        
        // Guess the missing last 4 bytes of key 
        for (int a = 0; a < 256; a++) {
            guessed_key[12] = (byte) a;
            for (int b = 0; b < 256; b++) {
                  guessed_key[13] = (byte) b;
                for (int c = 0; c < 256; c++) {
                      guessed_key[14] = (byte) c;
                    for (int d = 0; d < 256; d++) {
                        guessed_key[15] = (byte) d;
                        // Trying to decrypt the ciphertext using the gussed key by AES128 
                        if (decryptAes128(guessed_key,ciphertext, Salam))
                                break;
                        }
                    }
                }
            }
        }
    

    private static boolean decryptAes128(byte[] guessed_key, byte[] ciphertext , byte[] Salam ) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
         // Create the AES key format 
        SecretKeySpec AESkey = new SecretKeySpec(guessed_key, "AES");
        // Use Electronic Code Book mode 
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING"); 
        cipher.init(Cipher.DECRYPT_MODE, AESkey);
        
        byte[] decryptedtext = new byte[16];
        
        try {
            decryptedtext = cipher.doFinal(ciphertext);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            return false;
        }
     
        // If the decryptedtext does not starts with the word Salam, then the guessed key is NOT the right key! 
        // Otherwise the guessed key is the right key! 
        boolean found = ByteBuffer.wrap(decryptedtext, 0, 4).equals(ByteBuffer.wrap(Salam, 0, 4));
        if (!found ) {
            return false;
        }
       
        String Key = bytesToHex(guessed_key) ;
        System.out.println("The missing 4 bytes of the key in hexadecimal: " + Key.substring(24,32));
        System.out.println("Decrypted message: " + new String(decryptedtext));
        
        long end = System.currentTimeMillis();
        elapsedTime = end - start;
        System.out.println( "The time it took to find the correct key: " + elapsedTime + " milliseconds");
        System.exit(0);
        return true;
    }

       // Method to convert bytes to hexadecimal format
    private static String bytesToHex(byte[] bytes) {
        StringBuffer result = new StringBuffer();
        for (byte b : bytes) result.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        return result.toString();
    }
}
