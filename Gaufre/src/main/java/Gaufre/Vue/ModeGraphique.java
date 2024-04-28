package Gaufre.Vue;

import Gaufre.Modele.Gaufre;
import Gaufre.Modele.IA;
import Gaufre.Modele.Coup;
import Gaufre.Configuration.Config;

public class ModeGraphique {
    private Gaufre gaufre;
    private IA ia;
    private int nbJoueurs;

    public ModeGraphique(Gaufre g) {
        gaufre = g;
    }

    public void setIA(IA ia) {
        this.ia = ia;
        ia.init(gaufre);
    }

    public IA getIA(){
        return ia;
    }

    public Gaufre getGaufre() {
        return gaufre;
    }

    public int getNbJoueurs() {
        return nbJoueurs;
    }

    public void setNbJoueurs(int nbJoueurs) {
        this.nbJoueurs = nbJoueurs;
    }

    public void lancer() {
        InterfaceGraphique.demarrer(this);
    }

    public boolean jouer(int l, int c) {
        Coup coup = new Coup(l, c);
        return gaufre.jouer(coup);
    }

    public Coup jouerIA() {
        Coup coupIA = ia.coupSuivant();
        gaufre.jouer(coupIA);
        return coupIA;
    }

    public void reset() {
        gaufre.reinitialiser();
    }

    public void annuler() {
        gaufre.dejouer();
        if (nbJoueurs == 1) { // Si on joue contre l'IA on annule 2 fois
            gaufre.dejouer();
        }
    }

    public void refaire() {
        gaufre.rejouer();
        if (nbJoueurs == 1) { // Si on joue contre l'IA on rejoue 2 fois
            gaufre.rejouer();
        }
    }

    public boolean peutAnnuler() {
        return gaufre.estDejouable() && (gaufre.estFinie() == null);
    }

    public boolean peutRefaire() {
        return gaufre.estRejouable();
    }

    public boolean estFini() {
        return (gaufre.estFinie() != null);
    }

    public void plus() {
            gaufre.resize(gaufre.getNbLignes() + 1, gaufre.getNbColonnes() + 1);
    }
    
    public void moins() {
        if (gaufre.getNbLignes() > 1 && gaufre.getNbColonnes() > 1) {
            gaufre.resize(gaufre.getNbLignes() - 1, gaufre.getNbColonnes() - 1);
        }
    }

    public void resize(int l, int c) {
        gaufre.resize(l, c);
    }

    public void sauvegarder() {
        try {
            gaufre.sauvegarder("save.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean charger() {
        try {
            Gaufre newgaufre = new Gaufre("save.txt");
            if (newgaufre != null){
                gaufre = newgaufre;
            }
            Config.debug("Chargement réussi");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
