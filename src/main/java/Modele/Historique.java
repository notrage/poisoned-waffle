package Modele;
import java.util.ArrayList;

public class Historique {
    private ArrayList<Coup> faits ;
    private ArrayList<Coup> defaits;


    public Historique() {
        this.faits = new ArrayList<Coup>();
        this.defaits = new ArrayList<Coup>();
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
            this.defaits.add(c);
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

    @Override
    public String toString() {
        String s = "Historique{\n faits= ";
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