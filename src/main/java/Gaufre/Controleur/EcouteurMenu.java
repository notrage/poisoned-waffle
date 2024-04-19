package Gaufre.Controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EcouteurMenu implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent evt) {
        switch (evt.getActionCommand()) {
            default:
                throw new UnsupportedOperationException("Bouton du menu non supporté");
        }
    }

}
