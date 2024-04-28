package Gaufre.Vue;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Random;

import javax.swing.*;

import Gaufre.Controleur.EcouteurJeu;
import Gaufre.Controleur.EcouteurMenu;
import Gaufre.Controleur.EcouteurChoixIA;
import Gaufre.Controleur.EcouteurSouris;

import Gaufre.Modele.Coup;
import Gaufre.Modele.Gaufre;
import Gaufre.Modele.IAaleatoire;
import Gaufre.Modele.IAcoupGagnant;
import Gaufre.Modele.IAexploration;

import Gaufre.Configuration.ResourceLoader;
import Gaufre.Configuration.Config;

public class InterfaceGraphique implements Runnable {
    public final int MENU = 0;
    public final int JEU = 1;
    public final int CHOIX_IA = 2;
    public final int QUIT = -1;
    public final int ALEA = 10;
    public final int GAGNANT = 11;
    public final int EXPLO = 12;

    private BufferedImage gaufreMilieu, poison, miettes1, miettes2, miettes3, miettes4;
    private ModeGraphique modele;
    private EcouteurMenu ecouteurMenu;
    private EcouteurChoixIA ecouteurChoixIA;
    private Musique bgMusique;
    private int etat;
    private int typeIA;
    private JFrame fenetre;
    private GraphicsEnvironment ge;
    private JPanel plateau;
    private cellGaufre[] gaufreCells;
    private JLabel affichageNorth;
    public boolean boolCoupInval;

    InterfaceGraphique(ModeGraphique mg) {
        etat = MENU;
        modele = mg;
        ecouteurMenu = new EcouteurMenu(this);
        ecouteurChoixIA = new EcouteurChoixIA(this);
        bgMusique = new Musique("sons/SpaceJazz.wav");
        if (!Config.estMuet()) {
            bgMusique.play();
        }
        try {
            ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            Font titleFont = Font.createFont(Font.TRUETYPE_FONT,
                    ResourceLoader.getResourceAsStream("fonts/DEADLY_POISON_II.ttf"));
            ge.registerFont(titleFont);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
        gaufreMilieu = ResourceLoader.lireImage("gaufreMilieu");
        poison = ResourceLoader.lireImage("gaufrePoison");
        miettes1 = ResourceLoader.lireImage("miettes1");
        miettes2 = ResourceLoader.lireImage("miettes2");
        miettes3 = ResourceLoader.lireImage("miettes3");
        miettes4 = ResourceLoader.lireImage("miettes4");
        boolCoupInval = false;
    }

    public static InterfaceGraphique demarrer(ModeGraphique m) {
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
            Config.debug("Set Look and Feel to system.");
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException
                | IllegalAccessException e) {
            System.err.println("Can't set look and feel : " + e);
        }
        System.setProperty("sun.java2d.noddraw", Boolean.TRUE.toString());
        InterfaceGraphique vue = new InterfaceGraphique(m);
        SwingUtilities.invokeLater(vue);
        return vue;
    }

