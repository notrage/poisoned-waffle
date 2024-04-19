package Modele;

class Joueur {
    private int num;
    private int score;

    Joueur(int num) {
        this.num = num;
        this.score = 0;
    }

    public int getNum() {
        return this.num;
    }

    public int getScore() {
        return this.score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String toString() {
        return "Joueur " + this.num + " score : " + this.score;
    }
    
}
