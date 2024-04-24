package Gaufre.Vue;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;

import javax.swing.*;
import javax.imageio.ImageIO;

import java.io.FileNotFoundException;
import java.io.InputStream;

import Gaufre.Modele.Gaufre;
import Gaufre.Controleur.EcouteurJeu;
import Gaufre.Controleur.EcouteurMenu;
import Gaufre.Controleur.EcouteurSouris;
import Gaufre.Configuration.ResourceLoader;

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
    private GraphicsEnvironment ge;
    private Container plateau; // CONTIENT LE PLATEAU OU SE TROUVE LA GAUFRE

    InterfaceGraphique(ModeGraphique mg) {
        etat = MENU;
        modele = mg;
        try {
            ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            Font titleFont = Font.createFont(Font.TRUETYPE_FONT,
                    ResourceLoader.getResourceAsStream("fonts/DEADLY_POISON_II.ttf"));
            ge.registerFont(titleFont);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
        
        gaufreNO = lireImage("gaufreNO");
        gaufreN = lireImage("gaufreN");
        gaufreNE = lireImage("gaufreNE");
        gaufreO = lireImage("gaufreO");
        gaufreMilieu = lireImage("gaufreMilieu");
        gaufreE = lireImage("gaufreE");
        gaufreSO = lireImage("gaufreSO");
        gaufreS = lireImage("gaufreS");
        gaufreSE = lireImage("gaufreSE");
        poison = lireImage("gaufrePoison");
        miettes1 = lireImage("miettes1");
        miettes2 = lireImage("miettes2");
        miettes3 = lireImage("miettes3");
        miettes4 = lireImage("miettes4");
        iconeGaufre = lireImage("iconeGaufre");
    }

    private Image lireImage(String nom) {
        String imgPath = "images/" + nom + ".png";
        InputStream in;
        try {
            in = ResourceLoader.getResourceAsStream(imgPath);
            return ImageIO.read(in);
        } catch (FileNotFoundException e) {
            System.err.println("Erreur: fichier " + imgPath + " introuvable");
        } catch (NullPointerException e) {
            System.err.println("Erreur: ressource " + imgPath + " introuvable");
        } catch (Exception e) {
            System.err.println("ERREUR: impossible de charger l'image " + nom);
        }
        // If loading failed
        return null;
    }

    public static InterfaceGraphique demarrer(ModeGraphique m) {
        System.setProperty("sun.java2d.noddraw", Boolean.TRUE.toString());
        InterfaceGraphique vue = new InterfaceGraphique(m);
        // Get graphical environment
        // JFrame.setDefaultLookAndFeelDecorated(true); // Permet de mieux resize mais
        // déplacer la fenêtre devient buggé
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
                fenetre.getContentPane().removeAll();
                panel = creerMenu();
                break;

            case JEU:
                fenetre.getContentPane().removeAll();
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

        // Main container using BorderLayout
        JPanel pane = new JPanel(new BorderLayout());
        pane.setMinimumSize(new Dimension(1000, 800));
        pane.setBackground(new Color(255, 219, 77));
        pane.setOpaque(true);

        // Title section
        JLabel title = new JLabel("GAUFRE", SwingConstants.CENTER);
        final JLabel finalTitle = title;
        title.setForeground(new Color(0, 128, 0));
        title.setFont(new Font("DEADLY POISON II", Font.BOLD, 100));
        fenetre.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Calculate font size based on window width
                int newFontSize = Math.max(50, fenetre.getHeight() / 5);
                finalTitle.setFont(new Font("DEADLY POISON II", Font.BOLD, newFontSize));
            }
        });

        // Add title to the top of the BorderLayout
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);
        titlePanel.add(Box.createVerticalGlue());
        titlePanel.add(title);
        titlePanel.add(Box.createVerticalGlue());
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add titlePanel to the top of the BorderLayout
        pane.add(titlePanel, BorderLayout.NORTH);

        // Middle section with vertically stacked buttons
        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.Y_AXIS));
        middlePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        middlePanel.setOpaque(false);

        JButton button1J = new JButton("1 joueur");
        button1J.setActionCommand("Jeu1J");
        button1J.addActionListener(ecouteurMenu);
        button1J.setAlignmentX(Component.CENTER_ALIGNMENT);
        middlePanel.add(Box.createVerticalGlue());
        middlePanel.add(button1J);

        JButton button2J = new JButton("2 joueurs");
        button2J.setActionCommand("Jeu2J");
        button2J.addActionListener(ecouteurMenu);
        button2J.setAlignmentX(Component.CENTER_ALIGNMENT);
        middlePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        middlePanel.add(button2J);
        middlePanel.add(Box.createVerticalGlue());

        pane.add(middlePanel, BorderLayout.CENTER);

        // Bottom section with horizontally stacked buttons and text
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.LINE_AXIS));
        bottomPanel.setOpaque(false);

        JPanel bottomLeft = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomLeft.setOpaque(false);
        JButton volumeButton = new JButton("VOLUME");
        bottomLeft.add(volumeButton);
        bottomPanel.add(bottomLeft);

        JPanel bottomRight = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomRight.setOpaque(false);
        JLabel versionLabel = new JLabel(getClass().getPackage().getImplementationVersion());
        bottomRight.add(versionLabel);
        bottomPanel.add(bottomRight);

        pane.add(bottomPanel, BorderLayout.PAGE_END);

        return pane;
    }

    private Container creerJeu() {
        Container pane = new Container();

        pane.setLayout(new BorderLayout());

        // Titre
        // texte = new JLabel("GAUFRE")
        // pane.add(texte, BorderLayout.NORTH);
        pane.add(creerInfo(), BorderLayout.EAST);
        this.plateau = new Container();
        this.plateau.setLayout(new GridLayout(modele.getGaufre().getNbLignes(), modele.getGaufre().getNbColonnes()));
        EcouteurSouris ecouteurSouris = new EcouteurSouris(this);
        plateau.addMouseListener(ecouteurSouris);
        pane.add(plateau, BorderLayout.CENTER);

       
        modele.reset();
        afficherGaufre();

        return pane;
    }
    
    private Container creerInfo() {
        Container pane = new Container();
        ////
        Container textes = new Container();
        textes.setLayout(new BoxLayout(textes, BoxLayout.Y_AXIS));
        JLabel texte0 = new JLabel();
        JLabel texte1 = new JLabel();
        JLabel texte2 = new JLabel();
        JLabel texte3 = new JLabel();
        JLabel texte4 = new JLabel();

        
        texte0.setText("");
        texte1.setText("Tour : Joueur " + modele.getGaufre().getJoueurCourant().getNum());
        texte2.setText("Scores :");
        texte3.setText("Joueur 1 :" + modele.getGaufre().getJoueur1().getScore());
        texte4.setText("Joueur 2 :" + modele.getGaufre().getJoueur2().getScore());

        textes.add(texte1);
        textes.add(texte0);
        textes.add(texte2);
        textes.add(texte3);
        textes.add(texte4);
        ////
        Container boutons = new Container();
        boutons.setLayout(new GridLayout(2,2));

        JButton annuler = new JButton("Annuler");
        annuler.setActionCommand("Annuler");
        annuler.addActionListener(new EcouteurJeu(this));
        JButton refaire = new JButton("Refaire");
        refaire.setActionCommand("Refaire");
        refaire.addActionListener(new EcouteurJeu(this));
        JButton reset = new JButton("Reset");
        reset.setActionCommand("Reset");
        reset.addActionListener(new EcouteurJeu(this));
        JButton quitter = new JButton("Quitter");
        quitter.setActionCommand("Quitter");
        quitter.addActionListener(new EcouteurJeu(this));

        boutons.add(annuler);
        boutons.add(refaire);
        boutons.add(quitter);
        boutons.add(reset);
        
        
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        pane.add(textes);
        pane.add(boutons);

        return pane;
    }

    public void majInfo() {
        Container info = (Container) ((Container) fenetre.getContentPane()).getComponent(0);
        Container textes = (Container) info.getComponent(0);
        Container boutons = (Container) info.getComponent(1);
  
        JLabel texte1 = (JLabel) textes.getComponent(0);
        JLabel texte3 = (JLabel) textes.getComponent(3);
        JLabel texte4 = (JLabel) textes.getComponent(4);

        texte1.setText("Tour : Joueur " + modele.getGaufre().getJoueurCourant().getNum());
        texte3.setText("Joueur 1 :" + modele.getGaufre().getJoueur1().getScore());
        texte4.setText("Joueur 2 :" + modele.getGaufre().getJoueur2().getScore());

        
        JButton annuler = (JButton) boutons.getComponent(0);
        JButton refaire = (JButton) boutons.getComponent(1);
        if (modele.peutAnnuler()) {
            annuler.setEnabled(true);
        }
        else {
            annuler.setEnabled(false);
        }
        if (modele.peutRefaire()) {
            refaire.setEnabled(true);
        }
        else {
            refaire.setEnabled(false);
        }
    }

    public void afficherGaufre() {
        //majInfo();
        plateau.removeAll();

        int lignes = modele.getGaufre().getNbLignes();
        int colonnes = modele.getGaufre().getNbColonnes();

        for (int i = 0; i < lignes; i++) {
            for (int j = 0; j < colonnes; j++) {    
                if (modele.getGaufre().getCase(i,j)){
                    if (i==0 && j==0)
                        plateau.add(new ajoutGaufre(poison));
                    else
                        plateau.add(new ajoutGaufre(gaufreMilieu));
                }
                else
                    plateau.add(new ajoutGaufre(miettes1));
            }
        }

        fenetre.revalidate();
        fenetre.repaint();
        return;
    }


    private class ajoutGaufre extends JPanel {
        Image img;

        public ajoutGaufre(Image img) {
            this.img = img;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (img != null) {
                g.drawImage(img, 0, 0, getTailleCelluleX(), getTailleCelluleY(), null);
            }
        }
    }

    public void finPartie() {
        // BorderLayout layout = (BorderLayout) fenetre.getContentPane().getLayout();
        // Component boutons = layout.getLayoutComponent(BorderLayout.SOUTH);

        int gagnant = getMG().getGaufre().getJoueurCourant().getNum();
        int nbCoupsJoues = getMG().getGaufre().getHistorique().getNbFaits();
        
        System.out.println("Joueur " + gagnant + " a gagné !");
        System.out.println("La partie a duré " + nbCoupsJoues + " coups.");
        
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

    public Container getPlateau() {
        return plateau;
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
