package Gaufre.Configuration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ResourceLoader {
    static public InputStream getResourceAsStream(String relativePath)
            throws NullPointerException, FileNotFoundException {
        InputStream resourceStream;
        if (Config.estJar()) {
            resourceStream = ResourceLoader.class.getClassLoader().getResourceAsStream(relativePath);
            if (resourceStream == null) {
                throw new NullPointerException("Resource not found: " + relativePath);
            }
        } else {
            resourceStream = new FileInputStream(relativePath);
            if (resourceStream == null) {
                throw new FileNotFoundException("File not found: " + relativePath);
            }
        }
        return resourceStream;
    }
}
