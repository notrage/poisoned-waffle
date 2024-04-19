package Gaufre.Modele;

public interface IA {

    // Initialise l'IA et potentiellement ses coups 
    public void init(Gaufre g);

    // Renvoi le coup suivant de l'IA 
    public Coup coupSuivant();
}
