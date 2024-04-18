package gaufre;

import java.awt.Point;
import java.util.ArrayList;

class Coup {

    private Point case;
    private ArrayList<Point> positionMangees;
    private Joueur joueur;

    public Coup(Point case, ArrayList<Point> positionMangees, Joueur joueur) {
        this.case = case;
        this.positionMangees = new ArrayList<Point>(positionMangees);
        this.joueur = joueur;
    }

    public Point getCase() {
        return this.case;
    }

    public ArrayList<Point> getPositionMangees() {
        return this.positionMangees;
    }

    public Joueur getJoueur() {
        return this.joueur;
    }

    public String toString() {
        return "Coup : " + this.case + " mangees : " + this.positionMangees + " joueur : " + this.joueur;
    }

    public void jouer(Gaufre gaufre) {
        //TODO
        return;
    }

    public void annuler(Gaufre gaufre) {
        //TODO
        return;
    }    
}