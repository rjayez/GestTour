package Modele3P;

import Utils.comparator.ComparatorEquipeScorePondere;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Equipe
{

    private int numero;
    private Joueur[] joueurs;
    private Categorie categorie;
    private int[][] scores; // [NbTour][NbEpreuve]
    private int[] partiesGagnees;
    private int[] equipesJouees;
    private boolean present;

    private static int indexTour;

    public Equipe(int numero, Joueur[] joueurs, Categorie categorie, int[][] scores, int[] partiesGagnees, int[] equipesJouees, boolean present)
    {
        this.numero = numero;

        this.joueurs = joueurs;
        this.categorie = categorie;
        this.scores = scores;
        this.partiesGagnees = partiesGagnees;
        this.equipesJouees = equipesJouees;
        this.present = present;
    }

    public Equipe(Equipe eq)
    {
        this.numero = eq.numero;
        this.joueurs = eq.joueurs;
        this.categorie = eq.categorie;
        this.scores = eq.scores.clone();
        this.present = eq.present;
        
        this.scores = new int[eq.scores.length][eq.scores[0].length];
        for (int i = 0; i < eq.scores.length; i++)
        {
            System.arraycopy(eq.scores[i], 0, scores[i], 0, eq.scores[0].length);
        }

        this.partiesGagnees = eq.partiesGagnees.clone();
        this.equipesJouees = eq.equipesJouees.clone();
        
    }
    
    public Equipe(int numero, Categorie categorie, Configuration config)
    {
        this.numero = numero;
        this.categorie = categorie;
        this.joueurs = new Joueur[config.getNbJoueurEquipe()];
        for (int i = 0; i < joueurs.length; i++)
        {
            joueurs[i] = new Joueur();
        }
        scores = new int[config.getNbTour()][config.getNbEpreuve()];
        equipesJouees = new int[config.getNbTour()];
        partiesGagnees = new int[config.getNbTour()];
        present = true;

    }

    public Equipe(Configuration config)
    {
        this(0, Categorie.Homme, config);
    }

    /**
     * Classement général des équipes
     *
     * @param listEq
     */
    public static void classerEquipe(ArrayList<Equipe> listEq)
    {
        Collections.sort(listEq, new ComparateurScore());
    }

    public static void classerEquipeTourUnique(ArrayList<Equipe> listEq, int indexTour)
    {
        Equipe.indexTour = indexTour;
        Collections.sort(listEq, new ComparateurScoreTourUnique());
    }

    public static void classerEquipeIndexTour(ArrayList<Equipe> listEq, int indexTour)
    {
        Equipe.indexTour = indexTour;
        Collections.sort(listEq, new ComparateurScoreIndexTour());
    }

    public static void ordonnerEquipe(ArrayList<Equipe> listEq)
    {
        Collections.sort(listEq, new ComparateurEquipeNum());
    }

    public static void classerEquipeScorePondere(List<Epreuve> listEpreuves, List<Equipe> listEquipe, int indexTour)
    {
       Collections.sort(listEquipe, new ComparatorEquipeScorePondere(indexTour, listEpreuves));
    }
        
    
    public static class ComparateurEquipeNum implements Comparator<Equipe>
    {

        @Override
        public int compare(Equipe e1, Equipe e2)
        {
            if (e1.getNumero() > e2.getNumero())
            {
                return 1;
            }
            else if (e1.getNumero() < e2.getNumero())
            {
                return -1;
            }
            else
            {
                return 0;
            }

        }
    }

    public static class ComparateurScore implements Comparator<Equipe>
    {

        @Override
        public int compare(Equipe e1, Equipe e2)
        {
            if (e1.getPartiesGagneesTot() > e2.getPartiesGagneesTot()) // comparaison nombre épreuve gagnée
            {
                return -1; // retour négatif pour avoir un classement croissant
            }
            else if (e1.getPartiesGagneesTot() < e2.getPartiesGagneesTot())
            {
                return 1;
            }
            else
            {
                if (e1.getScoreTotal() > e2.getScoreTotal()) // classement par score
                {
                    return -1;
                }
                else if (e1.getScoreTotal() < e2.getScoreTotal())
                {
                    return 1;
                }
                else
                {
                    return new ComparateurEquipeNum().compare(e1, e2);   
                }
            }

        }
    }

    /**
     * Compare pour un nombre donner de tour
     *
     */
    public static class ComparateurScoreIndexTour implements Comparator<Equipe>
    {

        @Override
        public int compare(Equipe e1, Equipe e2)
        {
            if (e1.getPartiesGagneesTotalPartiel(indexTour) > e2.getPartiesGagneesTotalPartiel(indexTour)) // comparaison nombre épreuve gagnée
            {
                return -1; // retour négatif pour avoir un classement croissant
            }
            else if (e1.getPartiesGagneesTotalPartiel(indexTour) < e2.getPartiesGagneesTotalPartiel(indexTour))
            {
                return 1;
            }
            else
            {
                if (e1.getScoreTotalTour(indexTour) > e2.getScoreTotalTour(indexTour)) // classement par score
                {
                    return -1;
                }
                else if (e1.getScoreTotalTour(indexTour) < e2.getScoreTotalTour(indexTour))
                {
                    return 1;
                }
                else
                {
                    ComparateurEquipeNum cen = new ComparateurEquipeNum();
                    return cen.compare(e1, e2);
                }
            }

        }

    }

    /**
     * Compare sur un tour unique indiqué
     *
     */
    public static class ComparateurScoreTourUnique implements Comparator<Equipe>
    {

        @Override
        public int compare(Equipe e1, Equipe e2)
        {
            if (e1.getPartiesGagnees()[indexTour] > e2.getPartiesGagnees()[indexTour]) // comparaison
            // nombre épreuve gagnée
            {
                return -1; // retour négatif pour avoir un classement croissant
            }
            else if (e1.getPartiesGagnees()[indexTour] < e2.getPartiesGagnees()[indexTour])
            {
                return 1;
            }
            else
            {
                if (e1.getScoreTour(indexTour) > e2.getScoreTour(indexTour)) // classement par
                // score
                {
                    return -1;
                }
                else if (e1.getScoreTour(indexTour) < e2.getScoreTour(indexTour))
                {
                    return 1;
                }
                else
                {
                    return new ComparateurEquipeNum().compare(e1, e2);
                }
            }

        }
    }

    public static boolean aScoreSaisie (ArrayList<Equipe> listEquipes)
    {
        for (Equipe equipe : listEquipes)
            {
               for(int[] scoreTour : equipe.getScores())
               {
                   for(int scoreEpreuve : scoreTour)
                   {
                       if(scoreEpreuve != 0)
                           return true;
                   }
               }
            }
        
        return false;
    }
    
    public static boolean tourFini(ArrayList<Equipe> equipes, int nbEpreuve, int indexTour)
    {
        if (!equipes.isEmpty())
        {
            int s = 0;
            for (Equipe equipe : equipes)
            {
                s += equipe.getPartiesGagnees()[indexTour];
            }

            return s == ((equipes.size() / 2) * nbEpreuve);
            
        }
        else
        {
            return false;
        }
    }

    /**
     * Efface tous les scores de chaques équipes
     *
     * @param equipes
     */
    public static void effacerScore(ArrayList<Equipe> equipes)
    {
        if (!equipes.isEmpty())
        {
            int[][] temp = equipes.get(0).getScores();
            int tailleTab = temp.length;
            int largeurTab = temp[0].length;

            int[] temp2 = equipes.get(0).getPartiesGagnees();

            for (Equipe e : equipes)
            {
                int[][] scoreVide = new int[tailleTab][largeurTab];
                int partieVide[] = new int[temp2.length];
                int equipesJoueesVide[] = new int[temp2.length];
                e.setScores(scoreVide);
                e.setPartiesGagnees(partieVide);
                e.setEquipesJouees(equipesJoueesVide);
            }
        }

    }

    // Accesseurs
    public String getNomsJoueur()
    {
        String nomJoueur = new String();

        for (Joueur joueur : joueurs)
        {
            nomJoueur += joueur.getPrenom() + " " + joueur.getNom() + " | ";
        }

        return nomJoueur.substring(0, nomJoueur.length() - 3).trim();
    }

    public Joueur[] getJoueurs()
    {
        return joueurs;
    }

    public int getNumero()
    {
        return numero;
    }

    public String getNumeroStr()
    {
        return Integer.toString(numero);
    }

    public void setNumero(int numero)
    {
        this.numero = numero;
    }

    public void setJoueurs(Joueur[] joueurs)
    {
        this.joueurs = joueurs;
    }

    public int[][] getScores()
    {
        return scores;
    }

    public String getScores(int indexTour, int indexEpr)
    {
        return Integer.toString(scores[indexTour][indexEpr]);
    }

    /**
     * Renvoie le cumul des scores de chaque tour de toutes les épreuves
     * @return 
     */
    public int getScoreTotal()
    {
        int scoreTot = 0;
        for (int[] score : scores)
        {
            for (int j = 0; j < score.length; j++)
            {
                scoreTot += score[j];
            }
        }

        return scoreTot;
    }
    
    /**
     * Renvoie le cumul pondéré des scores de tous les tours pour toutes les épreuves
     * Le pondération fait que chaque épreuve est le même poids dans le score final
     * @param epreuves
     * @return 
     */
    public Integer getScoreTotalPondere(final ArrayList<Epreuve> epreuves)
    {
        return getScoreTotalTourPondere(epreuves, scores.length-1);
    }
    
     /**
     * Renvoie le cumul pondéré des scores des tours selon l'index pour toutes les épreuves
     * Le pondération fait que chaque épreuve est le même poids dans le score final
     * @param epreuves
     * @param indexTour index du dernier tour souhaité por faire le calcul du scores (index 0 => premier tour)
     * @return 
     */
     public Integer getScoreTotalTourPondere(final List<Epreuve> epreuves, final int indexTour)
    {
        int ppcmEpreuve = Epreuve.getPpcmEpreuve(epreuves);
        
        int scoreTot = 0;
        // iteration sur les tours
        for(int i = 0; i <= indexTour; i++)
        {
            //iteration sur les epreuves
            for (int j = 0; j < epreuves.size(); j++)
            {
                int multiplicateurScore = ppcmEpreuve / epreuves.get(j).getNbPointGagnant();
                scoreTot += multiplicateurScore * scores[i][j];
            }
            
        }

        return scoreTot;
    }



   
    
    /**
     * Fais la somme du score de l'équipe jusqu'au tour mentionner (SI
     * getScoreTotalTour(1) exemple ajoute les scores du tour 1 et 2 : 1
     * répresente l'index du tour c'est à dire le premier tour équivaut à 0)
     *
     * @param indexTour C'est l'index du tour choisis (l'index signifie que le
     * premier tour est à l'index 0)
     * @return retour la somme total des différents tour indiqué
     */
    public int getScoreTotalTour(int indexTour)
    {
        int somme = 0;
        for (int i = 0; i <= indexTour; i++)
        {
            for (int j = 0; j < scores[i].length; j++)
            {
                somme += scores[i][j];
            }
        }
        return somme;
    }

    /**
     * Calcule le score total d'un unique tour indiqué en paramétre
     *
     * @param indexTour C'est l'index du tour choisis (l'index signifie que le
     * premier tour est à l'index 0)
     * @return renvoi le score total du tour
     */
    public int getScoreTour(int indexTour)
    {
        int somme = 0;
        for (int i = 0; i < scores[indexTour].length; i++)
        {
            somme += scores[indexTour][i];

        }
        return somme;
    }

    public void setScores(int[][] scores)
    {
        this.scores = scores;
    }

    public void setScores(int indexTour, int indexEpreuve, int score)
    {
        this.scores[indexTour][indexEpreuve] = score;
    }

    public Categorie getCategorie()
    {
        return categorie;
    }

    public void setCategorie(Categorie categorie)
    {
        this.categorie = categorie;
    }

    public int[] getPartiesGagnees()
    {
        return partiesGagnees;
    }


    /**
     * Calcul le cumul des parties gagnées du premier tour, jusqu'au tour indiqué en paramètre
     * @param indexTour indique jusqu'a quel tour calculé le cumul des parties gagnées. 0 => premier tour.
     * @return 
     */
    public int getPartiesGagneesTotalPartiel(int indexTour)
    {
        int somme = 0;
        for (int i = 0; i <= indexTour; i++)
        {
            somme += partiesGagnees[i];
        }
        return somme;
    }

    public int getPartiesGagneesTot()
    {
        int total = 0;
        for (int i = 0; i < partiesGagnees.length; i++)
        {
            total += partiesGagnees[i];
        }
        return total;
    }

    public void setPartiesGagnees(int[] partiesGagnees)
    {
        this.partiesGagnees = partiesGagnees;
    }

    public void setPartieGagnee(int indexTour, int nbPartieGagne)
    {
        this.partiesGagnees[indexTour] = nbPartieGagne;
    }

    public int[] getEquipesJouees()
    {
        return equipesJouees;
    }

    public void setEquipesJouees(int[] equipesJouees)
    {
        this.equipesJouees = equipesJouees;
    }

    public void setEquipeJouee(int indexTour, int numEquipe)
    {
        this.equipesJouees[indexTour] = numEquipe;
    }

    /**
     * @return the present
     */
    public boolean isPresent()
    {
        return present;
    }

    /**
     * @param present the present to set
     */
    public void setPresent(boolean present)
    {
        this.present = present;
    }

}
