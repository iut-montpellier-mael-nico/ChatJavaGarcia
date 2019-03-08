

import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class GenerateurKey {
    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(1024);
        return keyGen.genKeyPair();
    }

    public static  Key generateKeyDES() throws NoSuchAlgorithmException {
        KeyGenerator secretKey = KeyGenerator.getInstance("DES");
        return secretKey.generateKey();
    }
}
