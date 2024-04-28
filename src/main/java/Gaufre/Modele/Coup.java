package Gaufre.Modele;

import java.awt.Point;

@SuppressWarnings("rawtypes")
public class Coup implements Comparable {

    private Point position;
    private int[] ancienPlateau;
    private Joueur joueur;
    private int priorite;

    // Constructeurs
    public Coup(Point position) {
        this.position = position;
        priorite = 0;
        this.ancienPlateau = null;
        this.joueur = null;
    }

    public Coup(int l, int c) {
        this(new Point(l, c));
    }

    public Coup(String sauvegarde) {
        sauvegarde = sauvegarde.substring(1, sauvegarde.length() - 1);

        String parties[] = sauvegarde.split(";");

        String point[] = parties[0].substring(1, parties[0].length() - 1).split(",");

        position = new Point((int) Integer.parseInt(point[0]), (int) Integer.parseInt(point[1]));

        String plateau[] = parties[1].substring(1, parties[1].length() - 1).split(",");
        int taille = plateau.length;
        int[] ancienPlateau = new int[taille];
        for (int i = 0; i < taille; i++) {
            ancienPlateau[i] = Integer.parseInt(plateau[i]);
        }
        joueur = new Joueur(parties[2]);

        priorite = 0;
    }

    // Getters
    public Point getPosition() {
        return this.position;
    }

    public int[] getAncienPlateau() {
        return this.ancienPlateau;
    }

    public Joueur getJoueur() {
        return this.joueur;
    }

    public int getPriorite() {
        return this.priorite;
    }

    // Setters
    public void setAncienPlateau(int[] ancienPlateau) {
        this.ancienPlateau = ancienPlateau;
    }

    public void setJoueur(Joueur joueur) {
        this.joueur = joueur;
    }

    public void setPriorite(int p) {
        this.priorite = p;
    }

    public String pourSauvegarde() {
        String s = "{";
        s += "(" + (int) getPosition().getX() + "," + (int) getPosition().getY() + ");";
        String plateau = "";
        for (int i = 0; i < getAncienPlateau().length; i++) {
            plateau += getAncienPlateau()[i];
            if (i != getAncienPlateau().length - 1)
                plateau += ",";
        }
        s += "[" + plateau + "];";
        s += getJoueur().pourSauvegarde();
        s += "}";
        return s;
    }

    // Autres méthodes
    @Override
    public String toString() {
        return "Coup{" +
                "position=" + position.getX() + " " + position.getY() +
                ", ancienPlateau=" + ancienPlateau.toString() +
                ", joueur=" + joueur.toString() +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        Coup that = (Coup) o;
        return Integer.compare(getPriorite(), that.getPriorite());
    }
}