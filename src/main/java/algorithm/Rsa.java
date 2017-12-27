package algorithm;

import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class Rsa {
    public static String encrypt(String data, String keyHex, Boolean javni) throws Exception{

        byte[] keyBytes = Hex.decodeHex(keyHex.toCharArray());
        Key key;

        if(javni==true)
            key = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(keyBytes));
        else
            key = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(keyBytes));


        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] encVal = cipher.doFinal(data.getBytes("UTF-8"));

        return Base64.getEncoder().encodeToString(encVal);
    }

    public static String decrypt(String data, String keyHex, Boolean javni) throws Exception {

        byte[] keyBytes = Hex.decodeHex(keyHex.toCharArray());
        Key key;
        if(javni == false)
            key = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(keyBytes));
        else
            key = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(keyBytes));

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, key);

        byte[] decValue = cipher.doFinal(Base64.getDecoder().decode(data));

        return new String(decValue);
    }
}
