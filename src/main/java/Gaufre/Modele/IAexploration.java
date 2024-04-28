package Gaufre.Modele;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class IAexploration implements IA {
    HashMap<Gaufre, Coup> coups;
    HashMap<Gaufre, Boolean> vainqueur;
    Gaufre gaufreInitiale;
    Random generateur;

    public IAexploration() {
        vainqueur = new HashMap<>();
        coups = new HashMap<>();
        generateur = new Random();
    }

    public void init(Gaufre g) {
        gaufreInitiale = g;
        exploration(g.clone());
    }

    public Coup coupSuivant() {
        if (coups.containsKey(gaufreInitiale)) {
            return coups.get(gaufreInitiale);
        } else {
            ArrayList<Point> ensPossible = new ArrayList<>();
            for (int i = 0; i < gaufreInitiale.getNbLignes(); i++) {
                for (int j = 0; j < gaufreInitiale.getNbColonnes(); j++) {
                    if (i == 0 && j == 0)
                        continue;
                    Point p = new Point(i, j);
                    if (gaufreInitiale.estJouable(new Coup(p))) {
                        ensPossible.add(p);
                    }
                }
            }
            if (ensPossible.size() == 0) {
                return new Coup(0, 0);
            }
            int x = Math.abs(generateur.nextInt() % ensPossible.size());
            Coup c = new Coup(ensPossible.get(x));
            return c;
        }
    }

    private Boolean exploration(Gaufre g) {
        Joueur joueur;
        Boolean r;
        if (g.getJoueurCourant() == g.getJoueur2()) {
            r = false;
        } else {
            r = true;
        }

        if ((joueur = g.estFinie()) != null) {
            // System.out.println("Vainqueur: " + joueur.getNum() + "\n");
            vainqueur.put(g.clone(), joueur == g.getJoueur2());
            return joueur == g.getJoueur2();
        } else if (vainqueur.containsKey(g)) {
            return vainqueur.get(g);
        } else {
            for (int i = 0; i < g.getNbLignes(); i++) {
                for (int j = 0; j < g.getNbColonnes(); j++) {
                    Coup c = new Coup(i, j);
                    if (g.estJouable(c)) {
                        joueur = g.getJoueurCourant();
                        g.jouer(c);
                        // System.out.println("Exploration du coup" + c);
                        Boolean resultat = exploration(g);
                        g.dejouer();
                        if (joueur == g.getJoueur2()) {
                            r = r || resultat;
                            if (resultat) {
                                coups.put(g.clone(), c);
                            }
                        } else if (joueur == g.getJoueur1()) {
                            r = r && resultat;
                        }
                    }
                }
            }
            vainqueur.put(g.clone(), r);
            return r;
        }
    }

}
