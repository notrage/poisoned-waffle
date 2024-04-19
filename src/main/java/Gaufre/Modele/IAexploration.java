package Gaufre.Modele;

import java.util.HashMap;

public class IAexploration implements IA {
    Gaufre gaufre;
    HashMap<Gaufre, Joueur> vainqueur;
    HashMap<Gaufre, Coup> coups;
    public IAexploration() {
    }

    public void init(Gaufre g) {
        System.out.println("Debut exploration");
        coups = new HashMap<>();
        vainqueur = new HashMap<>();
        gaufre = g.clone();
        exploration(g, g.getJoueurCourant());
        System.out.println("Fin exploration");

    }

    public Coup coupSuivant() {
        return coups.get(gaufre);
    }

    private Boolean exploration(Gaufre g, Joueur j) {
        Joueur v;
        Coup c;
        if ((v = g.estFinie()) != null) {
            System.out.println("Fin de partie, victoire du " + v);
            vainqueur.put(g, v);
            return v == g.getJoueur2(); // true si l'IA gagne
        } else if (vainqueur.containsKey(g)) {
            System.out.println("Solution déjà explorée");
            return vainqueur.get(g) == g.getJoueur2(); // true si l'IA gagne
        } else {
            for (int x = 0; x < g.getNbLignes(); x++){
                for (int y = 0; y < g.getNbColonnes(); x++){
                    c = new Coup(x, y);
                    if (g.jouer(c)){
                        System.out.println("Test du coup " + c);
                        Gaufre clone = g.clone();
                        boolean res = exploration(clone, g.getJoueur1());
                        if (j == g.getJoueur2() && res){ // Arbre OU 
                            coups.put(g, c);
                            return true;
                        } else if (j == g.getJoueur1() && !res){ // Arbre ET 
                            return false;
                        }
                        g.dejouer();
                    }
                }
            }
            return true;
        }
    }

}
