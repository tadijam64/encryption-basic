package utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class MyFileUtils {

    public static String openTextFile(String path) throws IOException {
        InputStream inputStream = FileUtils.openInputStream(new File(path));
        String text;
        try {
            text = IOUtils.toString(inputStream, getCharset());
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
        return text;
    }

    public  static void saveTextFile(String path, String text) throws IOException {
        FileUtils.writeStringToFile(new File(path), text, getCharset());
    }

    private static Charset getCharset() {
        Charset charset = Charset.defaultCharset();

        if (Charset.isSupported("UTF-8")) {
            charset = Charset.forName("UTF-8");
        }

        return charset;
    }
}