    public void run() {
        fenetre = new JFrame("Gauffre");
        fenetre.setIconImage(gaufreMilieu);
        fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fenetre.setLocationRelativeTo(null);
        fenetre.setSize(new Dimension(800, 600));
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
                fenetre.setContentPane(panel);
                break;

            case JEU:
                fenetre.getContentPane().removeAll();
                panel = creerJeu();
                fenetre.setContentPane(panel);
                majInfo();
                break;

            case CHOIX_IA:
                fenetre.getContentPane().removeAll();
                panel = creerChoixIA();
                fenetre.setContentPane(panel);
                break;

            case QUIT:
                fenetre.dispatchEvent(new WindowEvent(fenetre, WindowEvent.WINDOW_CLOSING));

            default:
                throw new UnsupportedOperationException("Etat de jeu " + etat + " non supporté");
        }
        if (Config.showBorders()) {
            showAllBorders(panel);
        }
        fenetre.revalidate();
        fenetre.repaint();
    }

    private void showAllBorders(Container container) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof Container) {
                showAllBorders((Container) comp);
            }
            if (comp instanceof JPanel) {
                ((JPanel) comp).setBorder(BorderFactory.createLineBorder(Color.red));
            }
        }
    }

    private Container creerMenu() {
        int menuWidth = fenetre.getWidth();
        int menuHeight = fenetre.getHeight();
        Config.debug("Height :", menuHeight, "Width :", menuWidth);
        // Create the background panel
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(menuWidth, menuHeight));
        layeredPane.setOpaque(false);
        FondMenu fond = new FondMenu();
        fond.setBounds(0, 0, menuWidth, menuHeight);
        fond.setOpaque(true);
        layeredPane.add(fond, JLayeredPane.DEFAULT_LAYER);

        // Main container using BorderLayout
        JPanel pane = new JPanel(new BorderLayout());
        pane.setOpaque(false);
        pane.setBounds(0, 0, menuWidth, menuHeight);

        // Title section
        JLabel title = new JLabel("GAUFRE", SwingConstants.CENTER);
        title.setOpaque(false);
        title.setForeground(new Color(5, 158, 56));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add title to the top of the BorderLayout
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.add(Box.createVerticalGlue());
        titlePanel.add(title);
        titlePanel.add(Box.createVerticalGlue());
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add titlePanel to the top of the BorderLayout
        pane.add(titlePanel, BorderLayout.NORTH);

        // Middle section with vertically stacked buttons using GridBagLayout
        JPanel middlePanel = new JPanel(new GridBagLayout());
        middlePanel.setOpaque(false);
        int insetSize = menuWidth / 3;
        Insets insets = new Insets(20, insetSize, 20, insetSize);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.insets = insets;

        JButton button1J = new JButton("Contre IA");
        button1J.setBackground(new Color(255, 209, 102));
        button1J.setMnemonic(KeyEvent.VK_I);
        button1J.addActionListener(ecouteurMenu);
        button1J.setActionCommand("Jeu1J");
        button1J.setAlignmentX(Component.CENTER_ALIGNMENT);
        middlePanel.add(button1J, gbc);

        JButton button2J = new JButton("2 joueurs");
        button2J.setBackground(new Color(255, 209, 102));
        button2J.setMnemonic(KeyEvent.VK_J);
        button2J.addActionListener(ecouteurMenu);
        button2J.setActionCommand("Jeu2J");
        button2J.setAlignmentX(Component.CENTER_ALIGNMENT);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        middlePanel.add(button2J, gbc);

        pane.add(middlePanel, BorderLayout.CENTER);

        // Bottom section with horizontally stacked buttons and text
        JPanel bottomPanel = new JPanel();
        bottomPanel.setPreferredSize(new Dimension(menuWidth, (int) (menuHeight * 0.2)));
        bottomPanel.setOpaque(false);
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.LINE_AXIS));

        JPanel bottomLeft = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomLeft.setOpaque(false);
        BufferedImage volImg;
        if (Config.estMuet()) {
            volImg = ResourceLoader.lireImage("muet");
        } else {
            volImg = ResourceLoader.lireImage("volume");
        }
        JButton volumeButton = new JButton();
        setButtonIcons(volumeButton, volImg);
        volumeButton.addActionListener(ecouteurMenu);
        volumeButton.setName("volumeButton");
        volumeButton.setActionCommand("volume");
        volumeButton.setBorderPainted(false);
        volumeButton.setFocusPainted(false);
        volumeButton.setContentAreaFilled(false);
        bottomLeft.add(volumeButton);
        bottomPanel.add(bottomLeft);

        JPanel bottomRight = new JPanel();
        bottomRight.setLayout(new BoxLayout(bottomRight, BoxLayout.Y_AXIS));
        bottomRight.setOpaque(false);
        JLabel versionLabel = new JLabel(getClass().getPackage().getImplementationVersion());
        versionLabel.setForeground(new Color(34, 84, 124));
        bottomRight.add(Box.createVerticalGlue());
        bottomRight.add(versionLabel);
        bottomPanel.add(bottomRight);

        pane.add(bottomPanel, BorderLayout.PAGE_END);

        layeredPane.add(pane, JLayeredPane.PALETTE_LAYER);
        int newTitleFontSize = Math.max(50, fenetre.getHeight() / 4);
        title.setFont(new Font("DEADLY POISON II", Font.BOLD, newTitleFontSize));
        versionLabel.setFont(new Font("Arial", Font.PLAIN, (int) (newTitleFontSize / 6)));
        int buttonFontSize = (int) (newTitleFontSize * 0.2); // Adjust the multiplier as needed
        Font buttonFont = new Font("Arial", Font.PLAIN, buttonFontSize);
        button1J.setFont(buttonFont);
        button2J.setFont(buttonFont);
        volumeButton.setFont(buttonFont);

        fenetre.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Calculate font size based on window width
                int newTitleFontSize = Math.max(50, fenetre.getHeight() / 4);
                title.setFont(new Font("DEADLY POISON II", Font.BOLD, newTitleFontSize));
                versionLabel.setFont(new Font("Arial", Font.PLAIN, (int) (newTitleFontSize / 6)));

                int buttonFontSize = (int) (newTitleFontSize * 0.2); // Adjust the multiplier as needed
                Font buttonFont = new Font("Arial", Font.PLAIN, buttonFontSize);
                button1J.setFont(buttonFont);
                button2J.setFont(buttonFont);
                volumeButton.setFont(buttonFont);

                layeredPane.setBounds(0, 0, fenetre.getWidth(), fenetre.getHeight());
                fond.setBounds(0, 0, fenetre.getWidth(), fenetre.getHeight());
                pane.setBounds(0, 0, fenetre.getWidth(), fenetre.getHeight());
                bottomPanel.setPreferredSize(new Dimension(fenetre.getWidth(), (int) (fenetre.getHeight() * 0.2)));

                // Change buttons sizes and margins
                int insetSize = fenetre.getWidth() / 3;
                Insets insets = new Insets(20, insetSize, 20, insetSize);
                gbc.insets = insets;
                middlePanel.removeAll();
                middlePanel.add(button1J, gbc);
                middlePanel.add(button2J, gbc);
                BufferedImage volImg;
                if (Config.estMuet()) {
                    volImg = ResourceLoader.lireImage("muet");
                } else {
                    volImg = ResourceLoader.lireImage("volume");
                }
                setButtonIcons(volumeButton, volImg);
            }
        });
        return layeredPane;
    }

    private void setButtonIcons(JButton b, BufferedImage img) {
        Image scaledImg = img.getScaledInstance(
                (int) (0.15 * fenetre.getWidth()),
                (int) (0.15 * fenetre.getHeight()),
                Image.SCALE_SMOOTH);
        // Create a new BufferedImage from the scaled image
        BufferedImage scaledBufferedImg = new BufferedImage(
                scaledImg.getWidth(null),
                scaledImg.getHeight(null),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = scaledBufferedImg.createGraphics();
        g2d.drawImage(scaledImg, 0, 0, null);
        g2d.dispose();

        RescaleOp op = new RescaleOp(0.7f, 0, null);
        BufferedImage darkerImage = op.filter(scaledBufferedImg, null);
        ImageIcon icon = new ImageIcon(scaledBufferedImg);
        ImageIcon darkIcon = new ImageIcon(darkerImage);
        b.setIcon(icon);
        b.setRolloverIcon(darkIcon);
    }

    public void toggleSon() {
        if (Config.estMuet()) {
            bgMusique.stop();
            BufferedImage volImg = ResourceLoader.lireImage("muet");
            setButtonIcons((JButton) getComponentByName(fenetre, "volumeButton"), volImg);
        } else {
            bgMusique.play();
            BufferedImage volImg = ResourceLoader.lireImage("volume");
            setButtonIcons((JButton) getComponentByName(fenetre, "volumeButton"), volImg);
        }
    }

    private Component getComponentByName(Component component, String name) {
        String compName = component.getName();
        if (compName != null && compName.equals(name)) {
            return component;
        }

        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                Component foundComponent = getComponentByName(child, name);
                if (foundComponent != null) {
                    return foundComponent;
                }
            }
        }

        return null; // Return null if no button is found
    }

    private Container creerChoixIA() {
        JPanel pane = new JPanel();
        pane.setLayout(new GridLayout(1, 3));

        // Creating three buttons - easy ; medium ; hard

        JButton easy = new JButton("Easy");
        easy.setBackground(new Color(255, 209, 102));
        easy.setMnemonic(KeyEvent.VK_E);
        easy.addActionListener(ecouteurChoixIA);
        easy.setActionCommand("EasyDifficulty");
        pane.add(easy, null);

        JButton medium = new JButton("Medium");
        medium.setBackground(new Color(255, 209, 102));
        medium.setMnemonic(KeyEvent.VK_M);
        medium.addActionListener(ecouteurChoixIA);
        medium.setActionCommand("MediumDifficulty");
        pane.add(medium, null);

        JButton hard = new JButton("Hard");
        hard.setBackground(new Color(255, 209, 102));
        hard.setMnemonic(KeyEvent.VK_H);
        hard.addActionListener(ecouteurChoixIA);
        hard.setActionCommand("HardDifficulty");
        pane.add(hard, null);

        return pane;
    }

    private Container creerJeu() {
        JPanel pane = new JPanel();
        int l = modele.getGaufre().getNbLignes(), c = modele.getGaufre().getNbColonnes();
        gaufreCells = new cellGaufre[l * c];

        pane.setLayout(new BorderLayout());
        pane.add(creerInfo(), BorderLayout.EAST);
        pane.add(creerSauv(), BorderLayout.SOUTH);
        plateau = new JPanel(new GridLayout(l, c));
        EcouteurSouris ecouteurSouris = new EcouteurSouris(this);
        plateau.addMouseListener(ecouteurSouris);
        pane.add(plateau, BorderLayout.CENTER);

        Gaufre g = modele.getGaufre();
        int lignes = g.getNbLignes();
        int colonnes = g.getNbColonnes();

        affichageNorth = new JLabel("    ");
        affichageNorth.setForeground(new Color(250, 50, 50));
        affichageNorth.setOpaque(false);
        affichageNorth.setAlignmentX(Component.CENTER_ALIGNMENT);
        pane.add(affichageNorth, BorderLayout.NORTH);

        for (int i = 0; i < lignes; i++) {
            for (int j = 0; j < colonnes; j++) {
                cellGaufre cell = new cellGaufre(gaufreMilieu);
                plateau.add(cell);
                gaufreCells[i * g.getNbColonnes() + j] = cell;
            }
        }
        gaufreCells[0].setImg(poison);
        for (int i = 0; i < lignes; i++) {
            for (int j = 0; j < colonnes; j++) {
                if (!g.getCase(i, j)) {
                    mangeCellGaufre(i, j);
                }
            }
        }

        // Faire jouer l'ia tout de suite si mode 1 joueur et J2 commence
        if (modele.getNbJoueurs() == 1) {
            switch (getTypeIA()) {
                case ALEA:
                    modele.setIA(new IAaleatoire());
                    break;
                case GAGNANT:
                    modele.setIA(new IAcoupGagnant());
                    break;
                case EXPLO:
                    modele.setIA(new IAexploration());
                    break;
            }
            if (modele.getGaufre().getJoueurCourant() == modele.getGaufre().getJoueur2()) {
                Config.debug("L'IA commence !");
                Coup coupIA = modele.jouerIA();
                l = (int) coupIA.getPosition().getX();
                c = (int) coupIA.getPosition().getY();
                Config.debug("Coup IA : ", l, c);
                mangeCellGaufre(l, c);
            }
        }
        return pane;

    }

    private Container creerSauv() {
        JPanel pane = new JPanel();
        pane.setLayout(new GridLayout(1, 2));
        JButton sauvegarder = new JButton("Sauvegarder");
        sauvegarder.setMnemonic(KeyEvent.VK_S);
        sauvegarder.setActionCommand("Sauvegarder");
        sauvegarder.addActionListener(new EcouteurJeu(this));
        JButton charger = new JButton("Charger");
        charger.setMnemonic(KeyEvent.VK_C);
        charger.setActionCommand("Charger");
        charger.addActionListener(new EcouteurJeu(this));

        pane.add(sauvegarder);
        pane.add(charger);
        return pane;
    }

    public void afficheGagnant() {
        if (getMG().getNbJoueurs() == 1) {
            if (getMG().getGaufre().estFinie().getNum() == 1) {
                affichageNorth.setText("Victoire du Joueur, tu viens de sauver l'humanité !");
            } else {
                affichageNorth.setText("Victoire de l'IA, les robots nous dominent...");
            }
        } else {
            affichageNorth.setText("Victoire du Joueur " + getMG().getGaufre().estFinie().getNum() + ", trop fort !");
        }
        affichageNorth.setForeground(new Color(70, 200, 10));
    }

    public void revertAfficahgeInval() {
        affichageNorth.setText("    ");
        boolCoupInval = false;
    }

    public void coupInval() {
        affichageNorth.setText("Coup Invalide");
    }

    private Container creerInfo() {
        JPanel pane = new JPanel();

        JPanel textes = new JPanel();
        textes.setLayout(new BoxLayout(textes, BoxLayout.Y_AXIS));
        JLabel tour = new JLabel();
        tour.setName("texteTour");
        tour.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel score = new JLabel();
        score.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel scoreJ1 = new JLabel();
        scoreJ1.setName("texteScoreJ1");
        scoreJ1.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel scoreJ2 = new JLabel();
        scoreJ2.setName("texteScoreJ2");
        scoreJ2.setAlignmentX(Component.CENTER_ALIGNMENT);

        tour.setText("Tour : Joueur " + modele.getGaufre().getJoueurCourant().getNum());
        score.setText("Scores :");
        scoreJ1.setText("Joueur 1 : " + modele.getGaufre().getJoueur1().getScore());
        scoreJ2.setText("Joueur 2 : " + modele.getGaufre().getJoueur2().getScore());

        textes.add(tour);
        textes.add(score);
        textes.add(scoreJ1);
        textes.add(scoreJ2);

        // Historique
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        JPanel histPanel = new JPanel();
        histPanel.setLayout(new BoxLayout(histPanel, BoxLayout.Y_AXIS));
        histPanel.setName("histPanel");
        scrollPane.setViewportView(histPanel);
        scrollPane.setPreferredSize(new Dimension(fenetre.getHeight() / 4, fenetre.getHeight() / 3));

        JPanel boutons = new JPanel();
        boutons.setLayout(new GridLayout(3, 3));

        JButton annuler = new JButton("Annuler");
        annuler.setMnemonic(KeyEvent.VK_A);
        annuler.setName("boutonAnnuler");
        annuler.setActionCommand("Annuler");
        annuler.addActionListener(new EcouteurJeu(this));
        annuler.setEnabled(modele.peutAnnuler());
        JButton refaire = new JButton("Refaire");
        refaire.setMnemonic(KeyEvent.VK_R);
        refaire.setName("boutonRefaire");
        refaire.setActionCommand("Refaire");
        refaire.addActionListener(new EcouteurJeu(this));
        refaire.setEnabled(modele.peutRefaire());
        JButton reset = new JButton("Reset");
        reset.setMnemonic(KeyEvent.VK_DELETE);
        reset.setActionCommand("Reset");
        reset.addActionListener(new EcouteurJeu(this));
        JButton quitter = new JButton("Quitter");
        quitter.setMnemonic(KeyEvent.VK_Q);
        quitter.setActionCommand("QuitterJeu");
        quitter.addActionListener(new EcouteurJeu(this));
        JButton plus = new JButton("+");
        plus.setMnemonic(KeyEvent.VK_P);
        plus.setActionCommand("Plus");
        plus.addActionListener(new EcouteurJeu(this));
        JButton moins = new JButton("-");
        moins.setMnemonic(KeyEvent.VK_M);
        moins.setActionCommand("Moins");
        moins.addActionListener(new EcouteurJeu(this));

        boutons.add(annuler);
        boutons.add(refaire);
        boutons.add(quitter);
        boutons.add(reset);
        boutons.add(plus);
        boutons.add(moins);

        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        pane.add(textes);
        pane.add(scrollPane);
        pane.add(boutons);

        return pane;
    }

    public void majInfo() {

        JLabel tour = (JLabel) getComponentByName(fenetre, "texteTour");
        JLabel scoreJ1 = (JLabel) getComponentByName(fenetre, "texteScoreJ1");
        JLabel scoreJ2 = (JLabel) getComponentByName(fenetre, "texteScoreJ2");
        JPanel histPanel = (JPanel) getComponentByName(fenetre, "histPanel");
        if (getMG().getNbJoueurs() == 1) {
            if (modele.getGaufre().getJoueurCourant().getNum() == 1) {
                tour.setText("Tour du Joueur");
            } else {
                tour.setText("Tour de l'IA");
            }
            scoreJ1.setText("Joueur : " + modele.getGaufre().getJoueur1().getScore());
            scoreJ2.setText("IA     : " + modele.getGaufre().getJoueur2().getScore());
        } else {
            tour.setText("Tour du Joueur " + modele.getGaufre().getJoueurCourant().getNum());
            scoreJ1.setText("Joueur 1 : " + modele.getGaufre().getJoueur1().getScore());
            scoreJ2.setText("Joueur 2 : " + modele.getGaufre().getJoueur2().getScore());
        }

        JLabel hist = new JLabel();
        hist.setText(modele.getGaufre().getHistorique().pourAffichage(getMG().getNbJoueurs() == 1));
        histPanel.removeAll();
        histPanel.repaint();
        histPanel.add(hist);

        JButton annuler = (JButton) getComponentByName(fenetre, "boutonAnnuler");
        JButton refaire = (JButton) getComponentByName(fenetre, "boutonRefaire");

        annuler.setEnabled(modele.peutAnnuler());
        refaire.setEnabled(modele.peutRefaire());
    }

    public void syncGaufre() {
        Gaufre g = modele.getGaufre();
        Random random = new Random();
        int randomMiettes;

        int lignes = g.getNbLignes();
        int colonnes = g.getNbColonnes();

        for (int i = 0; i < lignes; i++) {
            for (int j = 0; j < colonnes; j++) {
                cellGaufre cell = gaufreCells[i * g.getNbColonnes() + j];
                if (g.getCase(i, j)) {
                    if (i == 0 && j == 0) {
                        cell.setImg(poison);
                    } else {
                        cell.setImg(gaufreMilieu);
                    }
                } else {
                    if (cell.getImg() == gaufreMilieu) {
                        randomMiettes = random.nextInt(4);
                        switch (randomMiettes) {
                            case 0:
                                cell.setImg(miettes1);
                                break;
                            case 1:
                                cell.setImg(miettes2);
                                break;
                            case 2:
                                cell.setImg(miettes3);
                                break;
                            default:
                                cell.setImg(miettes4);
                                break;
                        }
                    }
                }
            }
        }
        plateau.repaint();
    }

    public void mangeCellGaufre(int l, int c) {
        Gaufre gaufre = modele.getGaufre();
        Random random = new Random();
        int randomMiettes;

        // Get the dimensions of the waffle grid
        int nbLignes = gaufre.getNbLignes();
        int nbColonnes = gaufre.getNbColonnes();

        // Define starting and ending row/column for the square
        int startRow = Math.min(l, nbLignes - 1);
        int startCol = Math.min(c, nbColonnes - 1);
        int endRow = Math.max(l, nbLignes - 1);
        int endCol = Math.max(c, nbColonnes - 1);

        // Loop through cells in the square
        for (int i = startRow; i <= endRow; i++) {
            for (int j = startCol; j <= endCol; j++) {
                cellGaufre cell = gaufreCells[i * nbColonnes + j];
                if (cell.getImg() == gaufreMilieu) {
                    randomMiettes = random.nextInt(3);
                    switch (randomMiettes) {
                        case 0:
                            cell.setImg(miettes1);
                            break;
                        case 1:
                            cell.setImg(miettes2);
                            break;
                        case 2:
                            cell.setImg(miettes3);
                            break;
                        default:
                            cell.setImg(miettes4);
                            break;
                    }
                    cell.repaint();
                }
            }
        }
        if (!Config.estMuet()) {
            EffetsSonores.playSound("crunch");
        }
    }

    private class cellGaufre extends JPanel {
        Image img;

        public cellGaufre(BufferedImage img) {
            this.img = img;
        }

        public void setImg(BufferedImage newImg) {
            this.img = newImg;
        }

        public Image getImg() {
            return this.img;
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
        int gagnant = getMG().getGaufre().estFinie().getNum();
        int nbCoupsJoues = getMG().getGaufre().getHistorique().getNbFaits();
        majInfo();
        // Afficher à l'écran
        System.out.println("Joueur " + gagnant + " a gagné !");
        System.out.println("La partie a duré " + nbCoupsJoues + " coups.");
    }

    public void setEtat(int newEtat) {
        etat = newEtat;
        metAJourFenetre();
    }

    public void setTypeIA(int type) {
        this.typeIA = type;
    }

    public int getEtat() {
        return etat;
    }

    public ModeGraphique getMG() {
        return modele;
    }

    public Musique getMusique() {
        return bgMusique;
    }

    public JFrame getFenetre() {
        return fenetre;
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

    public int getTypeIA() {
        return typeIA;
    }
}

class FondMenu extends JPanel {
    private BufferedImage textureImage;
    private int BIHeight;
    private int BIWidth;
    private TexturePaint texturePaint;
    private int xOffset = 0;
    private int yOffset = 0;

    public FondMenu() {
        textureImage = ResourceLoader.lireImage("gaufreMilieu");
        BIHeight = textureImage.getHeight();
        BIWidth = textureImage.getWidth();
        texturePaint = new TexturePaint(textureImage,
                new Rectangle(0, 0, textureImage.getWidth(), textureImage.getHeight()));

        // Start the animation
        Timer timer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Update the offsets to create the panning effect
                xOffset = (xOffset + 1) % BIWidth;
                yOffset = (yOffset + 1) % BIHeight;

                // Trigger a repaint
                repaint();
                getToolkit().sync();
            }
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Adjust the texture paint's position based on the offsets
        g2d.translate(-xOffset, -yOffset);
        g2d.setPaint(texturePaint);
        g2d.fillRect(0, 0, getWidth() + BIWidth, getHeight() + BIHeight);
    }
}
