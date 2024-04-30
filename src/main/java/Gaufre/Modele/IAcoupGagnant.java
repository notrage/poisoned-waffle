package Gaufre.Modele;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

// IA jouant aléatoirement SAUF si il existe un coup gagnat 
public class IAcoupGagnant implements IA {
    Gaufre gaufre;
    Random generateur;

    public IAcoupGagnant() {
        generateur = new Random();

    }

    public void init(Gaufre g) {
        gaufre = g;
    }

    public Coup coupSuivant() {
        Coup c;
        if ((c = trouveGagnant()) != null) {
            return c;
        } else {
            ArrayList<Point> ensPossible = new ArrayList<>();
            for (int i = 0; i < gaufre.getNbLignes(); i++) {
                for (int j = 0; j < gaufre.getNbColonnes(); j++) {
                    if (i == 0 && j == 0)
                        continue;
                    Point p = new Point(i, j);
                    if (gaufre.estJouable(new Coup(p))) {
                        ensPossible.add(p);
                    }
                }
            }
            if (ensPossible.size() == 0) {
                return new Coup(0, 0);
            }
            int x = Math.abs(generateur.nextInt() % ensPossible.size());
            c = new Coup(ensPossible.get(x));
            return c;
        }
    }

    // Méthode qui retourne un coup gagant, ou null si il n'en existe pas
    public Coup trouveGagnant() {
        Coup c;
        Gaufre copie = gaufre.clone();
        for (int i = 0; i < copie.getNbLignes(); i++) {
            for (int j = 0; j < copie.getNbColonnes(); j++) {
                c = new Coup(i, j);
                if (copie.jouer(c)) {
                    // Vérification si l'IA gagne avec ce coup
                    if (copie.estFinie() == copie.getJoueur2()) {
                        return c;
                    } else {
                        // Si le coup n'est pas gagnat, on l'annule et on essaye un autre
                        copie.dejouer();
                    }
                }
            }
        }
        return null;
    }
}
