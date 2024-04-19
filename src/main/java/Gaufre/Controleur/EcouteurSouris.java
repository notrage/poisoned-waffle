package Gaufre.Controleur;

import java.awt.event.MouseAdapter;

import org.w3c.dom.events.MouseEvent;

import Gaufre.Modele.Gaufre;

public class EcouteurSouris extends MouseAdapter {
    // InterfaceGraphique inter;
    Gaufre gaufre;

    public EcouteurSouris(Gaufre g) {
        gaufre = g;
    }

    public void mousePressed(MouseEvent e) {
        // int x = e.getX() / taille d'une cellule (largeur)
        // int y = e.getY() / taille d'une cellule (hauteur)
        // if (gaufre.jouer(new Coup(x, y)));
        // inter.repaint()
    }

    public void mouseEntered(MouseEvent e) {
        // Repeindre le jeu en ajoutant un masque vert ou rouge si le coup est valide
    }
}
