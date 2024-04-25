package Gaufre.Controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Gaufre.Configuration.Config;
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
                Config.debug("Click bouton annuler");
                mg.annuler();
                ig.syncGaufre();
                break;
            case "Refaire":
                Config.debug("Click bouton refaire");
                mg.refaire();
                ig.syncGaufre();
                break;
            case "Reset":
                Config.debug("Click bouton reset");
                mg.reset();
                ig.syncGaufre();
                break;
            case "Quitter":
                System.exit(0);
                break;
            default:
                throw new UnsupportedOperationException("Bouton du jeu non supporté");
        }
    }

}
