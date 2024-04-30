package Gaufre.Modele;

import java.awt.Point;
import java.util.ArrayList;

public class Historique {
    private ArrayList<Coup> faits;
    private ArrayList<Coup> defaits;

    // Constructeur
    public Historique() {
        this.faits = new ArrayList<Coup>();
        this.defaits = new ArrayList<Coup>();
    }

    // Getteurs
    public ArrayList<Coup> getFaits() {
        return this.faits;
    }

    public ArrayList<Coup> getDefaits() {
        return this.defaits;
    }

    // Autres méthodes
    public int getNbFaits() {
        return this.faits.size();
    }

    public int getNbDefaits() {
        return this.defaits.size();
    }

    public void empileDefait(Coup c) {
        this.defaits.add(c);
    }

    public boolean peutDefaire() {
        return this.faits.size() > 0;
    }

    public boolean peutRefaire() {
        return this.defaits.size() > 0;
    }

    public void fait(Coup c) {
        this.faits.add(c);
        this.defaits.clear();
    }

    public Coup defait() {
        if (peutDefaire()) {
            Coup c = faits.remove(faits.size() - 1);
            empileDefait(c);
            return c;
        }
        return null;
    }

    public Coup refait() {
        if (peutRefaire()) {
            Coup c = this.defaits.remove(this.defaits.size() - 1);
            this.faits.add(c);
            return c;
        }
        return null;
    }

    public void raz() {
        this.faits.clear();
        this.defaits.clear();
    }

    public String pourSauvegarde() {
        boolean added = false;
        String s = "{";
        for (Coup c : getFaits()) {
            s += c.pourSauvegarde();
            s += " ";
            added = true;
        }
        if (added)
            s = s.substring(0, s.length() - 1);
        s += "}\n{";
        added = false;
        for (Coup c : getDefaits()) {
            s += c.pourSauvegarde();
            s += " ";
            added = true;
        }
        if (added)
            s = s.substring(0, s.length() - 1);
        s += "}";
        return s;
    }

    public String pourAffichage(boolean IA) {
        String s = "<html>";
        ArrayList<Coup> faits = getFaits();
        for (int i = faits.size() - 1; i >= 0; i--) {
            Coup c = faits.get(i);
            Joueur joueur = c.getJoueur();
            Point position = c.getPosition();
            if (IA) {
                if (joueur.getNum() == 1) {
                    s += "Vous" + " : (";
                } else {
                    s += "IA" + " : (";
                }
            } else {
                s += "J" + joueur.getNum() + " : (";
            }
            s += (int) (position.getX() + 1) + ", " + (int) (position.getY() + 1 )+ ")<br>";
        }
        s += "</html>";
        return s;
    }

    @Override
    public String toString() {
        String s = "faits{\n";
        for (Coup c : this.faits) {
            s += "\t" + c.toString() + "\n";
        }
        s += "}, defaits{\n";
        for (Coup c : this.defaits) {
            s += "\t" + c.toString() + "\n";
        }
        s += "}\n";
        return s;
    }
}
