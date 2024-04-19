package Gaufre.Vue;

import java.awt.*;
import java.awt.event.WindowEvent;

import javax.swing.*;

import Gaufre.Modele.Gaufre;
import Gaufre.Controleur.AdaptateurClavier;
import Gaufre.Controleur.EcouteurJeu;
import Gaufre.Controleur.EcouteurMenu;

public class InterfaceGraphique implements Runnable {
    public final int MENU = 0;
    public final int JEU = 1;
    public final int QUIT = -1;
    private Gaufre modele;
    private EcouteurMenu ecouteurMenu = new EcouteurMenu(this);
    private int etat;
    private JFrame fenetre;

    InterfaceGraphique(Gaufre m) {
        etat = MENU;
        modele = m;
    }

    public static InterfaceGraphique demarrer(Gaufre m) {
        InterfaceGraphique vue = new InterfaceGraphique(m);
        SwingUtilities.invokeLater(vue);
        return vue;
    }

    public void run() {
        fenetre = new JFrame("Gauffre");
        fenetre.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        fenetre.setLayout(new GridLayout());
        fenetre.setLocationRelativeTo(null);
        metAJourFenetre();
        fenetre.pack();
        fenetre.setVisible(true);
    }

    private void metAJourFenetre() {
        Container panel;
        switch (etat) {
            case MENU:
                panel = creerMenu();
                break;

            case JEU:
                panel = creerJeu();
                break;

            case QUIT:
                fenetre.dispatchEvent(new WindowEvent(fenetre, WindowEvent.WINDOW_CLOSING));

            default:
                throw new UnsupportedOperationException("Etat de jeu " + etat + " non supporté");
        }
        fenetre.setContentPane(panel);
        fenetre.revalidate();
        fenetre.repaint();
    }

    private Container creerMenu() {
        Container pane = new Container();
        JButton bouton;
        JLabel texte;
        GridBagConstraints c;

        pane.setLayout(new GridBagLayout());
        pane.setMinimumSize(new Dimension(1000, 800));

        // Titre
        texte = new JLabel("GAUFRE");
        c = new GridBagConstraints();
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.ipady = c.fill = GridBagConstraints.BOTH; // Allow component to grow in both directions
        c.gridy = 0;
        c.gridx = 3;
        pane.add(texte, c);

        // 1 joueur (vs ordinateur)
        bouton = new JButton("1 joueur");
        c = new GridBagConstraints();
        c.weightx = 1.0;
        c.weighty = 0.1;
        c.fill = GridBagConstraints.BOTH; // Allow component to grow in both directions
        c.gridy = 1;
        c.gridx = 3;
        bouton.setActionCommand("Jeu1J");
        bouton.addActionListener(ecouteurMenu);
        pane.add(bouton, c);

        // 2 joueurs (vs humain)
        bouton = new JButton("2 joueurs");
        c = new GridBagConstraints();
        c.gridwidth = 4;
        c.weightx = 1.0;
        c.weighty = 0.1;
        c.fill = GridBagConstraints.BOTH; // Allow component to grow in both directions
        c.gridy = 2;
        c.gridx = 0;
        pane.add(bouton, c);

        // Quitter
        bouton = new JButton("Quitter");
        c = new GridBagConstraints();
        c.gridwidth = 4;
        c.weightx = 1.0;
        c.weighty = 0.1;
        c.fill = GridBagConstraints.BOTH; // Allow component to grow in both directions
        c.gridy = 3;
        c.gridx = 0;
        bouton.addActionListener(ecouteurMenu);
        pane.add(bouton, c);

        // Volume
        bouton = new JButton("VOLUME");
        c = new GridBagConstraints();
        c.weighty = 1.0;
        c.anchor = GridBagConstraints.PAGE_END;
        c.insets = new Insets(10, 0, 0, 0);
        c.gridy = 4;
        c.gridx = 0;
        pane.add(bouton, c);

        // Version
        texte = new JLabel(getClass().getPackage().getImplementationVersion());
        c = new GridBagConstraints();
        c.weighty = 1.0;
        c.anchor = GridBagConstraints.LAST_LINE_END;
        c.gridwidth = 4;
        c.gridy = 4;
        c.gridx = 3;
        pane.add(texte, c);

        return pane;
    }

    private Container creerJeu() {
        Container pane = new Container();
        JLabel texte;
        GridBagConstraints c = new GridBagConstraints();

        pane.setLayout(new GridBagLayout());

        // Titre
        texte = new JLabel("GAUFRE");
        c.ipady = 40;
        c.gridwidth = 4;
        c.weightx = 0.5;
        c.gridy = 0;
        c.gridx = 0;
        pane.add(texte, c);

        return pane;
    }

    public void setEtat(int newEtat) {
        etat = newEtat;
        metAJourFenetre();
    }
}
