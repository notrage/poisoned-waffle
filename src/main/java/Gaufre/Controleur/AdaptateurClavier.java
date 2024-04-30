package Gaufre.Controleur;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class AdaptateurClavier extends KeyAdapter {

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            default:
                System.err.println("Touche " + e.getKeyChar() + " non supportée");
        }
    }
}
