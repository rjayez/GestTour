/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele3P;

import java.util.ArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 *
 * @author Romain
 */
public class EquipeTest
{
    
    public EquipeTest()
    {
    }

    @Test
    public void testClasserEquipe()
    {
        System.out.println("classerEquipe");
        ArrayList<Equipe> listEq = null;
        Equipe.classerEquipe(listEq);
        fail("The test case is a prototype.");
    }

    @Test
    public void testClasserEquipeTourUnique()
    {
        System.out.println("classerEquipeTourUnique");
        ArrayList<Equipe> listEq = null;
        int indexTour = 0;
        Equipe.classerEquipeTourUnique(listEq, indexTour);
        fail("The test case is a prototype.");
    }

    @Test
    public void testClasserEquipeIndexTour()
    {
        System.out.println("classerEquipeIndexTour");
        ArrayList<Equipe> listEq = null;
        int indexTour = 0;
        Equipe.classerEquipeIndexTour(listEq, indexTour);
        fail("The test case is a prototype.");
    } 

    @Test
    public void testOrdonnerEquipe()
    {
        System.out.println("ordonnerEquipe");
        ArrayList<Equipe> listEq = null;
        Equipe.ordonnerEquipe(listEq);
        fail("The test case is a prototype.");
    }

   
    @Test
    public void testTourFini()
    {
        System.out.println("tourFini");
        ArrayList<Equipe> equipes = null;
        int nbEpreuve = 0;
        int indexTour = 0;
        boolean expResult = false;
        boolean result = Equipe.tourFini(equipes, nbEpreuve, indexTour);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testEffacerScore()
    {
        System.out.println("effacerScore");
        ArrayList<Equipe> equipes = null;
        Equipe.effacerScore(equipes);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetNomsJoueur()
    {
        System.out.println("getNomsJoueur");
        Equipe instance = null;
        String expResult = "";
        String result = instance.getNomsJoueur();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetJoueurs()
    {
        System.out.println("getJoueurs");
        Equipe instance = null;
        Joueur[] expResult = null;
        Joueur[] result = instance.getJoueurs();
        
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetNumero()
    {
        System.out.println("getNumero");
        Equipe instance = null;
        int expResult = 0;
        int result = instance.getNumero();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetNumeroStr()
    {
        System.out.println("getNumeroStr");
        Equipe instance = null;
        String expResult = "";
        String result = instance.getNumeroStr();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetNumero()
    {
        System.out.println("setNumero");
        int numero = 0;
        Equipe instance = null;
        instance.setNumero(numero);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetJoueurs()
    {
        System.out.println("setJoueurs");
        Joueur[] joueurs = null;
        Equipe instance = null;
        instance.setJoueurs(joueurs);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetScores_0args()
    {
        System.out.println("getScores");
        Equipe instance = null;
        int[][] expResult = null;
        int[][] result = instance.getScores();
        
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetScores_int_int()
    {
        System.out.println("getScores");
        int indexTour = 0;
        int indexEpr = 0;
        Equipe instance = null;
        String expResult = "";
        String result = instance.getScores(indexTour, indexEpr);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetScoreTotal()
    {
        System.out.println("getScoreTotal");
        Equipe instance = null;
        int expResult = 0;
        int result = instance.getScoreTotal();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetScoreTotalPondere()
    {
        System.out.println("getScoreTotalPondere");
        ArrayList<Epreuve> epreuves = new ArrayList<>();
        
        Epreuve ep1 = new Epreuve("ep1", 10);
        Epreuve ep2 = new Epreuve("ep2", 11);
        Epreuve ep3 = new Epreuve("ep3", 12);
        epreuves.add(ep1);
        epreuves.add(ep2);
        epreuves.add(ep3);
        Equipe instance = new Equipe(1, Categorie.Femme, Configuration.getInstance());
        int[][] scores = new int[3][3];
        scores[0][0] = 1; // 1 * 66 = 66 points pondérés
        scores[0][1] = 2; // 2 * 60 = 120 points pondérés
        scores[0][2] = 3; // 3 * 55 = 165 points pondérés
        scores[1][0] = 4; // 4 * 66 = 264 points pondérés
        scores[1][1] = 5; // 5 * 60 = 300 points pondérés
        scores[1][2] = 6; // 6 * 55 = 330 points pondérés
        scores[2][0] = 7; // 7 * 66 = 462 points pondérés
        scores[2][1] = 8; // 8 * 60 = 480 points pondérés
        scores[2][2] = 9; // 9 * 55 = 495 points pondérés
        instance.setScores(scores);
            
        int expResult = 2682;// total des points
        int result = instance.getScoreTotalPondere(epreuves);
        
        
        assertEquals(expResult, result);
        
    }

    @Test
    public void testGetScoreTotalTour()
    {
        System.out.println("getScoreTotalTour");
        int indexTour = 0;
        Equipe instance = null;
        int expResult = 0;
        int result = instance.getScoreTotalTour(indexTour);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetScoreTour()
    {
        System.out.println("getScoreTour");
        int indexTour = 0;
        Equipe instance = null;
        int expResult = 0;
        int result = instance.getScoreTour(indexTour);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetScores_intArrArr()
    {
        System.out.println("setScores");
        int[][] scores = null;
        Equipe instance = null;
        instance.setScores(scores);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetScores_3args()
    {
        System.out.println("setScores");
        int indexTour = 0;
        int indexEpreuve = 0;
        int score = 0;
        Equipe instance = null;
        instance.setScores(indexTour, indexEpreuve, score);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetCategorie()
    {
        System.out.println("getCategorie");
        Equipe instance = null;
        Categorie expResult = null;
        Categorie result = instance.getCategorie();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetCategorie()
    {
        System.out.println("setCategorie");
        Categorie categorie = null;
        Equipe instance = null;
        instance.setCategorie(categorie);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetPartiesGagnees()
    {
        System.out.println("getPartiesGagnees");
        Equipe instance = null;
        int[] expResult = null;
        int[] result = instance.getPartiesGagnees();
        
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetPartiesGagneesTotalPartiel()
    {
        System.out.println("getPartiesGagneesTotalPartiel");
        int indexTour = 0;
        Equipe instance = null;
        int expResult = 0;
        int result = instance.getPartiesGagneesTotalPartiel(indexTour);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetPartiesGagneesTot()
    {
        System.out.println("getPartiesGagneesTot");
        Equipe instance = null;
        int expResult = 0;
        int result = instance.getPartiesGagneesTot();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetPartiesGagnees()
    {
        System.out.println("setPartiesGagnees");
        int[] partiesGagnees = null;
        Equipe instance = null;
        instance.setPartiesGagnees(partiesGagnees);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetPartieGagnee()
    {
        System.out.println("setPartieGagnee");
        int indexTour = 0;
        int nbPartieGagne = 0;
        Equipe instance = null;
        instance.setPartieGagnee(indexTour, nbPartieGagne);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetEquipesJouees()
    {
        System.out.println("getEquipesJouees");
        Equipe instance = null;
        int[] expResult = null;
        int[] result = instance.getEquipesJouees();
        
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetEquipesJouees()
    {
        System.out.println("setEquipesJouees");
        int[] equipesJouees = null;
        Equipe instance = null;
        instance.setEquipesJouees(equipesJouees);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetEquipeJouee()
    {
        System.out.println("setEquipeJouee");
        int indexTour = 0;
        int numEquipe = 0;
        Equipe instance = null;
        instance.setEquipeJouee(indexTour, numEquipe);
        fail("The test case is a prototype.");
    }

    @Test
    public void testIsPresent()
    {
        System.out.println("isPresent");
        Equipe instance = null;
        boolean expResult = false;
        boolean result = instance.isPresent();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetPresent()
    {
        System.out.println("setPresent");
        boolean present = false;
        Equipe instance = null;
        instance.setPresent(present);
        fail("The test case is a prototype.");
    }
    
}
