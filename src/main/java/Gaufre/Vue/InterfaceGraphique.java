package Gaufre.Vue;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import javax.swing.*;

import Gaufre.Controleur.EcouteurJeu;
import Gaufre.Controleur.EcouteurMenu;
import Gaufre.Controleur.EcouteurSouris;
import Gaufre.Configuration.ResourceLoader;
import Gaufre.Configuration.Config;

public class InterfaceGraphique implements Runnable {
    public final int MENU = 0;
    public final int JEU = 1;
    public final int QUIT = -1;
    private BufferedImage gaufreMilieu, poison, miettes1;
    private ModeGraphique modele;
    private EcouteurMenu ecouteurMenu;
    private Musique bgMusique;
    private int etat;
    private JFrame fenetre;
    private GraphicsEnvironment ge;
    private Container plateau;

    private ArrayList<JLabel> textesAModifier;
    private ArrayList<JButton> boutonsAModifier;

    InterfaceGraphique(ModeGraphique mg) {
        etat = MENU;
        modele = mg;
        ecouteurMenu = new EcouteurMenu(this);
        // bgMusique = new Musique("sons/SpaceJazz.wav");
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
        int titleFontSize = (int) (menuHeight / 4);
        title.setFont(new Font("DEADLY POISON II", Font.BOLD, titleFontSize));
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

        JButton button1J = new JButton("1 joueur");
        button1J.addActionListener(ecouteurMenu);
        button1J.setActionCommand("Jeu1J");
        button1J.setAlignmentX(Component.CENTER_ALIGNMENT);
        middlePanel.add(button1J, gbc);

        JButton button2J = new JButton("2 joueurs");
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
        // volumeButton.setPreferredSize(new Dimension((int) (0.15 * menuHeight), (int)
        // (0.15 * menuHeight)));
        volumeButton.addActionListener(ecouteurMenu);
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
        versionLabel.setFont(new Font("Arial", Font.PLAIN, (int) (titleFontSize / 6)));
        versionLabel.setForeground(new Color(34, 84, 124));
        bottomRight.add(Box.createVerticalGlue());
        bottomRight.add(versionLabel);
        bottomPanel.add(bottomRight);

        pane.add(bottomPanel, BorderLayout.PAGE_END);

        layeredPane.add(pane, JLayeredPane.PALETTE_LAYER);

        fenetre.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Calculate font size based on window width
                int newTitleFontSize = Math.max(50, fenetre.getHeight() / 4);
                title.setFont(new Font("DEADLY POISON II", Font.BOLD, newTitleFontSize));
                versionLabel.setFont(new Font("Arial", Font.PLAIN, (int) (newTitleFontSize / 6)));

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
            setButtonIcons(getButtonByCmd(fenetre, "volume"), volImg);
        } else {
            bgMusique.play();
            BufferedImage volImg = ResourceLoader.lireImage("volume");
            setButtonIcons(getButtonByCmd(fenetre, "volume"), volImg);
        }
    }

    private JButton getButtonByCmd(Component component, String actionCmd) {
        if (component instanceof JButton) {
            JButton button = (JButton) component;
            if (button.getActionCommand().equals(actionCmd)) {
                return button;
            }
        }

        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                JButton foundButton = getButtonByCmd(child, actionCmd);
                if (foundButton != null) {
                    return foundButton;
                }
            }
        }

        return null; // Return null if no button is found
    }

    private Container creerJeu() {
        Container pane = new Container();

        pane.setLayout(new BorderLayout());
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
        textesAModifier = new ArrayList<JLabel>();
        boutonsAModifier = new ArrayList<JButton>();

        Container textes = new Container();
        textes.setLayout(new BoxLayout(textes, BoxLayout.Y_AXIS));
        JLabel texte0 = new JLabel();
        JLabel texte1 = new JLabel();
        JLabel texte2 = new JLabel();
        JLabel texte3 = new JLabel();
        JLabel texte4 = new JLabel();

        texte0.setText("Tour : Joueur " + modele.getGaufre().getJoueurCourant().getNum());
        texte1.setText("");
        texte2.setText("Scores :");
        texte3.setText("Joueur 1 :" + modele.getGaufre().getJoueur1().getScore());
        texte4.setText("Joueur 2 :" + modele.getGaufre().getJoueur2().getScore());

        textes.add(texte0);
        textes.add(texte1);
        textes.add(texte2);
        textes.add(texte3);
        textes.add(texte4);
        textesAModifier.add(texte0);
        textesAModifier.add(texte1);
        textesAModifier.add(texte2);
        textesAModifier.add(texte3);
        textesAModifier.add(texte4);
        ////
        Container boutons = new Container();
        boutons.setLayout(new GridLayout(2, 2));

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
        boutonsAModifier.add(annuler);
        boutonsAModifier.add(refaire);
        boutonsAModifier.add(reset);
        boutonsAModifier.add(quitter);

        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        pane.add(textes);
        pane.add(boutons);

        return pane;
    }

    public void majInfo() {

        JLabel texte0 = textesAModifier.get(0);
        JLabel texte3 = textesAModifier.get(3);
        JLabel texte4 = textesAModifier.get(4);

        texte0.setText("Tour : Joueur " + modele.getGaufre().getJoueurCourant().getNum());
        texte3.setText("Joueur 1 :" + modele.getGaufre().getJoueur1().getScore());
        texte4.setText("Joueur 2 :" + modele.getGaufre().getJoueur2().getScore());

        JButton annuler = boutonsAModifier.get(0);
        JButton refaire = boutonsAModifier.get(1);

        annuler.setEnabled(modele.peutAnnuler());
        refaire.setEnabled(modele.peutRefaire());
    }

    public void afficherGaufre() {

        plateau.removeAll();

        int lignes = modele.getGaufre().getNbLignes();
        int colonnes = modele.getGaufre().getNbColonnes();

        for (int i = 0; i < lignes; i++) {
            for (int j = 0; j < colonnes; j++) {
                if (modele.getGaufre().getCase(i, j)) {
                    if (i == 0 && j == 0)
                        plateau.add(new ajoutGaufre(poison));
                    else
                        plateau.add(new ajoutGaufre(gaufreMilieu));
                } else
                    plateau.add(new ajoutGaufre(miettes1));
            }
        }
        majInfo();
        fenetre.revalidate();
        fenetre.repaint();
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
