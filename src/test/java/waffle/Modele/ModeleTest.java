package waffle.Modele;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import Modele.*;

public class ModeleTest {
    @Test 
    public void testFindePartie(){
        Gaufre g = new Gaufre(3, 3);
        assertTrue(g.estFinie() == null);
        g.jouer(new Coup(0, 1));

        assertTrue(g.estFinie() == true);
        assertTrue(g.estFinie() == true);
        assertTrue(g.estFinie() == true);
        assertTrue(g.estFinie() == true);
        assertTrue(g.estFinie() == true);

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
        joueDejoue(g, new Coup(2, 2));
        assertTrue(g.jouer(new Coup(2, 2)));
        joueDejoue(g, new Coup(0, 1));
        joueDejoue(g, new Coup(1, 0));
        joueDejoue(g, new Coup(0, 0));
        assertTrue(g.dejouer());
        assertTrue(g.jouer(new Coup(0, 1)));
        dejoueRejoue(g);
        assertTrue(g.dejouer());
        assertFalse(g.dejouer());
        assertTrue(g.estRejouable());
        g.jouer(new Coup(0, 1));
        assertFalse(g.estRejouable());
    }

    private void joueDejoue(Gaufre g, Coup c) {
        boolean[][] copie = new boolean[g.getLignes()][g.getColonnes()];
        for (int i = 0; i < g.getLignes(); i++) {
            for (int j = 0; j < g.getColonnes(); j++) {
                copie[i][j] = g.getCase(i, j);
            }
        }
        // Si le coup est valide on le déjoue
        if (g.jouer(c)) {
            g.dejouer();
        }
        equivalent(copie, g.getPlateau());
    }

    private void dejoueRejoue(Gaufre g) {
        boolean[][] copie = new boolean[g.getLignes()][g.getColonnes()];
        for (int i = 0; i < g.getLignes(); i++) {
            for (int j = 0; j < g.getColonnes(); j++) {
                copie[i][j] = g.getCase(i, j);
            }
        }
        assertTrue(g.estDejouable());
        g.dejouer();
        assertTrue(g.estRejouable());
        g.rejouer();
        equivalent(copie, g.getPlateau());
    }

    public void equivalent(boolean[][] m1, boolean[][] m2) {
        for (int i = 0; i < m1.length; i++) {
            for (int j = 0; j < m1[0].length; j++) {
                assertTrue(m1[i][j] == m2[i][j]);
            }
        }
    }

    public void afficher(boolean[][] m) {
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                System.out.print(m[i][j] ? 'O' : 'X');
            }
            System.out.println();
        }
    }
}
