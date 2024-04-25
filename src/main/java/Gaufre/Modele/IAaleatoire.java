package Gaufre.Modele;

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
        int x = generateur.nextInt() % gaufre.getNbLignes();
        int y = generateur.nextInt() % gaufre.getNbColonnes();
        Coup c = new Coup(x, y);
        while (!gaufre.estJouable(c)) {
            x = generateur.nextInt() % gaufre.getNbLignes();
            y = generateur.nextInt() % gaufre.getNbColonnes();
            c = new Coup(x, y);
        }
        return c;
    }
}
