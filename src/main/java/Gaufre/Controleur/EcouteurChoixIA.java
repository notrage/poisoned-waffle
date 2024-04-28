package Gaufre.Controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Gaufre.Vue.InterfaceGraphique;

import Gaufre.Configuration.Config;
import Gaufre.Modele.IAaleatoire;
import Gaufre.Modele.IAcoupGagnant;
import Gaufre.Modele.IAexploration;

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
                vue.getMG().setIA(new IAaleatoire());
                vue.setEtat(vue.JEU);
                break;
            case "MediumDifficulty":
                vue.getMG().setIA(new IAcoupGagnant());
                vue.setEtat(vue.JEU);
                break;
            case "HardDifficulty":
                vue.getMG().setIA(new IAexploration());
                vue.setEtat(vue.JEU);
                break;
            default:
                throw new UnsupportedOperationException("Bouton du choix de l'IA non supporté");
        }
    }

}
