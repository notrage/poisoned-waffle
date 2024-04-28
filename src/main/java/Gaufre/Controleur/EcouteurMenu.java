package Gaufre.Controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Gaufre.Vue.InterfaceGraphique;

import Gaufre.Configuration.Config;

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
                vue.setEtat(vue.CHOIX_IA);
                break;
            case "Jeu2J":
                vue.getMG().setNbJoueurs(2);
                vue.setEtat(vue.JEU);
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
