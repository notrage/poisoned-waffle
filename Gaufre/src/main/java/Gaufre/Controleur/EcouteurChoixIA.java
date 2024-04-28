package Gaufre.Controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Gaufre.Vue.InterfaceGraphique;

import Gaufre.Configuration.Config;


public class EcouteurChoixIA implements ActionListener {
    private InterfaceGraphique vue;

    public EcouteurChoixIA(InterfaceGraphique vue) {
        this.vue = vue;
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        Config.debug("Received action : ", evt.getActionCommand());
        switch (evt.getActionCommand()) {
            case "EasyDifficulty":
                vue.setTypeIA(vue.ALEA);
                vue.setEtat(vue.JEU);
                break;
            case "MediumDifficulty":
                vue.setTypeIA(vue.GAGNANT);
                vue.setEtat(vue.JEU);
                break;
            case "HardDifficulty":
                vue.setTypeIA(vue.EXPLO);
                vue.setEtat(vue.JEU);
                break;
            default:
                throw new UnsupportedOperationException("Bouton du choix de l'IA non supporté");
        }
    }

}
