package Gaufre.Controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Gaufre.Vue.InterfaceGraphique;

import Gaufre.Configuration.Config;
import Gaufre.Modele.IAaleatoire;

public class EcouteurMenu implements ActionListener {
    private InterfaceGraphique vue;

    public EcouteurMenu(InterfaceGraphique vue) {
        this.vue = vue;
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        Config.debug("Received action : ", evt.getActionCommand());
        switch (evt.getActionCommand()) {
            case "Jeu1J":
            vue.getMG().setNbJoueurs(1);
            vue.getMG().setIA(new IAaleatoire());
            vue.setEtat(vue.JEU);
                break;
            case "Jeu2J":
                vue.setEtat(vue.JEU);
                vue.getMG().setNbJoueurs(2);
                break;
            case "volume":
                Config.toggleSon();
                vue.toggleSon();
                break;
            case "Quitter":
                System.exit(0);
                break;
            default:
                throw new UnsupportedOperationException("Bouton du menu non supporté");
        }
    }

}