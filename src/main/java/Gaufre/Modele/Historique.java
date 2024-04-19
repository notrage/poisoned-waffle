package Gaufre.Modele;
import java.util.ArrayList;

public class Historique {
    private ArrayList<Coup> faits ;
    private ArrayList<Coup> defaits;

    // Constructeur
    public Historique() {
        this.faits = new ArrayList<Coup>();
        this.defaits = new ArrayList<Coup>();
    }

    //Autres méthodes
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
        String s = "{";
        for (Coup c: faits) {
            s += c.pourSauvegarde();
            s += "\n";
        }
        s = s.substring(0, s.length()-1);
        s += "}\n\n{";
        for (Coup c: defaits) {
            s += c.pourSauvegarde();
            s += "\n";
        }
        s = s.substring(0, s.length()-1);
        s += "}";
        return s;
    }

    @Override
    public String toString() {
        String s = "faits{ ";
        for (Coup c : this.faits) {
            s += c.toString() + "\n";
        }
        s += "defaits= ";
        for (Coup c : this.defaits) {
            s += c.toString() + "\n";
        }
        return s;
    }
}