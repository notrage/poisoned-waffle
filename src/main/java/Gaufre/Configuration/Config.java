package Gaufre.Configuration;

public class Config {
    static boolean estJar;

    public Config() {
        estJar = System.getProperty("java.class.path").endsWith("jar");
    }

    static public boolean estJar() {
        return estJar;
    }
}
