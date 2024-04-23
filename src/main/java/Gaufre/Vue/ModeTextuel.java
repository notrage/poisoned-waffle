package Gaufre.Vue;

import Gaufre.Modele.Gaufre;
import Gaufre.Modele.Coup;
import Gaufre.Modele.IA;
import Gaufre.Modele.IAcoupGagnant;
import Gaufre.Modele.IAaleatoire;
import Gaufre.Modele.IAexploration;

import java.util.Scanner;

public class ModeTextuel {
    Gaufre gaufre;
    Scanner sc;
    IA ia;

    public ModeTextuel(Gaufre g) {
        gaufre = g;
        sc = new Scanner(System.in);
    }

    public void afficher() {
        for (int i = 0; i < gaufre.getNbLignes(); i++) {
            System.out.print("| ");
            for (int j = 0; j < gaufre.getNbColonnes(); j++) {
                if (i == 0 && j == 0) {
                    System.out.print("∅ | ");
                    continue;
                }
                if (gaufre.getCase(i, j)) {
                    System.out.print("O | ");
                } else {
                    System.out.print("X | ");
                }
            }
            System.out.println();
        }
    }

    public boolean jouer() {
        System.out.println("Entrez la ligne : ");
        int l = sc.nextInt();
        System.out.println("Entrez la colonne : ");
        int c = sc.nextInt();
        Coup coup = new Coup(l, c);
        return gaufre.jouer(coup);
    }

    public void annuler() {
        gaufre.dejouer();
    }

    public void refaire() {
        gaufre.rejouer();
    }

    public void scores() {
        System.out.println("Scores : \n"
                + "Joueur 1 : " + gaufre.getJoueur1().getScore() + "\n"
                + "Joueur 2 : " + gaufre.getJoueur2().getScore());
    }

    public void sauvegarder() {
        System.out.println("Entrez le nom du fichier : ");
        String nom = sc.nextLine();
        try {
            gaufre.sauvegarder(nom);
        } catch (Exception e) {
            System.out.println("Erreur lors de la sauvegarde");
        }
    }

    public void charger() {
        System.out.println("Entrez le nom du fichier : ");
        String nom = sc.nextLine();
        try {
            gaufre = new Gaufre(nom);
        } catch (Exception e) {
            System.out.println("Erreur lors du chargement");
        }
    }

    public void reset() {
        gaufre.reinitialiser();
    }

    public boolean partieFinie() {
        return gaufre.estFinie() != null;
    }

    public int getJoueurCourant() {
        return gaufre.getJoueurCourant().getNum();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Entrez la taille de la gaufre \n Largeur : ");
        int l = sc.nextInt();
        System.out.println("Hauteur : ");
        int c = sc.nextInt();
        Gaufre g = new Gaufre(l, c);
        System.out.println("Nombre de joueurs : ");
        int nbJoueurs = 0;
        while (nbJoueurs < 1 || nbJoueurs > 2) {
            nbJoueurs = sc.nextInt();
        }
        ModeTextuel mt = new ModeTextuel(g);
        System.out.println("Les differentes commandes sont :\n"
                + "- joue\n"
                + "- affiche\n"
                + "- annule\n"
                + "- refait\n"
                + "- sauve\n"
                + "- charge\n"
                + "- scores\n"
                + "- reset\n");

        if (nbJoueurs == 1) {
            mt.ia = new IAexploration();
            mt.ia.init(g);
            if (mt.gaufre.getJoueurCourant() == mt.gaufre.getJoueur2()) {
                System.out.println("L'IA commence !");
                Coup co = mt.ia.coupSuivant();
                System.out.println("L'IA joue en  " + co.getPosition().x + ", " + co.getPosition().y);
                mt.gaufre.jouer(co);
                mt.afficher();
            }
        }
        while (sc.hasNext()) {
            System.out.println("Au tour du Joueur " + mt.getJoueurCourant());
            String com = sc.nextLine();
            switch (com.trim()) {
                case "joue":
                    if (!mt.jouer()) {
                        System.out.println("Coup invalide");
                    } else if (nbJoueurs == 1 && !mt.partieFinie()) {
                        mt.afficher();
                        Coup co = mt.ia.coupSuivant();
                        System.out.println("L'IA joue en  " + co.getPosition().x + ", " + co.getPosition().y);
                        mt.gaufre.jouer(co);
                    }
                    break;
                case "annule":
                    mt.annuler();
                    if (nbJoueurs == 1) {
                        mt.annuler();
                    }
                    break;
                case "refait":
                    mt.refaire();
                    if (nbJoueurs == 1) {
                        mt.refaire();
                    }
                    break;
                case "sauve":
                    mt.sauvegarder();
                    break;
                case "charge":
                    mt.charger();
                    break;
                case "scores":
                    mt.scores();
                    break;
                case "reset":
                    mt.reset();
                    break;
                default:
                    if (com.trim() != "") {
                        System.out.println(com.trim());
                        System.out.println("Commande invalide");
                        break;
                    }
            }
            mt.afficher();
            if (mt.partieFinie()) {
                System.out.println("Partie terminée");
                System.out.println("Vainqueur : " + mt.gaufre.estFinie());
            }
        }

        sc.close();
    }

}