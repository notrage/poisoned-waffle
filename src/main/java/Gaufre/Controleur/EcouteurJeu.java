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
                ig.majInfo();
                break;
            case "Refaire":
                Config.debug("Click bouton refaire");
                mg.refaire();
                ig.syncGaufre();
                ig.majInfo();
                break;
            case "Reset":
                Config.debug("Click bouton reset");
                mg.reset();
                ig.setEtat(ig.JEU);
                break;
            case "QuitterJeu":
                Config.debug("Click bouton quitter jeu");
                ig.getMG().reset();
                ig.setEtat(ig.MENU);
                break;
            case "Plus":
                Config.debug("Click bouton plus");
                mg.plus();
                ig.setEtat(ig.JEU);
                break;
            case "Moins":
                Config.debug("Click bouton moins");
                mg.moins();
                ig.setEtat(ig.JEU);
                break;
            case "Sauvegarder":
                Config.debug("Click bouton sauvegarder");
                mg.sauvegarder();
                break;
            case "Charger":
                Config.debug("Click bouton charger");
                if (mg.charger()){
                    ig.setEtat(ig.JEU);
                }
                break;

            default:
                throw new UnsupportedOperationException("Bouton du jeu non supporté");
        }
    }

}
