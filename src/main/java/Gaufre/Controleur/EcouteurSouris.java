package Gaufre.Controleur;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.Graphics2D;

import Gaufre.Vue.InterfaceGraphique;
import Gaufre.Configuration.Config;
import Gaufre.Modele.Coup;

public class EcouteurSouris extends MouseAdapter {
    // InterfaceGraphique inter;
    InterfaceGraphique ig;

    public EcouteurSouris(InterfaceGraphique ig) {
        this.ig = ig;
    }

    public void mouseClicked(MouseEvent e) {
        if (ig.getPlateau().contains(e.getX(), e.getY()) && !ig.getMG().estFini()) {
            int c = e.getX() / ig.getTailleCelluleX();
            int l = e.getY() / ig.getTailleCelluleY();
            Config.debug("Click sur case ", l, c);
            if (ig.getMG().getGaufre().jouer(new Coup(l, c))) {
                ig.mangeCellGaufre(l, c);
                Config.debug("Coup valide");
                if (ig.getMG().estFini()) {
                    ig.finPartie();
                }
            }
        }
    }

    public void mousePressed(MouseEvent e) {
        return;
    }

    public void mouseReleased(MouseEvent e) {
        return;
    }

    public void mouseExited(MouseEvent e) {
        // Repeindre le jeu en enlevant le masque vert ou rouge
        Graphics2D g2d = (Graphics2D) ig.getGraphics();
        g2d.setColor(new Color(0, 0, 0, 0));
        g2d.fillRect(0, 0, ig.getPlateau().getWidth(), ig.getPlateau().getHeight());
    }
}
