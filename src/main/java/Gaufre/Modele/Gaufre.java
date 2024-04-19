package Gaufre.Modele;

import java.awt.Point;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Random;
import java.util.ArrayList;

public class Gaufre{

    private Joueur[] joueurs;
    private int joueurCourant;
    private int[] plateau;
    private Historique historique;
    private int nbLignes;
    private int nbColonnes;

    public Gaufre(int nbL, int nbC) {
        Joueur j1 = new Joueur(1);
        Joueur j2 = new Joueur(2);
        setnbLignes(nbL);
        setnbColonnes(nbC);
        this.joueurs = new Joueur[]{j1, j2};
        Random rand = new Random();
        this.joueurCourant = rand.nextInt(2);
        this.plateau = new int[nbL];
        for (int i = 0; i < nbL; i++){
            this.plateau[i] = nbC;;
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

    public int[] getPlateau() {
        return this.plateau;
    }

    public Historique getHistorique() {
        return this.historique;
    }

    public int getLignes(){
        return nbLignes;
    }

    public int getColonnes(){
        return nbColonnes;
    }

    public boolean getCase(int l, int c){
        return plateau[l] > c;
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

    public void setPlateau(int [] plateau) {
        this.plateau = plateau;
    }

    public void setHistorique(Historique historique) {
        this.historique = historique;
    }

    public void setCase(int l, int c, boolean b){
        if (b){
            plateau[l] = c;
        }
        else{
            plateau[l] = Math.min(plateau[l],c);
        }
    }
    
    public void setCase(Point p, boolean b){
        setCase(p.y,p.x,b);
    }

    public void setnbLignes(int nbL){
        this.nbLignes = nbL;
    }

    public void setnbColonnes(int nbC){
        this.nbColonnes = nbC;
    }

    //Autres methodes
    @Override
    public String toString() {
        String s = "Gaufre{\n joueurs= ";
        for (Joueur j : this.joueurs) {
            s += j.toString() + "\n";
        }
        s += "joueurCourant= " + this.joueurCourant + "\n";
        s += "plateau= \n";
        s += nbColonnes + " " + nbLignes + "\n";
        for (int i = 0; i < plateau.length; i++) {
                s += plateau[i] + " ";
            }
        s += "\n";
        s += "historique= " + this.historique + "\n";
        return s;
    }

    void changerJoueur() {
        this.joueurCourant =(this.joueurCourant+ 1) % 2;
    }

    // renvoie la liste des Point manges si le coup est possible, null sinon
    public ArrayList<Point> peutJouer(Coup coup) {

        int l = coup.getPosition().x;
        int c = coup.getPosition().y;
        
        if (l >= 0 && l < getLignes() && c >= 0 && c < getColonnes() && getCase(l, c)) {
            ArrayList<Point> positionMangees = new ArrayList<Point>();
            for (int i = l; i < getLignes(); i++) {
                for (int j = c; j < getColonnes(); j++) {
                    if (!getCase(i, j)) {
                        return positionMangees;
                    }
                    positionMangees.add(new Point(i, j));
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
        if (plateau[0] == 0) {
            return getJoueurCourant();
        }
        else if (plateau[1] == 0 && plateau[0] == 1) {
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
/* 
    public void sauvegarder(String nomFichier) {
        PrintStream p = new PrintStream(new OutputStream(nomFichier));
        p.println(getLignes() + " " + getColonnes());
        for (int i = 0; i < getLignes();i++){
            p.print(plateau[i] + " ");
        }
        p.println();
        p.println(joueurCourant);
        p.println(historique.toString());
        p.close();
        return;
    }

    public void restaurer(String nomFichier) {
        Scanner sc = new Scanner(new File(nomFichier));
        setnbLignes(sc.nextInt());
        setnbColonnes(sc.nextInt());
        for (int i = 0; i < getLignes(); i++){
            plateau[i] = sc.nextInt();
        }
        joueurCourant = sc.nextInt();
        
        return;
    }
*/
    public void reinitialiser() {
        historique.raz();
        for (int i = 0; i < getLignes(); i++) {
            plateau[i] = getColonnes();
        }
        Random rand = new Random();
        this.joueurCourant = rand.nextInt(2);
        return;
    }
}