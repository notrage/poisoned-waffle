package Gaufre.Vue;
import Gaufre.Modele.Gaufre;
import java.util.Scanner;

public  class ModeTextuel {
    Gaufre gaufre;
    Scanner sc;
    public ModeTextuel(Gaufre g){
        gaufre = g;
        sc = new Scanner(System.in);
    }

    public void afficher(){
        System.out.println(gaufre.toString());
    }

    public static void main(String[] args) {
        Scanner  sc = new Scanner(System.in);
        System.out.println("Entrez la taille de la gaufre ");

    }


}