package gaufre;

import java.awt.Point;
import java.util.ArrayList;

class Coup {

    private Point position;
    private ArrayList<Point> positionMangees;
    private Joueur joueur;

    public Coup(Point position) {
        this.position = position;
        this.positionMangees = null;
        this.joueur = null;
    }

    public Coup(int l, int c){
        this.position = new Point(l, c);
        this.positionMangees = null;
        this.joueur = null;
    }

    public Point getPosition() {
        return this.position;
    }

    public ArrayList<Point> getPositionMangees() {
        return this.positionMangees;
    }

    public Joueur getJoueur() {
        return this.joueur;
    }

    public void setPositionMangees(ArrayList<Point> positionMangees) {
        this.positionMangees = positionMangees;
    }

    public void setJoueur(Joueur joueur) {
        this.joueur = joueur;
    }

    public String toString() {
        return "Coup : position :" + this.position + " mangees : " + this.positionMangees + " joueur : " + this.joueur;
    }
}