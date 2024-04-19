package Gaufre.Modele;

import java.awt.Point;

public class Coup {

    private Point position;
    private int[] ancienPlateau;
    private Joueur joueur;

    //Constructeurs
    public Coup(Point position) {
        this.position = position;
        this.ancienPlateau = null;
        this.joueur = null;
    }

    public Coup(int l, int c){
        this(new Point(l, c));
    }

    //Getters
    public Point getPosition() {
        return this.position;
    }

    public int [] getAncienPlateau() {
        return this.ancienPlateau;
    }

    public Joueur getJoueur() {
        return this.joueur;
    }

    //Setters
    public void setAncienPlateau(int[] ancienPlateau) {
        this.ancienPlateau = ancienPlateau;
    }

    public void setJoueur(Joueur joueur) {
        this.joueur = joueur;
    }

    //Autres méthodes
    @Override
    public String toString() {
        return "Coup{" +
                "position=" + position.getX() +" " + position.getY() +
                ", ancienPlateau=" + ancienPlateau.toString() +
                ", joueur=" + joueur.toString() +
                '}';
    }
}