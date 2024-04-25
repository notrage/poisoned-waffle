package Gaufre.Modele;

import java.util.HashMap;

public class IAexploration implements IA {
    HashMap<Gaufre, Coup> coups;
    HashMap<Gaufre, Boolean> vainqueur;
    Gaufre gaufreInitiale;

    public IAexploration() {
        vainqueur = new HashMap<>();
        coups = new HashMap<>();
    }

    public void init(Gaufre g) {
        gaufreInitiale = g;
        if (exploration(g.clone())) {
            System.out.println("Victoire trouvée !");
        } else {
            System.out.println("Pas de victoire possible");
        }

    }

    public Coup coupSuivant() {
        return coups.get(gaufreInitiale);
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
            //System.out.println("Vainqueur: " + joueur.getNum() + "\n");
            vainqueur.put(g.clone(), joueur == g.getJoueur2());
            return joueur == g.getJoueur2();
        } else if (vainqueur.containsKey(g)){
           return vainqueur.get(g);
        } else {
            for (int i = 0; i < g.getNbLignes(); i++) {
                for (int j = 0; j < g.getNbColonnes(); j++) {
                    Coup c = new Coup(i, j);
                    if (g.estJouable(c)) {
                        joueur = g.getJoueurCourant();
                        g.jouer(c);
                        //System.out.println("Exploration du coup" + c);
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
