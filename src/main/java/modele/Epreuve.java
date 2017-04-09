package modele;

import lombok.Data;
import utils.GestTourUtils;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class Epreuve
{


	private String nom;
	private int nbPointGagnant;
	
	public Epreuve(String nom, int nbPointGagnant)
	{
		this.nom = nom;
		this.nbPointGagnant = nbPointGagnant;
	}
	
	public Epreuve()
	{
		this("", 0);
	}

    public Epreuve(final Epreuve epreuve){
        nom = epreuve.getNom();
        nbPointGagnant = epreuve.getNbPointGagnant();
    }

	
	private int calcPPCM(int Nb1, int Nb2)
	{
		int Produit, Reste, PPCM;
		
		Produit = Nb1 * Nb2;
		Reste = Nb1 % Nb2;
		while (Reste != 0)
		{
			Nb1 = Nb2;
			Nb2 = Reste;
			Reste = Nb1 % Nb2;
		}
		PPCM = Produit / Nb2;

		return PPCM;
	}
	
	private int calculPPCMscore(int[] tabNb)
	{
		if (tabNb.length > 2)
		{
			int temp = calcPPCM(tabNb[0], tabNb[1]);
			for (int i = 2; i < tabNb.length - 1; i++)
			{
				temp = calcPPCM(temp, tabNb[i]);
			}
			return temp;
		}
		else if (tabNb.length == 2)
		{
			return calcPPCM(tabNb[0], tabNb[1]);
		}
		else
		{
			return -1;
		}
		
	}
    
    
    /**
     * Calcul le ppcm des scores gagnant de toutes les épreuves
     * @param epreuves tableau des épreuves
     * @return renvoie le ppcm
     */
    public static int getPpcmEpreuve(final List<Epreuve> epreuves)
    {
        List<Integer> scoreMaxEpreuve = epreuves.stream().map(Epreuve::getNbPointGagnant).collect(Collectors.toList());

		return GestTourUtils.calculerPGCD(scoreMaxEpreuve);
    }
}
