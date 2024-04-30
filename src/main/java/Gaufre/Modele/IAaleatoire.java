package Gaufre.Modele;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

public class IAaleatoire implements IA {
    Gaufre gaufre;
    Random generateur;

    public IAaleatoire() {
        generateur = new Random();
    }

    public void init(Gaufre g) {
        gaufre = g;
    }

    public Coup coupSuivant() {
        ArrayList<Point> ensPossible = new ArrayList<>();
        for (int i = 0; i < gaufre.getNbLignes(); i++) {
            for (int j = 0; j < gaufre.getNbColonnes(); j++) {
                if (i == 0 && j == 0) continue;
                Point p = new Point(i,j);
                if (gaufre.estJouable(new Coup(p))){
                    ensPossible.add(p);
                }
            }
        }
        if (ensPossible.size() == 0){
            return new Coup(0, 0);
        }
        int x = Math.abs(generateur.nextInt() % ensPossible.size());
        Coup c = new Coup(ensPossible.get(x));
        return c;
    }
}
