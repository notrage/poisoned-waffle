package Gaufre.Vue;

import java.awt.*;
import javax.swing.*;

import Gaufre.Modele.Gaufre;

public class InterfaceGraphique implements Runnable {
    private final static int MENU = 0;
    private final static int JEU = 1;
    Gaufre modele;
    int etat;
    JFrame fenetre;

    InterfaceGraphique(Gaufre m) {
        etat = MENU;
        modele = m;
    }

    public static void demarrer() {
        InterfaceGraphique vue = new InterfaceGraphique(null);
        SwingUtilities.invokeLater(vue);
    }

    public void run() {
        fenetre = new JFrame("Gauffre");
        fenetre.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        fenetre.setLocationRelativeTo(null);
        metAJourFenetre();
    }

    private void metAJourFenetre() {
        switch (etat) {
            case MENU:
                creerMenu(fenetre.getContentPane());
                break;

            case JEU:

                break;

            default:
                break;
        }
    }

    private void creerMenu(Container pane) {
        JButton bouton;
        JLabel texte;
        GridBagConstraints c = new GridBagConstraints();

        // Titre
        texte = new JLabel("GAUFRE");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 40;
        c.gridwidth = 4;
        c.weightx = 0.5;
        c.gridy = 0;
        c.gridx = 0;
        pane.add(texte, c);

        // 1 joueur (vs ordinateur)
        bouton = new JButton("1 joueur");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 5;
        c.gridwidth = 4;
        c.weightx = 0.5;
        c.gridy = 1;
        c.gridx = 0;
        pane.add(bouton, c);

        // 2 joueurs (vs humain)
        bouton = new JButton("2 joueurs");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 5;
        c.gridwidth = 4;
        c.weightx = 0.5;
        c.gridy = 2;
        c.gridx = 0;
        pane.add(bouton, c);

        // Quitter
        bouton = new JButton("Quitter");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 20;
        c.gridwidth = 4;
        c.weightx = 0.5;
        c.gridy = 3;
        c.gridx = 0;
        pane.add(bouton, c);

        // Volume
        bouton = new JButton("5");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 0; // reset to default
        c.weighty = 1.0; // request any extra vertical space
        c.anchor = GridBagConstraints.PAGE_END; // bottom of space
        c.insets = new Insets(10, 0, 0, 0); // top padding
        c.gridx = 0; // aligned with button 2
        c.gridy = 4; // third row
        pane.add(bouton, c);
    }

}
