package Gaufre.Configuration;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class ResourceLoader {
    static public InputStream getResourceAsStream(String relativePath) {
        InputStream resourceStream = null;
        if (Config.estJar()) {
            resourceStream = ResourceLoader.class.getClassLoader().getResourceAsStream(relativePath);
            if (resourceStream == null) {
                System.err.println("Image " + relativePath + " not found.");
                Config.debug("Attempting to use placeholder for image...");
                resourceStream = ResourceLoader.class.getClassLoader().getResourceAsStream("images/notFound.png");
                if (resourceStream == null) {
                    System.err.println("While handling missing resource " + relativePath + " new error occured :");
                    System.err.println("Missing required resource images/notFound.png");
                    System.exit(1);
                }
            }
        } else {
            try {
                resourceStream = new FileInputStream(relativePath);
            } catch (FileNotFoundException e1) {
                System.err.println("Image " + relativePath + " not found.");
                try {
                    resourceStream = new FileInputStream("images/notFound.png");
                } catch (FileNotFoundException e2) {
                    System.err.println("While handling " + e1 + " new error occured :");
                    System.err.println("Missing required file images/notFound.png : " + e2);
                    System.exit(1);

                }
            }
        }
        return resourceStream;
    }

    static public BufferedImage lireImage(String nom) {
        String imgPath = "images/" + nom + ".png";
        InputStream in;
        try {
            in = ResourceLoader.getResourceAsStream(imgPath);
            return ImageIO.read(in);
        } catch (FileNotFoundException e) {
            System.err.println("Erreur: fichier " + imgPath + " introuvable");
        } catch (NullPointerException e) {
            System.err.println("Erreur: ressource " + imgPath + " introuvable");
        } catch (Exception e) {
            System.err.println("ERREUR: impossible de charger l'image " + nom);
        }
        // If loading failed
        return null;
    }
}
