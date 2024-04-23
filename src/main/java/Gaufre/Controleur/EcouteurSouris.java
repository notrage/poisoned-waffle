package Gaufre.Controleur;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.Graphics2D;

import Gaufre.Vue.InterfaceGraphique;
import Gaufre.Modele.Coup;

public class EcouteurSouris extends MouseAdapter {
    // InterfaceGraphique inter;
    InterfaceGraphique ig;

    public EcouteurSouris(InterfaceGraphique ig) {
        this.ig = ig;
    }

    public void mouseClicked(MouseEvent e) {
        // int x = e.getX() / taille d'une cellule (largeur)
        // int y = e.getY() / taille d'une cellule (hauteur)
        // if (gaufre.jouer(new Coup(x, y)));
        // inter.repaint()
        if (ig.getEtat() == ig.JEU) {
            if (e.getX() < ig.getTaillePlateauX() && e.getY() < ig.getTaillePlateauY()) {
                int c = e.getX() / ig.getTailleCelluleX();
                int l = e.getY() / ig.getTailleCelluleY();
                if (ig.getMG().jouer(l, c))
                    ig.afficherGaufre();
            }

        }
    }

    public void mouseReleased(MouseEvent e) {
        // Repeindre le jeu en enlevant le masque vert ou rouge
        if (ig.getEtat() == ig.JEU) {
            if (e.getX() < ig.getTaillePlateauX() && e.getY() < ig.getTaillePlateauY()) {
                ig.afficherGaufre();
            }
        }
    }
    


    public void mousePressed(MouseEvent e) {
        // Repeindre le jeu en ajoutant un masque vert ou rouge si le coup est valide
        if (ig.getEtat() == ig.JEU) {
            if (e.getX() < ig.getTaillePlateauX() && e.getY() < ig.getTaillePlateauY()) {
                int c = e.getX() / ig.getTailleCelluleX();
                int l = e.getY() / ig.getTailleCelluleY();
                // Dessiner un masque vert ou rouge avec transparence
                Graphics2D g2d = (Graphics2D) ig.getGraphics();
                g2d.setColor(ig.getMG().getGaufre().estJouable(new Coup(l, c))
                        ? new Color(0, 255, 0, 128)
                        : new Color(255, 0, 0, 128));
                g2d.fillRect(c * ig.getTailleCelluleX(), l * ig.getTailleCelluleY(), ig.getTailleCelluleX(),
                        ig.getTailleCelluleY());
            }
        }
    }
    
    public void mouseExited(MouseEvent e) {
        // Repeindre le jeu en enlevant le masque vert ou rouge
        if (ig.getEtat() == ig.JEU) {
            if (e.getX() < ig.getTaillePlateauX() && e.getY() < ig.getTaillePlateauY()) {
                ig.afficherGaufre();
            }
        }
    }
}
