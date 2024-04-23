package Gaufre;

import Gaufre.Modele.Gaufre;
import Gaufre.Vue.InterfaceGraphique;
import Gaufre.Vue.ModeGraphique;

public class Jeu {

    Jeu() {
        ModeGraphique mode = new ModeGraphique(new Gaufre(5, 5));
        InterfaceGraphique vue = InterfaceGraphique.demarrer(mode);
    }

    public static void main(String[] args) {
        new Jeu();
    }
}
