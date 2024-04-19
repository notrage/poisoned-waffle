package Gaufre.Controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EcouteurJeu implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent evt) {
        switch (evt.getActionCommand()) {
            default:
                throw new UnsupportedOperationException("Bouton du jeu non supporté");
        }
    }

}
