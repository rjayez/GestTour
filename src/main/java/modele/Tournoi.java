package modele;

import ihm.principale.IHM3P;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class Tournoi
{

    private static List<Epreuve> epreuves;
    private static Sauvegarde sauvegarde;
    private static Configuration config;
    private static Impression impression;
    private static IHM3P ihm;
    private static ArrayList<Equipe> equipes;

    /**
     * @param args
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException
    {
        //Initialisation des variables
        ///Rediriger la sortie
        try (PrintStream log = new PrintStream(new FileOutputStream("error.log", true)) //		System.setErr(log);
            //		System.setOut(log);
            )
        {
            //Initialisation des variables
            config = Configuration.getInstance();
            epreuves = new ArrayList<>();
            equipes = new ArrayList<>();
            sauvegarde = Sauvegarde.getInstance();
            try
            {
                if (!config.getPrevFile().get(0).isEmpty())
                {
                    sauvegarde.openSaveFile(config.getPrevFile().get(0), equipes, epreuves);
                }
                else
                {
                    // TODO Vérifier l'utilisation de ce fichier, qu'est ce que j'ai branlé ?
                    File f = new File("testSaveXml.xml");
                    if (f.exists())
                    {
                        sauvegarde.openSaveFile("testSaveXml.xml", equipes, epreuves);
                    }
                    else
                    {
                        sauvegarde.defaultConfig(epreuves);
                    }
                }

                impression = Impression.getInstance();

                ihm = new IHM3P(equipes, sauvegarde, epreuves);
                ihm.lancerIHM();
            }
            catch (Exception e)
            {
                e.printStackTrace();

            }
        }
    }
}
