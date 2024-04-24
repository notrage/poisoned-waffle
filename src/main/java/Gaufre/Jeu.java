package Gaufre;

import Gaufre.Modele.Gaufre;
import Gaufre.Vue.InterfaceGraphique;
import Gaufre.Vue.Musique;
import Gaufre.Vue.ModeGraphique;
import Gaufre.Configuration.Config;

public class Jeu {

    Jeu() {
        ModeGraphique mode = new ModeGraphique(new Gaufre(5, 5));
        InterfaceGraphique.demarrer(mode);
    }

    public static void main(String[] args) {
        new Config();
        new Jeu();
    }
}
