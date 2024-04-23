package Gaufre.Vue;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.*;

import Gaufre.Modele.Gaufre;
import Gaufre.Controleur.EcouteurMenu;

public class InterfaceGraphique implements Runnable {
    public final int MENU = 0;
    public final int JEU = 1;
    public final int QUIT = -1;
    private ModeGraphique modele;
    private EcouteurMenu ecouteurMenu = new EcouteurMenu(this);
    private int etat;
    private JFrame fenetre;
    private GraphicsEnvironment ge;

    InterfaceGraphique(ModeGraphique mg) {
        etat = MENU;
        modele = mg;
        try {
            ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            File fontFile = new File("src/main/resources/fonts/DEADLY_POISON_II.ttf");
            Font titleFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
            ge.registerFont(titleFont);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
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
        title.setForeground(new Color(0, 128, 0));
        title.setFont(new Font("DEADLY POISON II", Font.BOLD, 100));
        fenetre.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Calculate font size based on window width
                int newFontSize = Math.max(50, fenetre.getHeight() / 5);
                title.setFont(new Font("DEADLY POISON II", Font.BOLD, newFontSize));
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
        pane.add(titlePanel, BorderLayout.PAGE_START);

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
        modele.reset();

        return pane;
    }

    public void setEtat(int newEtat) {
        etat = newEtat;
        metAJourFenetre();
    }

    public ModeGraphique getMG() {
        return modele;
    }

    public static void main(String[] args) {
        Gaufre g = new Gaufre(10, 10);
        ModeGraphique mg = new ModeGraphique(g);
        InterfaceGraphique.demarrer(mg);
    }

}
