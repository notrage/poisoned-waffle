package Gaufre.Modele;

import java.awt.Point;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;

public class Gaufre {

    private Joueur joueur1, joueur2, joueurCourant;
    private int[] plateau;
    private Historique historique;
    private int nbLignes;
    private int nbColonnes;

    //Constructeurs
    public Gaufre(int nbL, int nbC) {
        joueur1 = new Joueur(1);
        joueur2 = new Joueur(2);
        setNbLignes(nbL);
        setNbColonnes(nbC);
        Random rand = new Random();
        int r = rand.nextInt() % 2;
        if (r == 0){
            joueurCourant = joueur1;
        } else {
            joueurCourant = joueur2; 
        }
        plateau = new int[nbL];
        for (int i = 0; i < nbL; i++){
            plateau[i] = nbC;
        }
        historique = new Historique();
    }

    //Getters
    public Joueur getJoueur1() {
        return joueur1;
    }

    public Joueur getJoueur2() {
        return joueur2;
    }

    public Joueur getJoueurCourant() {
        return joueurCourant;
    }

    public int[] getPlateau() {
        return plateau;
    }

    public Historique getHistorique() {
        return historique;
    }

    public int getNbLignes(){
        return nbLignes;
    }

    public int getNbColonnes(){
        return nbColonnes;
    }

    public boolean getCase(int l, int c){
        return l >= 0 && l < getNbLignes() && c >= 0 && c < getNbColonnes() && plateau[l] > c;
    }

    public boolean getCase(Point p){
        return getCase(p.x,p.y);
    }

    //Setters
    public void setJoueur1(Joueur joueur1) {
        this.joueur1 = joueur1;
    }

    public void setJoueur2(Joueur joueur2) {
        this.joueur2 = joueur2;
    }

    public void setJoueurCourant(Joueur joueurCourant) {
        this.joueurCourant = joueurCourant;
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

    public void setNbLignes(int nbL){
        nbLignes = nbL;
    }

    public void setNbColonnes(int nbC){
        nbColonnes = nbC;
    }

    //Autres methodes
    public String toString() {
        String s = "Gaufre{\n joueurs= \n";
        s += joueur1.toString() + " \n" + joueur2.toString() + "\n";
        s += "joueurCourant= " + joueurCourant + "\n";
        s += "plateau= \n";
        s += nbColonnes + " " + nbLignes + "\n";
        for (int i = 0; i < plateau.length; i++) {
                s += plateau[i] + " ";
            }
        s += "\n";
        s += "historique= " + historique.toString() + "\n";
        return s;
    }

    void changerJoueur() {
        if (getJoueurCourant() == getJoueur1())
            setJoueurCourant(getJoueur2());
        else
            setJoueurCourant(getJoueur1());
    }

    public boolean estJouable(Coup coup) {
        return getCase(coup.getPosition());
    }

    public int[] clonePlateau() {
        int taille = getPlateau().length;
        int[] clonePlateau = new int[taille];
        for (int i = 0; i < taille; i++) {
            clonePlateau[i] = getPlateau()[i];
        }
        return clonePlateau;
    }

    public Gaufre clone() {
        Gaufre copieGaufre = new Gaufre(1, 1);
        copieGaufre.setNbLignes(getNbLignes());
        copieGaufre.setNbColonnes(getNbColonnes());
        Joueur copieJoueur1 = getJoueur1().clone();
        Joueur copieJoueur2 = getJoueur2().clone();
        copieGaufre.setJoueur1(copieJoueur1);
        copieGaufre.setJoueur2(copieJoueur2);

        if (getJoueurCourant() == getJoueur1())
            copieGaufre.setJoueurCourant(copieJoueur1);
        else
            copieGaufre.setJoueurCourant(copieJoueur2);
            
        copieGaufre.setPlateau(clonePlateau());
        return copieGaufre;
    }

    private boolean jouerSansHistorique(Coup coup) {
        
        if (estJouable(coup)) {
            coup.setAncienPlateau(clonePlateau());
            coup.setJoueur(getJoueurCourant());
            for (int i = coup.getPosition().x; i < getNbLignes(); i++) {
                plateau[i] = Math.min(coup.getPosition().y, plateau[i]);
            }
            return true;
        }
        return false;
    }

    public boolean jouer(Coup coup) {
            
        if (jouerSansHistorique(coup)) {
            getHistorique().fait(coup);
            changerJoueur();
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
            setPlateau(c.getAncienPlateau());
            changerJoueur();
            return true;
        }
        return false;
    }

    public boolean rejouer() {
        if (estRejouable()){
            changerJoueur();
            return jouerSansHistorique(getHistorique().refait());
        }
        // refaire le dernier coup annulé (update historique)
        return false;
    }

    public Joueur estFinie() {
        if (!getCase(0,0)) {
            getJoueurCourant().incrementScore();
            return getJoueurCourant();
        }
        else if (!getCase(0,1) && !getCase(1,0)) {
            if (getJoueurCourant() == getJoueur1()){
                getJoueur2().incrementScore();
                return getJoueur2();
            } else {
                getJoueur1().incrementScore();
                return getJoueur1();
            }
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
        p.println(getNbLignes() + " " + getNbColonnes());
        print(Joueurs.)

        for (int i = 0; i < getNbLignes();i++){
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
        setNbLignes(sc.nextInt());
        setNbColonnes(sc.nextInt());
        for (int i = 0; i < getNbLignes(); i++){
            plateau[i] = sc.nextInt();
        }
        joueurCourant = sc.nextInt();
        historique = new Historique();
        //fait
        sc.nextLine();
        while (!sc.equals('}')){ 
            historique.fait(new Coup(sc.nextLine()));
        }
        //defait
        sc.nextLine();
        while (!sc.equals('}')){ 
            historique.defait(new Coup(sc.nextLine()));
        }
        sc.close();
        return;
    }
    */
    public void reinitialiser() {
        historique.raz();
        for (int i = 0; i < getNbLignes(); i++) {
            plateau[i] = getNbColonnes();
        }
        Random rand = new Random();
        int r = rand.nextInt() % 2;
        if (r == 0){
            joueurCourant = joueur1;
        } else {
            joueurCourant = joueur2; 
        }
        return;
    }
}