package Gaufre.Vue;

import java.awt.*;
import java.awt.event.WindowEvent;

import javax.swing.*;
import javax.imageio.ImageIO;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import Gaufre.Modele.Gaufre;
import Gaufre.Controleur.EcouteurMenu;

public class InterfaceGraphique implements Runnable {
    public final int MENU = 0;
    public final int JEU = 1;
    public final int QUIT = -1;
    private Image gaufreNE, gaufreSE, gaufreNO, gaufreSO, gaufreN, gaufreS, gaufreE, gaufreO, 
                  gaufreMilieu, poison, miettes1, miettes2, miettes3, miettes4, iconeGaufre;
    private ModeGraphique modele;
    private EcouteurMenu ecouteurMenu = new EcouteurMenu(this);
    private int etat;
    private JFrame fenetre;
    private Container plateau; // CONTIENT LE PLATEAU OU SE TROUVE LA GAUFRE

    InterfaceGraphique(ModeGraphique mg) {
        etat = MENU;
        modele = mg;
        gaufreNO = lireImage("gaufreNO");
        gaufreN = lireImage("gaufreN");
        gaufreNE = lireImage("gaufreNE");
        gaufreO = lireImage("gaufreO");
        gaufreMilieu = lireImage("gaufreMilieu");
        gaufreE = lireImage("gaufreE");
        gaufreSO = lireImage("gaufreSO");
        gaufreS = lireImage("gaufreS");
        gaufreSE = lireImage("gaufreSE");
        poison = lireImage("poison");
        miettes1 = lireImage("miettes1");
        miettes2 = lireImage("miettes2");
        miettes3 = lireImage("miettes3");
        miettes4 = lireImage("miettes4");
        iconeGaufre = lireImage("iconeGaufre");
   }

   private Image lireImage(String nom) {
       InputStream in = null;
       try {
       	in = new FileInputStream("src/main/resources/images/" + nom + ".png");
       } catch (FileNotFoundException e) {
       	System.err.println("ERREUR: impossible de trouver l'image " + nom + ".png");
           return null;
       }
       try {
       	// Chargement d'une image utilisable dans Swing 
       	return ImageIO.read(in);
       } catch (Exception e) {
           System.err.println("ERREUR: impossible de charger l'image " + nom);
           return null;
       }
    
   }

    public static InterfaceGraphique demarrer(ModeGraphique m) {
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
        c.ipady = c.fill = GridBagConstraints.BOTH;
        c.gridy = 0;
        c.gridx = 3;
        pane.add(texte, c);

        // 1 joueur (vs ordinateur)
        bouton = new JButton("1 joueur");
        c = new GridBagConstraints();
        c.weightx = 1.0;
        c.weighty = 0.1;
        c.fill = GridBagConstraints.BOTH;
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
        c.fill = GridBagConstraints.BOTH;
        c.gridy = 2;
        c.gridx = 0;
        bouton.setActionCommand("Jeu2J");
        bouton.addActionListener(ecouteurMenu);
        pane.add(bouton, c);

        // Quitter
        bouton = new JButton("Quitter");
        c = new GridBagConstraints();
        c.gridwidth = 4;
        c.weightx = 1.0;
        c.weighty = 0.1;
        c.fill = GridBagConstraints.BOTH;
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
        this.plateau = new Container();
        this.plateau.setLayout(new GridLayout(modele.getGaufre().getNbLignes(), modele.getGaufre().getNbColonnes()));
        pane.add(plateau);
        modele.reset();
        afficherGaufre();

        return pane;
    }

    public void afficherGaufre() {
        
        //TODO
    }

    public void setEtat(int newEtat) {
        etat = newEtat;
        metAJourFenetre();
    }

    
    public int getEtat() {
        return etat;
    }

    public ModeGraphique getMG() {
        return modele;
    }

    public int getTaillePlateauX() {
        return plateau.getWidth();
    }
    
    public int getTaillePlateauY() {
        return plateau.getHeight();
    }

    public int getTailleCelluleX() {
        return plateau.getWidth() / modele.getGaufre().getNbColonnes();
    }

    public int getTailleCelluleY() {
        return plateau.getHeight() / modele.getGaufre().getNbLignes();
    }

    public Graphics2D getGraphics() {
        return (Graphics2D) plateau.getGraphics();
    }

    public static void main(String[] args) {
        Gaufre g = new Gaufre(10, 10);
        ModeGraphique mg = new ModeGraphique(g);
        InterfaceGraphique.demarrer(mg);
    }

}
