package utils;

import javax.crypto.Cipher;
import java.security.SecureRandom;

public class Constants {

    public static byte[] IV = "b94d27b9934d3e08".getBytes();
    public static final String TAJNI_KLJUC_PATH = "tajni_kljuc.txt";
    public static final String PRIVATNI_KLJUC_PATH = "privatni_kljuc.txt";
    public static final String JAVNI_KLJUC_PATH = "javni_kljuc.txt";
    public static final String ORIGINALNI_TEKST_PATH = "originalni_tekst.txt";
    public static final String KRIPTIRANI_TEKST_PATH = "kriptirani_tekst.txt";

}
