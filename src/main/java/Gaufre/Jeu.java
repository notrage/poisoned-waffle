package Gaufre;

import Gaufre.Modele.Gaufre;
import Gaufre.Vue.InterfaceGraphique;

public class Jeu {
    Gaufre model;

    Jeu() {
        model = new Gaufre(5, 5);
        InterfaceGraphique vue = InterfaceGraphique.demarrer(model);
    }

    public static void main(String[] args) {
        new Jeu();
    }
}
