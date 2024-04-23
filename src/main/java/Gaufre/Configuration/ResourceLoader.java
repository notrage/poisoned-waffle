package Gaufre.Configuration;

import java.io.IOException;
import java.io.InputStream;

public class ResourceLoader {

    public static InputStream getResourceAsStream(String relativePath) throws IOException {
        InputStream resourceStream = ResourceLoader.class.getClassLoader().getResourceAsStream(relativePath);
        if (resourceStream == null) {
            throw new IOException("Resource not found: " + relativePath);
        }
        return resourceStream;
    }
}
