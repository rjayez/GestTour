package Modele3P;

import Utils.GestTourUtils;
import java.util.ArrayList;

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
	
	public String getNom()
	{
		return nom;
	}
	
	public void setNom(String nom)
	{
		this.nom = nom;
	}
	
	public int getNbPointGagnant()
	{
		return nbPointGagnant;
	}
	
	public void setNbPointGagnant(int nbPointGagnant)
	{
		this.nbPointGagnant = nbPointGagnant;
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
		// System.out.println("PGCD = " + Nb2 + " PPCM = " + PPCM);
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
    public static int getPpcmEpreuve(final ArrayList<Epreuve> epreuves)
    {
        ArrayList<Integer> scoreMaxEpreuve = new ArrayList<>();
        for(Epreuve epreuve : epreuves)
        {
            scoreMaxEpreuve.add(epreuve.getNbPointGagnant());
        }
        
        return GestTourUtils.calculerPGCD(scoreMaxEpreuve);
    }
}
