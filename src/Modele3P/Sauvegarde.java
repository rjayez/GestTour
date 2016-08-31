package Modele3P;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class Sauvegarde
{
	private static Sauvegarde instance;
	private Element racine;
	private Document doc;
    private Configuration config;
	
	// private String savePath;
	public static Sauvegarde getInstance()
        {
            if(instance == null)
                instance = new Sauvegarde();
            
            return instance;
        }
        
	private Sauvegarde()
	{}
	
	public void openSaveFile(String fichier, ArrayList<Equipe> equipes, ArrayList<Epreuve> epreuves)
	{

        config = Configuration.getInstance();
		config.setSaveFile(fichier);
		File f = new File(fichier);
		
		if (f.exists())
		{
			equipes.clear();
			epreuves.clear();
			racine = new Element("Configuration");
			doc = new Document(racine);
			try
			{
				SAXBuilder sxb = new SAXBuilder();
				try
				{
					doc = sxb.build(new File(fichier));
				}
				catch (JDOMException | IOException e)
				{
				}
				
				racine = doc.getRootElement();
				
				Element options = racine.getChild("Options");
				
				/*
				 * Element eleSavePath = options.getChild("savePath"); config.setCheminConfigActuel(eleSavePath.getText());
				 */
				
				Element option = options.getChild("nbEpreuve");
				config.setNbEpreuve(Integer.parseInt(option.getText()));
				
				option = options.getChild("isLocked");
				config.setLockedInsc(Boolean.parseBoolean(option.getText()));
				
				option = options.getChild("nbJoueurEquipe");
				config.setNbJoueurEquipe(Integer.parseInt(option.getText()));
				
				option = options.getChild("nbTour");
				config.setNbTour(Integer.parseInt(option.getText()));
				
				option = options.getChild("tarif");
				config.setTarif(option.getText());
                
                config.setTourFini(new boolean[config.getNbTour()]);
                
                option = options.getChild("tourFini");
				int i = 0;
                for (Iterator<Element> iterator = option.getChildren().iterator(); iterator.hasNext(); i++)
                {
                    Element ele = iterator.next();
                    config.setTourFini(Boolean.parseBoolean(ele.getText()), i);
                }
				
				// Recuperation des epreuves
				List<Element> eleEpreuves = racine.getChildren("Epreuve");
				i = 0;
				for (Iterator<Element> iterator = eleEpreuves.iterator(); iterator.hasNext(); i++)
				{
					Epreuve epr = new Epreuve();
					Element eleEpreuve = iterator.next();
					epr.setNom(eleEpreuve.getChildText("nom"));
					epr.setNbPointGagnant(Integer.parseInt(eleEpreuve.getChildText("pointGagnant")));
					epreuves.add(epr);
				}
				
				// Recuperation des equipes
				
				List<Element> eleEquipes = racine.getChildren("Equipe");
				
                for (Element eleEquipe : eleEquipes)
                {
                    // recuperations des joueurs
                    List<Element> EleJoueurs = eleEquipe.getChildren("joueur");
                    Joueur joueurs[] = new Joueur[config.getNbJoueurEquipe()];
                    int k = 0;
                    for (Iterator<Element> iterator3 = EleJoueurs.iterator(); iterator3.hasNext(); k++)
                    {
                        Element eleJoueur = iterator3.next();
                        
                        Joueur joueur = new Joueur();
                        joueur.setNom(eleJoueur.getChildText("nom"));
                        joueur.setPrenom(eleJoueur.getChildText("prenom"));
                        joueur.setVille(eleJoueur.getChildText("ville"));
                        
                        joueurs[k] = joueur;
                        
                    }
                    
                    // récuperation des scores
                    int[][] scores = new int[config.getNbTour()][config.getNbEpreuve()];
                    
                    List<Element> EleTours = eleEquipe.getChildren("Tour");
                    
                    i = 0;
                    for (Iterator<Element> iterator = EleTours.iterator(); iterator.hasNext(); i++)
                    {
                        Element eleTour = iterator.next();
                        
                        List<Element> eleScores = eleTour.getChildren("score");
                        int l = 0;
                        for (Iterator<Element> iterator4 = eleScores.iterator(); iterator4.hasNext(); l++)
                        {
                            Element eleScore = iterator4.next();
                            
                            scores[i][l] = Integer.parseInt(eleScore.getText());
                        }
                        
                    }
                    
                    // récuperation des equipes joue
                    int[] numEqJoue = new int[config.getNbTour()];
                    List<Element> eleEqJoue = eleEquipe.getChildren("equipeJouee");
                    int p = 0;
                    for (Iterator<Element> iterator5 = eleEqJoue.iterator(); iterator5.hasNext(); p++)
                    {
                        Element element = iterator5.next();
                        numEqJoue[p] = Integer.parseInt(element.getText());
                        
                    }
                    
                    List<Element> elePartieGagne = eleEquipe.getChildren("partieGagnee");
                    int[] partieGagne = new int[config.getNbTour()];
                    int i2 = 0;
                    for (Iterator<Element> iterator = elePartieGagne.iterator(); iterator.hasNext(); i2++)
                    {
                        Element elePG = iterator.next();
                        partieGagne[i2] = Integer.parseInt(elePG.getText());
                        
                    }
                    
                    Categorie cat = Categorie.valueOf(eleEquipe.getChildText("categorie"));
                    boolean present = Boolean.parseBoolean(eleEquipe.getChildText("present"));
                    int numero = Integer.parseInt(eleEquipe.getAttributeValue("num"));
                    
                    equipes.add(new Equipe(numero, joueurs, cat, scores, partieGagne, numEqJoue, present));
                }
				
			}
			catch (Exception e)
			{
				
				defaultConfig(equipes, epreuves);
				e.printStackTrace();
			}
		}
		else
		{
			defaultConfig(equipes, epreuves);
		}
		
	}
	
	public void enregistrer(String fichier, ArrayList<Equipe> equipes, ArrayList<Epreuve> epreuves)
	{

		racine = new Element("Configuration");
		doc = new Document(racine);
		// Les options
		Element eleOptions = new Element("Options");
		
		ArrayList<Element> listElemOpt = new ArrayList<>();
		
		Element eleNbEpr = new Element("nbEpreuve");
		eleNbEpr.setText(Integer.toString(epreuves.size()));
		Element eleNbJoueurEq = new Element("nbJoueurEquipe");
		eleNbJoueurEq.setText(Integer.toString(config.getNbJoueurEquipe()));
		Element eleNbTour = new Element("nbTour");
		eleNbTour.setText(Integer.toString(config.getNbTour()));
		Element eleTarif = new Element("tarif");
		eleTarif.setText(config.getTarif());
		Element eleIsLocked = new Element("isLocked");
		eleIsLocked.setText(Boolean.toString(config.isLockedInsc()));
        
        Element eleTourFini = new Element("tourFini");
        for (int i = 0; i < config.getNbTour(); i++)
        {
            Element ele = new Element("tour" + String.valueOf(i+1));
            ele.setText(Boolean.toString(config.getTourFini()[i]));
            eleTourFini.addContent(ele);
        }
		
		listElemOpt.add(eleNbEpr);
		listElemOpt.add(eleNbJoueurEq);
		listElemOpt.add(eleNbTour);
		listElemOpt.add(eleTarif);
		listElemOpt.add(eleIsLocked);
        listElemOpt.add(eleTourFini);
		
		eleOptions.addContent(listElemOpt);
		
		racine.addContent(eleOptions);
		
        // Les epreuves
        for (Epreuve epreuve : epreuves)
        {
            Element eleEpr = new Element("Epreuve");
            Element eleNom = new Element("nom");
            eleNom.setText(epreuve.getNom());
            Element elePtsGagnant = new Element("pointGagnant");
            elePtsGagnant.setText(Integer.toString(epreuve.getNbPointGagnant()));
            eleEpr.addContent(eleNom);
            eleEpr.addContent(elePtsGagnant);
            racine.addContent(eleEpr);
        }
		
		// Les tours
		
		// Les equipes
		
		for (Equipe equipe : equipes)
		{
			Element eleEquipe = new Element("Equipe");
			eleEquipe.setAttribute("num", equipe.getNumeroStr());
			
			// les joueurs
			Joueur[] joueurs = equipe.getJoueurs();
            for (Joueur joueur : joueurs)
            {
                Element eleJoueur = new Element("joueur");
                Element eleNomJoueur = new Element("nom");
                eleNomJoueur.setText(joueur.getNom());
                eleJoueur.addContent(eleNomJoueur);
                Element elePrenomJoueur = new Element("prenom");
                elePrenomJoueur.setText(joueur.getPrenom());
                eleJoueur.addContent(elePrenomJoueur);
                Element eleVilleJoueur = new Element("ville");
                eleVilleJoueur.setText(joueur.getVille());
                eleJoueur.addContent(eleVilleJoueur);
                eleEquipe.addContent(eleJoueur);
            }
			
			int[][] scores = equipe.getScores();
            for (int[] score : scores)
            {
                Element eleTour = new Element("Tour");
                for (int j = 0; j < score.length; j++)
                {
                    Element eleScore = new Element("score");
                    eleScore.setText(Integer.toString(score[j]));
                    eleTour.addContent(eleScore);
                }
                eleEquipe.addContent(eleTour);
            }
			
			Element eleCat = new Element("categorie");
			eleCat.setText(equipe.getCategorie().toString());
			eleEquipe.addContent(eleCat);
            
            Element elementPresent = new Element("present");
            elementPresent.setText(String.valueOf(equipe.isPresent()));
            eleEquipe.addContent(elementPresent);
			
			int[] numEqJoue = equipe.getEquipesJouees();
			for (int num : numEqJoue)
			{
				Element eleEqJoue = new Element("equipeJouee");
				eleEqJoue.setText(Integer.toString(num));
				
				eleEquipe.addContent(eleEqJoue);
			}
			
			int[] partiesGagnees = equipe.getPartiesGagnees();
			for (int partiesGagnee : partiesGagnees) {
				Element elePartieGagnee = new Element("partieGagnee");
				elePartieGagnee.setText(Integer.toString(partiesGagnee));
				eleEquipe.addContent(elePartieGagnee);
			}
			
			racine.addContent(eleEquipe);
			
		}
		
		//affiche();
		
		XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
		try
		{
			sortie.output(doc, new FileOutputStream(fichier));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}
	
	public void defaultConfig(ArrayList<Equipe> equipes, ArrayList<Epreuve> epreuves)
	{
		config = Configuration.getInstance();
		
		int[] scoreGagnant = { 33, 11, 11 };
		String[] nomEpreuve = { "Ping Pong", "Palet", "Petanque" };
		
		for (int i = 0; i < nomEpreuve.length; i++)
		{
			epreuves.add(new Epreuve(nomEpreuve[i], scoreGagnant[i]));
		}
		
	}
	
}
