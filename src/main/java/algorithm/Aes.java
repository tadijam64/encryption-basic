package algorithm;

import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

import static utils.Constants.IV;

public class Aes {

    public static String encrypt(String data, String secretKey, boolean b) throws Exception {

        byte[] secretKeyBytes = Hex.decodeHex(secretKey.toCharArray());
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKeyBytes, "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, new IvParameterSpec(IV));

        byte[] encVal = cipher.doFinal(data.getBytes("UTF-8"));

        return Base64.getEncoder().encodeToString(encVal);
    }

    public static String decrypt(String data, String secretKey, boolean b) throws Exception {

        byte[] secretKeyBytes = Hex.decodeHex(secretKey.toCharArray());
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKeyBytes, "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(IV));

        byte[] decValue = cipher.doFinal(Base64.getDecoder().decode(data));

        return new String(decValue);
    }
}
