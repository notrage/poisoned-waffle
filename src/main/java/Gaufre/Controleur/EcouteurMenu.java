package Gaufre.Controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Gaufre.Vue.InterfaceGraphique;

public class EcouteurMenu implements ActionListener {
    private InterfaceGraphique vue;

    public EcouteurMenu(InterfaceGraphique vue) {
        this.vue = vue;
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        switch (evt.getActionCommand()) {
            case "Jeu1J":
                vue.setEtat(vue.JEU);
                break;
            case "Quitter":
                System.exit(0);
                break;
            default:
                throw new UnsupportedOperationException("Bouton du menu non supporté");
        }
    }

}
