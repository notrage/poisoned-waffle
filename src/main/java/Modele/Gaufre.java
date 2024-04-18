package Modele;

import java.awt.Point;
import java.util.Random;
import java.util.ArrayList;

public class Gaufre{

    private Joueur[] joueurs;
    private int joueurCourant;
    private boolean[][] plateau;
    private Historique historique;

    public Gaufre(int nbL, int nbC) {
        Joueur j1 = new Joueur(1);
        Joueur j2 = new Joueur(2);
        this.joueurs = new Joueur[]{j1, j2};
        Random rand = new Random();
        this.joueurCourant = rand.nextInt(2);
        this.plateau = new boolean[nbL][nbC];
        for (int i = 0; i < nbL; i++){
            for (int j = 0; j < nbC; j++){
                this.plateau[i][j] = true;
            }
        }
        for (int i = 0; i < nbL; i++) {
            for (int j = 0; j < nbC; j++) {
                setCase(i, j, true);
            }
        }
        this.historique = new Historique();
    }

    //Getters
    public Joueur[] getJoueur() {
        return this.joueurs;
    }

    public Joueur getJoueurCourant() {
        return this.joueurs[this.joueurCourant];
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
        this.joueurCourant = joueurCourant.getNum()-1;
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

    //Autres methodes
    @Override
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
        this.joueurCourant =(this.joueurCourant+ 1) % 2;
    }

    // renvoie la liste des Point manges si le coup est possible, null sinon
    public ArrayList<Point> peutJouer(Coup coup) {

        int x = coup.getPosition().x;
        int y = coup.getPosition().y;
        
        if (x >= 0 && x < plateau.length && y >= 0 && y < plateau[0].length) {
            ArrayList<Point> positionMangees = new ArrayList<Point>();
            for (int i = x; i < plateau.length; i++) {
                for (int j = y; j < plateau[0].length; j++) {
                    if (plateau[i][j]) positionMangees.add(new Point(i, j));
                }
            }
            return positionMangees;
        }
        return new ArrayList<Point>();
    }

    public boolean jouerSansHistorique(Coup coup) {
        
        ArrayList<Point> positionMangees = peutJouer(coup);

        if (!positionMangees.isEmpty()) {
            
            coup.setJoueur(getJoueurCourant());
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
            getHistorique().fait(coup);
            return true;
        }
        return false;
    }

    public boolean dejouer() {
        if (estDejouable()){
            Coup c = getHistorique().defait();
            if (c == null){
                return false;
            }
            for (Point p : c.getPositionMangees()){
                setCase(p, true);
            }
            return true;
        }
        return false;
    }

    public boolean rejouer() {
        if (estRejouable()){
            return jouerSansHistorique(getHistorique().refait());
        }
        // refaire le dernier coup annulé (update historique)
        return false;
    }

    public Joueur estFinie() {
        if (plateau[0][0] == false) {
            return getJoueurCourant();
        }
        else if (plateau[0][1] == false && plateau[1][0] == false) {
            return joueurs[(joueurCourant + 1) % 2];
        }
        return null;
    }

    public boolean estDejouable(){
        return getHistorique().peutDefaire();
    }

    public boolean estRejouable(){
        return getHistorique().peutRefaire();
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
        historique.raz();
        for (int i = 0; i < plateau.length; i++) {
            for (int j = 0; j < plateau[0].length; j++) {
                setCase(i, j, true);
            }
        }
        Random rand = new Random();
        this.joueurCourant = rand.nextInt(2);
        return;
    }
}