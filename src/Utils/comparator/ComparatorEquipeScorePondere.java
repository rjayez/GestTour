/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils.comparator;

import Modele3P.Epreuve;
import Modele3P.Equipe;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author Romain
 */
public class ComparatorEquipeScorePondere implements Comparator<Equipe>
{
    final int indexTour;
    final List<Epreuve> listEpreuve;
    
    public ComparatorEquipeScorePondere(final int indexTour, final List<Epreuve> listEpreuve)
    {
        this.indexTour = indexTour;
        this.listEpreuve = listEpreuve;
    }
    

    @Override
    public int compare(Equipe equipe1, Equipe equipe2)
    {
         if (equipe1.getPartiesGagneesTotalPartiel(indexTour) > equipe2.getPartiesGagneesTotalPartiel(indexTour)) // comparaison nombre épreuve gagnée
            {
                return -1; // retour négatif pour avoir un classement décroissant
            }
            else if (equipe1.getPartiesGagneesTotalPartiel(indexTour) < equipe2.getPartiesGagneesTotalPartiel(indexTour))
            {
                return 1;
            }
            else
            {
                if (equipe1.getScoreTotalTourPondere(listEpreuve, indexTour) > equipe2.getScoreTotalTourPondere(listEpreuve, indexTour)) // classement par score
                {
                    return -1;
                }
                else if (equipe1.getScoreTotalTourPondere(listEpreuve, indexTour) < equipe2.getScoreTotalTourPondere(listEpreuve, indexTour))
                {
                    return 1;
                }
                else
                {
                    return new Equipe.ComparateurEquipeNum().compare(equipe1, equipe2);   
                }
            }
    }
    
}
