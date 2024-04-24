package Gaufre.Controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Gaufre.Vue.InterfaceGraphique;
import Gaufre.Vue.ModeGraphique;

public class EcouteurJeu implements ActionListener {
    private InterfaceGraphique ig;
    private ModeGraphique mg;

    public EcouteurJeu(InterfaceGraphique ig) {
        this.ig = ig;
        this.mg = ig.getMG();
    }
    @Override
    public void actionPerformed(ActionEvent evt) {
        switch (evt.getActionCommand()) {
            case "Annuler":
                mg.annuler();
                ig.afficherGaufre();
                break;
            case "Refaire":
                mg.refaire();
                ig.afficherGaufre();
                break;
            case "Reset":
                mg.reset();
                ig.afficherGaufre();
                break;
            case "Quitter":
                System.exit(0);
                break;
            default:
                throw new UnsupportedOperationException("Bouton du jeu non supporté");
        }
    }

}
