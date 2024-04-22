package Gaufre.Modele;

public class Joueur {
    private int num;
    private int score;

    // Constructeur
    public Joueur(int num) {
        setNum(num);
        setScore(0);
    }

    public Joueur(String sauvegarde) {
        String parties[] = sauvegarde.substring(1, sauvegarde.length() - 1).split(",");
        setNum(Integer.parseInt(parties[0]));
        setScore(Integer.parseInt(parties[1]));
    }

    // Getters
    public int getNum() {
        return this.num;
    }

    public int getScore() {
        return this.score;
    }

    // Setters
    public void setScore(int score) {
        this.score = score;
    }

    public void setNum(int num) {
        this.num = num;
    }

    // Autres méthodes

    public void incrementScore() {
        this.score++;
    }

    @Override
    public String toString() {
        return "Joueur " + this.num + " score : " + this.score;
    }

    public Joueur clone() {
        Joueur j = new Joueur(this.getNum());
        j.setScore(this.getScore());
        return j;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Joueur that = (Joueur) o;
        return that.getNum() == getNum();
    }

    @Override 
    public int hashCode(){
        return getNum();
    }

    public String pourSauvegarde() {
        return "{" + getNum() + "," + getNum() + "}";
    }
}
