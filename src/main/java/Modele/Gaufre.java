package Modele;

import java.awt.Point;
import java.util.Random;
import java.util.ArrayList;

public class Gaufre{

    private Joueur[] joueurs;
    private Joueur joueurCourant;
    private boolean[][] plateau;
    private Historique historique;

    public Gaufre(Joueur[] joueurs, int nbL, int nbC) {
        this.joueurs = joueurs;
        Random rand = new Random();
        this.joueurCourant = joueurs[rand.nextInt(2)];
        this.plateau = new boolean[nbL][nbC];
        this.historique = new Historique();
    }

    //Getters
    public Joueur[] getJoueur() {
        return this.joueurs;
    }

    public Joueur getJoueurCourant() {
        return this.joueurCourant;
    }

    public boolean[][] getPlateau() {
        return this.plateau;
    }

    public Historique getHistorique() {
        return this.historique;
    }

    public int getLignes(){
        return plateau.length;
    }

    public int getColonnes(){
        return plateau[0].length;
    }

    public boolean getCase(int l, int c){
        return plateau[l][c];
    }

    public boolean getCase(Point p){
        return getCase(p.y,p.x);
    }

    //Setters
    public void setJoueur(Joueur[] joueur) {
        this.joueurs = joueur;
    }

    public void setJoueurCourant(Joueur joueurCourant) {
        this.joueurCourant = joueurCourant;
    }

    public void setPlateau(boolean[][] plateau) {
        this.plateau = plateau;
    }

    public void setHistorique(Historique historique) {
        this.historique = historique;
    }

    public void setCase(int l, int c, boolean b){
        this.plateau[l][c] = b;
    }
    
    public void setCase(Point p, boolean b){
        setCase(p.y,p.x,b);
    }

    public String toString() {
        String s = "Gaufre{\n joueurs= ";
        for (Joueur j : this.joueurs) {
            s += j.toString() + "\n";
        }
        s += "joueurCourant= " + this.joueurCourant + "\n";
        s += "plateau= ";
        for (int i = 0; i < plateau.length; i++) {
            for (int j = 0; j < plateau[0].length; j++) {
                s += plateau[i][j] + " ";
            }
            s += "\n";
        }
        s += "historique= " + this.historique + "\n";
        return s;
    }

    void changerJoueur() {
        joueurCourant = joueurs[(joueurCourant.getNum() + 1) % 2];
    }

    // renvoie la liste des Point manges si le coup est possible, null sinon
    public ArrayList<Point> peutJouer(Coup coup) {

        int x = coup.getPosition().x;
        int y = coup.getPosition().y;
        
        if (x > 0 && x <= plateau.length && y > 0 && y <= plateau[0].length) {
            ArrayList<Point> positionMangees = new ArrayList<Point>();
            for (int i = x; i < plateau.length; i++) {
                for (int j = y; j < plateau[0].length; j++) {
                    if (plateau[i][j]) positionMangees.add(new Point(i, j));
                }
            }
            return positionMangees;
        }
        return null;
    }

    public boolean jouerSansHistorique(Coup coup) {
        
        ArrayList<Point> positionMangees;
        positionMangees = peutJouer(coup);

        if (positionMangees != null) {
            
            coup.setJoueur(joueurCourant);
            coup.setPositionMangees(positionMangees);

            for (Point p : positionMangees) {
                setCase(p, false);
            }

            changerJoueur();
            return true;
        }
        return false;
    }

    public boolean jouer(Coup coup) {
            
        if (jouerSansHistorique(coup)) {
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

    public void reinitialiser() {
        //TODO
        return;
    }
}