/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

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
        
        
        Assert.assertEquals(expResult, result);
        
    }


    
}
