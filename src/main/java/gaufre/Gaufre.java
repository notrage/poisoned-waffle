package gaufre;

import java.awt.Point;
import java.util.ArrayList;

class Gaufre{

    private Joueur joueur;
    private boolean[][] plateau;
    private Historique historique;

    public Gaufre(Joueur joueur, int nbL, int nbC) {
        this.joueur = joueur;
        this.plateau = new boolean[nbL][nbC];
        this.historique = new Historique();
    }

    public Joueur getJoueur() {
        return this.joueur;
    }

    public boolean[][] getPlateau() {
        return this.plateau;
    }

    public Historique getHistorique() {
        return this.historique;
    }

    public void setJoueur(Joueur joueur) {
        this.joueur = joueur;
    }

    public void setPlateau(boolean[][] plateau) {
        this.plateau = plateau;
    }

    public void setHistorique(Historique historique) {
        this.historique = historique;
    }

    public String toString() {
        return "Gaufre : " + this.plateau + " joueur : " + this.joueur + " historique : " + 
            this.historique;
    }

    public boolean jouer(Coup coup) {
        
        int x = coup.getPosition().x;
        int y = coup.getPosition().y;
        
        if (x > 0 && x <= plateau.length && y > 0 && y <= plateau[0].length) {
            coup.setJoueur(joueur);
            ArrayList<Point> positionMangees = new ArrayList<Point>();
            for (int i = x; i < plateau.length; i++) {
                for (int j = y; j < plateau[0].length; j++) {
                    if (plateau[i][j]) positionMangees.add(new Point(i, j));
                }
            }
            coup.setPositionMangees(positionMangees);
            
            for (Point p : positionMangees) {
                plateau[p.x][p.y] = false;
            }
            historique.fait(coup);
            return true;
        }
        return false;
    }

    public void dejouer() {
        //TODO
        // annuler le dernier coup (update historique)
        return;
    }

    public void rejouer() {
        //TODO
        // refaire le dernier coup annulé (update historique)
        return;
    }

    public boolean estFinit() {
        //TODO
        return false;
    }

    public void sauvegarder() {
        //TODO
        return;
    }

    public void restaurer(String nomFichier) {
        //TODO
        return;
    }

    public void renitialiser() {
        //TODO
        return;
    }
}