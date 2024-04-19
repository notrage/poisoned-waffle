package waffle.Modele;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

import Gaufre.Modele.*;

public class ModeleTest {
/*    
    @Test
    public void sauvegardeRestaureGaufreTest() throws Exception {
        // Avec une gaufre simplement initialisée
        Gaufre g = new Gaufre(3,3);
        g.sauvegarder("test.txt");
        Gaufre restoree = new Gaufre("test.txt");
        estGaufreEquivalente(g, restoree);
    }
*/
    @Test 
    public void copieGaufreTest() {
        // Copie de base
        Gaufre g = new Gaufre(3, 3);
        Gaufre copie = g.clone();
        estGaufreEquivalente(g, copie);
        // Et maintenant si on fait jouer des coups sur le plateau
        for (int i = 2; i > 0; i--) {
            for (int j = 2; j > 0; j--) {
                if (i != 0 || j != 0) {
                    g.jouer(new Coup(i, j));
                    copie.jouer(new Coup(i, j));
                    estGaufreEquivalente(g, copie);
                }
            }
        }
    }

    @Test
    public void coupTest() {
        Gaufre g = new Gaufre(3, 3);
        // Verification de la validité d'un coup
        Coup coupValide = new Coup(2, 2);
        Coup coupInvalide = new Coup(3, 5);
        assertTrue(g.jouer(coupValide));
        // Le nouveau terrain ne doit pas permettre de rejouer le même coup
        assertFalse(g.jouer(coupValide));
        // Coup impossible
        assertFalse(g.jouer(coupInvalide));

        // Un autre coup valide
        coupValide = new Coup(1, 2);
        assertTrue(g.jouer(coupValide));

        // Un autre coup invalide
        coupInvalide = new Coup(-1, 0);
        assertFalse(g.jouer(coupInvalide));
    }

    @Test
    public void historiqueTest() {
        // Joue un coup, le déjoue, vérifie si le plateau est de retour à son état
        // initial
        Gaufre g = new Gaufre(3, 3);
        Joueur jcourant = g.getJoueurCourant();
        joueDejoue(g, new Coup(2, 2));
        assert(g.getJoueurCourant() == jcourant);
        assertTrue(g.jouer(new Coup(2, 2)));
        jcourant = g.getJoueurCourant();
        joueDejoue(g, new Coup(0, 1));
        assert(g.getJoueurCourant() == jcourant);
        joueDejoue(g, new Coup(1, 0));
        assert(g.getJoueurCourant() == jcourant);
        joueDejoue(g, new Coup(0, 0));
        assertTrue(g.dejouer());
        g.jouer(new Coup(0, 1));
        jcourant = g.getJoueurCourant();
        dejoueRejoue(g);
        assert(g.getJoueurCourant() == jcourant);
        assertTrue(g.dejouer());
        assertFalse(g.dejouer());
        assertTrue(g.estRejouable());
        jcourant = g.getJoueurCourant();
        g.jouer(new Coup(0, 1));
        assert(g.getJoueurCourant() != jcourant);
        assertFalse(g.estRejouable());
    }

    @Test 
    public void testFinDePartie() {
        // Un test de base
        Gaufre g = new Gaufre(3, 3);
        assertTrue(g.estFinie() == null);
        g.jouer(new Coup(0,1));
        assertTrue(g.estFinie() == null);
        g.jouer(new Coup(2,0));
        assertTrue(g.estFinie() == null);
        g.jouer(new Coup(1,0));
        assertTrue(g.estFinie() != null);
        
        // Cas où l'on joue un coup sur la case empoisonée
        g.reinitialiser();
        assertTrue(g.estFinie() == null);
        g.jouer(new Coup(0, 0));
        assertTrue(g.estFinie() != null);

        // Cas où l'on joue toutes les cases du plateau
        g.reinitialiser();
        for (int i = 2; i > 0; i--) {
            for (int j = 2; j > 0; j--) {
                g.jouer(new Coup(i, j));
                if (i != 0 || j != 0)
                    assertTrue(g.estFinie() == null);
                else
                    assertTrue(g.estFinie() != null);
            }
        }
    }

    private void joueDejoue(Gaufre g, Coup c) {
        int[] copie = g.clonePlateau();

        // Si le coup est valide on le déjoue
        if (g.jouer(c)) {
            g.dejouer();
        }
        estPlateauEquivalent(copie, g.getPlateau());
    }

    private void dejoueRejoue(Gaufre g) {
        int[] copie = g.clonePlateau();

        assertTrue(g.estDejouable());
        g.dejouer();
        assertTrue(g.estRejouable());
        g.rejouer();
        estPlateauEquivalent(copie, g.getPlateau());
    }

    public void estPlateauEquivalent(int[] g1, int[] g2) {
        assertTrue(g1.length == g2.length);
        for (int i = 0; i < g1.length; i++) {
            assertTrue(g1[i] == g2[i]);
        }
    }

    public void estGaufreEquivalente(Gaufre g, Gaufre copie) {
        assertTrue(g.getNbColonnes() == copie.getNbColonnes());
        assertTrue(g.getNbLignes() == copie.getNbLignes());
        assertTrue(g.getJoueur1().getNum() == copie.getJoueur1().getNum());
        assertTrue(g.getJoueurCourant().getNum() == copie.getJoueurCourant().getNum());
        if (g.getJoueurCourant() == g.getJoueur1())
            assertTrue(copie.getJoueurCourant() == copie.getJoueur1());
        else
            assertTrue(copie.getJoueurCourant() == copie.getJoueur2());
        for (int i = 0; i < g.getNbLignes(); i++){
            for (int j = 0; j < g.getNbColonnes(); j++){
                assertTrue(g.getCase(i, j) == copie.getCase(i, j));
            }
        }
    }
}