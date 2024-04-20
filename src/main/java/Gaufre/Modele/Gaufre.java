package Gaufre.Modele;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Random;
import java.util.Objects;

public class Gaufre {

    private Joueur joueur1, joueur2, joueurCourant;
    private int[] plateau;
    private Historique historique;
    private int nbLignes;
    private int nbColonnes;

    //Constructeurs
    public Gaufre(int nbL, int nbC) {
        setJoueur1(new Joueur(1));
        setJoueur2(new Joueur(2));
        setNbLignes(nbL);
        setNbColonnes(nbC);
        Random rand = new Random();
        int r = rand.nextInt() % 2;
        if (r == 0)
            setJoueurCourant(joueur1);
        else 
            setJoueurCourant(joueur2);
        
        setPlateau(new int[nbL]);
        for (int i = 0; i < nbL; i++){
            plateau[i] = nbC;
        }
        setHistorique(new Historique());
    }

    public Gaufre(String nomFichier) {

        try (BufferedReader reader = new BufferedReader(new FileReader(nomFichier))) {
            String line = reader.readLine();
            String dimensions[] = line.split(" ");

            setJoueur1(new Joueur(1));
            setJoueur2(new Joueur(2));
            setNbLignes(Integer.parseInt(dimensions[0]));
            setNbColonnes(Integer.parseInt(dimensions[1]));
            
            setPlateau(new int[getNbLignes()]);
            for (int i = 0; i < getNbLignes(); i++){
                plateau[i] = getNbColonnes();
            }
            setHistorique(new Historique());
            
            // Lecture des coups faits
            if ((line = reader.readLine()) != null){
                if (!(line = line.substring(1, line.length() - 1)).isEmpty()){
                    String[] faits = line.split(" ");
                    for (String f: faits){
                        jouer(new Coup(f));
                    }
                }
            }
            // Lecture des coups défaits
            if ((line = reader.readLine()) != null){
                if (!(line = line.substring(1, line.length() - 1)).isEmpty()){
                    String[] defaits = line.split(" ");
                    for (String d: defaits){
                        getHistorique().empileDefait(new Coup(d));
                    }
                } 
            }
            // Lecture du joueur courant
            line = reader.readLine();
            if (Integer.parseInt(line) == 1)
                setJoueurCourant(joueur1);
            else 
                setJoueurCourant(joueur2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
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

    public void sauvegarder(String nomFichier) throws Exception {
        PrintStream ps = new PrintStream(new FileOutputStream(nomFichier));
        ps.println(getNbLignes() + " " + getNbColonnes());
        ps.println(getHistorique().pourSauvegarde());
        ps.print(getJoueurCourant().getNum());
        ps.close();
        return;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Gaufre that = (Gaufre) o;
        if (getJoueurCourant().getNum() != that.getJoueurCourant().getNum())
            return false;
        for (int i = 0; i < getNbLignes(); i++){
            if (getPlateau()[i] != that.getPlateau()[i]) return false;
        } 
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPlateau(), getJoueurCourant());
    } 
}