package Gaufre;

import Gaufre.Modele.Gaufre;
import Gaufre.Vue.InterfaceGraphique;
import Gaufre.Vue.ModeGraphique;
import Gaufre.Configuration.Config;

public class Jeu {

    Jeu() {
        ModeGraphique mode = new ModeGraphique(new Gaufre(6, 5));
        InterfaceGraphique.demarrer(mode);
    }

    public static void main(String[] args) {
        new Config();
        new Jeu();
    }
}
