package Modele3P;

import java.util.Collections;
import java.util.LinkedList;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;


public class Configuration
{

    private static Configuration instance;
    private int nbEpreuve;
    private int nbJoueurEquipe;
    private int nbTour;
    private String tarif;
    private String saveFile;
    private LinkedList<String> prevFile;
    private boolean isLockedInsc;
    private boolean[] tourFini;

    public static Configuration getInstance()
    {
        if (instance == null)
        {
            instance = new Configuration();
        }

        return instance;
    }

    private Configuration(String saveFile, int nbEpreuve, int nbJoueurEquipe, int nbTour, String tarif, boolean isLocked, boolean[] tourFini)
    {
        this.isLockedInsc = isLocked;
        this.tourFini = tourFini;
        this.nbEpreuve = nbEpreuve;
        this.nbJoueurEquipe = nbJoueurEquipe;
        this.nbTour = nbTour;
        this.tarif = tarif;
        this.saveFile = saveFile;
        this.prevFile = new LinkedList<>();
        try
        {
            getPreviousFile();
        }
        catch (SecurityException | BackingStoreException e)
        {
            e.printStackTrace();
        }
    }

    private Configuration()
    {
        this("defaut.xml", 3, 2, 3, "6.0", false, new boolean[3]);
    }

    private void getPreviousFile() throws SecurityException, BackingStoreException
    {
        Preferences pref = Preferences.userRoot().node("gesttour");

        String val = pref.get("prevfile", "");

        if (val.compareTo("") == 0)
        {
            pref.put("prevfile", " ; ; ; ;");
            pref.flush();
            for (int i = 0; i < 4; i++)
            {
                prevFile.add("");
            }

        }
        else
        {
            String[] values = val.split(";");
            if (values.length > 0)
            {
                for (int i = 0; i < 4; i++)
                {
                    prevFile.add(values[i].trim());
                }
            }

        }

    }

    /**
     * Réorganisation de la liste des fichiers récent avec le chemin du fichier
     * passé en paramétre
     *
     */
    public void fichierRecent(String fichier)
    {
        boolean present = false;
        int i = 0;
        for (String s : prevFile)
        {
            if (s.equals(fichier))
            {
                present = true;
                break;
            }
            i++;
        }

        if (present)
        {
            Collections.swap(prevFile, 0, i);
        }
        else
        {
            prevFile.addFirst(fichier);
            prevFile.removeLast();
        }

        Preferences pref = Preferences.userRoot().node("gesttour");

        String save = "";
        for (String s : prevFile)
        {
            save += s + " ;";
        }

        pref.put("prevfile", save);
        try
        {
            pref.flush();
        }
        catch (BackingStoreException e)
        {

            e.printStackTrace();
        }
    }

    // Accesseurs
    public LinkedList<String> getPrevFile()
    {
        return prevFile;
    }

    public int getNbEpreuve()
    {
        return nbEpreuve;
    }

    public void setNbEpreuve(int nbEpreuve)
    {
        this.nbEpreuve = nbEpreuve;
    }

    public int getNbJoueurEquipe()
    {
        return nbJoueurEquipe;
    }

    public void setNbJoueurEquipe(int nbJoueurEquipe)
    {
        this.nbJoueurEquipe = nbJoueurEquipe;
    }

    public int getNbTour()
    {
        return nbTour;
    }

    public void setNbTour(int nbTour)
    {
        this.nbTour = nbTour;
    }

    public String getTarif()
    {
        return tarif;
    }

    public void setTarif(String tarif)
    {
        this.tarif = tarif;
    }

    public String getSaveFile()
    {
        return saveFile;
    }

    public void setSaveFile(String saveFile)
    {
        this.saveFile = saveFile;
    }

    public boolean isLockedInsc()
    {
        return isLockedInsc;
    }

    public void setLockedInsc(boolean isLockedInsc)
    {
        this.isLockedInsc = isLockedInsc;
    }

    /**
     * @return the tourFini
     */
    public boolean[] getTourFini()
    {
        return tourFini;
    }

    /**
     * @param tourFini the tourFini to set
     */
    public void setTourFini(boolean[] tourFini)
    {
        this.tourFini = tourFini;
    }
    
    public void setTourFini(boolean fini, int index)
    {
        this.tourFini[index] = fini;
    }

}
