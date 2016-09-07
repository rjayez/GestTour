package IHM.principale;

import Constantes.TexteIHM;
import IHM.fenetre.InscriptionIHM;
import IHM.fenetre.NouveauIHM;
import IHM.fenetre.PreferenceIHM;
import IHM.fenetre.ScoreIHM;
import Modele3P.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class IHM3P {

    private final ArrayList<Equipe> equipes;
    private final Sauvegarde save;
    private final Impression pdf;
    private final Configuration config;
    private final ArrayList<Epreuve> epreuves;

    private Table tableInsc;

    private boolean isCategorie;

    // Variable IHM
    private final Shell fenetre;
    private final Display display;
    private final ArrayList<Table> tablesRenc;
    private final ArrayList<Table> tablesScore;
    private ArrayList<Table> listTablesClass;
    private final ArrayList<TabItem> ongletPrincipaux; // 1 Inscription (index 0) + N Tour (indx n° tour) + 1 Classement (index size -1)
    private final ArrayList<TabItem> ongletClassement;
    private final TabFolder onglet;
    private Button btnVerrouillerInsc;
    private Button[] tabBtnVerrouillerTour;
    private boolean uneFeuilleMarqueParEquipe;
    private boolean estDonneesAJour; // Variable permettant d'indiquer que le programme est dans un état qui a des données qui ne sont pas sauvegardé

    private GridLayout gridL;
    private GridData gData;

    public IHM3P(ArrayList<Equipe> equipes, Sauvegarde save, ArrayList<Epreuve> epreuves) {
        this.equipes = equipes;
        this.save = save;
        this.pdf = Impression.getInstance();
        this.config = Configuration.getInstance();
        this.epreuves = epreuves;
        this.uneFeuilleMarqueParEquipe = true;
        display = new Display();
        fenetre = new Shell(display, SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
        fenetre.setText("GestTour");
        fenetre.setMaximized(true);
        tablesScore = new ArrayList<>();
        tablesRenc = new ArrayList<>();
        listTablesClass = new ArrayList<>();
        onglet = new TabFolder(fenetre, 0);

        boolean[] islockedScore = new boolean[config.getNbTour()];
        for (int i = 0; i < islockedScore.length; i++) {
            islockedScore[i] = false;
        }
        isCategorie = false;
        ongletPrincipaux = new ArrayList<>();
        ongletClassement = new ArrayList<>();
        tabBtnVerrouillerTour = new Button[config.getNbTour()];
    }

    public void lancerHM() {

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {

                save.enregistrer(config.getSaveFile(), equipes, epreuves);

                try {
                    File dossierSauvegardeAuto = new File(System.getProperty("user.home") + "\\temp");
                    if (!dossierSauvegardeAuto.exists()) {
                        Files.createDirectory(dossierSauvegardeAuto.toPath());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                save.enregistrer(System.getProperty("user.home") + "\\temp\\" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-YY HH-mm")) + ".slr", equipes,
                        epreuves);

                estDonneesAJour = true;
            }
        }, 2000, 300000);// Démarre au bout de 2 secondes et enregistre toutes les 5 minutes (300 secondes)

        createContents();

        fenetre.addListener(SWT.Close, this::lancerDialogExitApplication);

        fenetre.addDisposeListener(arg0 -> {
        });

        // initialisation de l'affichage des données
        initAffichage(tableInsc);

        // Lancement de la fenétre
        fenetre.open();

        // Boucle essentiel de l'IHM
        while (!fenetre.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        timer.cancel();
        fenetre.dispose();
        display.dispose();

    }

    private void initAffichage(final Table tableInsc) {

        fenetre.setText("GestTour - " + config.getSaveFile());

        affichageEquipeInsc(tableInsc);

        for (int i = 0; i < tabBtnVerrouillerTour.length; i++) {

            if (config.getTourFini()[i]) {
                tabBtnVerrouillerTour[i].setText("Tour verrouillé");
                tabBtnVerrouillerTour[i].setEnabled(false);
            } else {
                tabBtnVerrouillerTour[i].setText("Verrouiller le tour");
                tabBtnVerrouillerTour[i].setEnabled(true);
            }
        }
        creerMenuBar();
    }

    /**
     * Lance la boite de dialogue qui demande si l'on doit enregistrer le fichier en cours d'utilisation avant de quitter l'application Oui -> enregistre et quitte Non ->
     * n'enregistre pas et quitte Annuler -> ne quitte pas
     *
     * @param event event
     */
    private void lancerDialogExitApplication(Event event) {
        MessageBox dialog = new MessageBox(fenetre, SWT.YES | SWT.NO | SWT.CANCEL);

        dialog.setText("Enregistrement");
        dialog.setMessage("Voulez vous enregistrer avant de quitter ?");
        int i = dialog.open();
        if (i == SWT.YES) {
            // Enregistre et ferme l'application
            save.enregistrer(config.getSaveFile(), equipes, epreuves);
        } else if (i == SWT.CANCEL) {
            // Annule la fermeture de l'application
            event.doit = false;
        }
    }

    /**
     * Méthode qui cree un table SWT avec le nombre et le nom de colonne passés en paramétre
     *
     * @param parent  Fenetre parente
     * @param columns liste des noms des colonnes de la table
     * @return retourne la table avec les colonnes et options de base
     */
    private Table creerTable(Composite parent, ArrayList<String[]> columns) {
        Table table = new Table(parent, SWT.BORDER | SWT.FULL_SELECTION);

        // Nom de la colonne puis la largeur
        for (String[] strings : columns) {
            TableColumn tableCl = new TableColumn(table, SWT.CENTER);
            tableCl.setText(strings[0]);
            tableCl.setWidth(Integer.parseInt(strings[1]));
        }

        table.setLinesVisible(true);
        table.setHeaderVisible(true);

        GridData gDataTable = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 6);
        table.setLayoutData(gDataTable);

        return table;
    }

    /**
     * Ajoute une nouvelleEquipe au tableau avec les données rentrer dans la fenetre d'inscription
     *
     * @param equipe nouvelle équipe inscrite
     * @param table  tableau d'inscription auquel sera ajouter la nouvelle équipe
     */
    private void ajoutNouvelleEquipe(Equipe equipe, Table table) {

        ArrayList<String> infoEquipe = new ArrayList<>();
        Joueur[] joueurs = equipe.getJoueurs();
        TableItem tiEquipe = new TableItem(table, 0);

        int numEquipe = table.getItemCount();
        equipe.setNumero(numEquipe);

        equipes.add(equipe);

        infoEquipe.add(Integer.toString(numEquipe));

        for (Joueur joueur : joueurs) {
            infoEquipe.add(joueur.getNom());
            infoEquipe.add(joueur.getPrenom());
            infoEquipe.add(joueur.getVille());
        }

        infoEquipe.add(equipe.getCategorie().toString());
        infoEquipe.add(4, ""); // blanc pour la zone de séparation du tableau

        tiEquipe.setText(infoEquipe.toArray(new String[9]));

    }

    private void affichageEquipeInsc(Table table) {
        table.removeAll();
        Equipe.ordonnerEquipe(equipes);
        String[] strInfo;
        for (Equipe eq : equipes) {
            // TODO Vérifier la taille du tableau selon le nombre de joueur, surement faux pour un nombre de joueur différent de 2
            strInfo = new String[10];
            strInfo[0] = eq.getNumeroStr();
            int i = 1;
            for (Joueur j : eq.getJoueurs()) {
                strInfo[i] = j.getNom();
                strInfo[i + 1] = j.getPrenom();
                strInfo[i + 2] = j.getVille();
                i += 4;
            }

            strInfo[i - 1] = eq.getCategorie().toString();

            if (eq.isPresent()) {
                strInfo[strInfo.length - 1] = "Oui";
            } else {
                strInfo[strInfo.length - 1] = "Non";
            }

            TableItem ti = new TableItem(table, 0);
            ti.setText(strInfo);
        }

        if (config.isLockedInsc()) {
            btnVerrouillerInsc.setText("Déverrouiller l'inscription");
        } else {
            btnVerrouillerInsc.setText("Verrouiller l'inscription");
        }

    }

    /**
     * Affiche le score des équipes dans le tableau des sous-onglets Score dans les onglets Tour
     *
     * @param indexTour index du tour
     * @param table     table SWT
     */
    private void affichageEquipeScore(int indexTour, Table table) {
        String[] strInfo;
        table.removeAll();
        Equipe.ordonnerEquipe(equipes);
        for (Equipe eq : equipes) {
            strInfo = new String[2 + epreuves.size()];
            strInfo[0] = eq.getNumeroStr();
            for (int i = 0; i < epreuves.size(); i++) {
                strInfo[i + 1] = eq.getScores(indexTour, i);
            }
            strInfo[epreuves.size() + 1] = Integer.toString(eq.getPartiesGagnees()[indexTour]);
            //strInfo[epreuves.size() + 2] = eq.getScoreTotalTourPondere(epreuves, indexTour).toString();

            TableItem ti = new TableItem(table, 0);
            ti.setText(strInfo);
        }
        //Equipe.classerEquipe(equipes);
        Equipe.classerEquipeScorePondere(epreuves, equipes, config.getNbTour() - 1);

    }


    /**
     * Rempli le tableau de classement des équipes de chaque tour hors le dernier qui posséde un affichage différent
     *
     * @param indexTour index du tour (index 0 = premier tour)
     * @param table     table SWT
     */
    private void afficherClassement(int indexTour, Table table) {

        //Equipe.classerEquipeIndexTour(equipes, indexTour);
        Equipe.classerEquipeScorePondere(epreuves, equipes, indexTour);
        table.removeAll();
        String[] strInfo;
        float diviseur = Epreuve.getPpcmEpreuve(epreuves) * (indexTour + 1) * epreuves.size();
        for (Equipe eq : equipes) {
            strInfo = new String[5];
            // Colonne 1 : Numéro d'équipe
            strInfo[0] = eq.getNumeroStr();
            // Colonne 2 : Nom des joueurs de l'équipe
            strInfo[1] = eq.getNomsJoueur();
            // Colonne 3 : Nombre d'épreuve gagnées
            strInfo[2] = Integer.toString(eq.getPartiesGagneesTotalPartiel(indexTour));
            // Colonne 4 : Scores total
            strInfo[3] = Integer.toString(eq.getScoreTotalTour(indexTour));
            // Colonne 5 : Scores total pondéré
            float pourcentageScore = (eq.getScoreTotalTourPondere(epreuves, indexTour) / diviseur) * 100;
            strInfo[4] = String.format("%,.1f", pourcentageScore);

            TableItem ti = new TableItem(table, 0);
            ti.setText(strInfo);
        }
    }

    /**
     * Rempli le tableau du classement du dernier tour dans le dernier sous-onglet de l'onglet "Classement"
     *
     * @param table table SWT
     */
    private void afficherClassemmentFinal(Table table) {
        table.removeAll();
        // Equipe.classerEquipe(equipes);
        Equipe.classerEquipeScorePondere(epreuves, equipes, config.getNbTour() - 1);
        table.getColumn(1).setWidth(0);
        String[] strInfo = new String[8];
        int i = 1;

        float diviseur = Epreuve.getPpcmEpreuve(epreuves) * config.getNbTour() * epreuves.size();
        for (Equipe eq : equipes) {
            // Place | N° | Noms joueurs | Epreuves Gagnés | Score | Score pondéré | Categorie

            strInfo[0] = Integer.toString(i);

            strInfo[2] = eq.getNumeroStr();
            strInfo[3] = eq.getNomsJoueur();
            strInfo[4] = Integer.toString(eq.getPartiesGagneesTot());
            strInfo[5] = Integer.toString(eq.getScoreTotal());

            float pourcentageScore = (eq.getScoreTotalPondere(epreuves) / diviseur) * 100;
            strInfo[6] = String.format("%,.1f", pourcentageScore);
            strInfo[INDEX_COLONNE_CATEGORIE] = eq.getCategorie().toString();
            // strInfo[7] = eq.getCommentaire;

            TableItem ti = new TableItem(table, SWT.NONE);
            ti.setText(strInfo);
            i++;
        }
    }

    private static final int INDEX_COLONNE_CATEGORIE = 7;

    private void selectionCategorie(Table table, Categorie cat) {
        afficherClassemmentFinal(table);

        TableItem[] items = table.getItems();

        if (cat != Categorie.Tous) {
            // TODO essayer de ne plus vérifier avec la valeur dans le tableau (bug parce qu'une nouvelle colonne a été rajouté
            ArrayList<Integer> index = new ArrayList<>(items.length);
            for (int i = 0; i < items.length; i++) {
                if (!cat.toString().equals(items[i].getText(INDEX_COLONNE_CATEGORIE))) {
                    index.add(i);
                }

            }

            int[] tabIndex = new int[index.size()];
            int j = 0;
            for (Iterator<Integer> iterator = index.iterator(); iterator.hasNext(); j++) {
                tabIndex[j] = iterator.next();

            }

            table.remove(tabIndex);

            table.getColumn(1).setWidth(130);
            for (int i = 0; i < table.getItemCount(); i++) {
                table.getItem(i).setText(1, Integer.toString(i + 1));
            }

        }

    }

    /**
     *
     * @param indexTour index du tour actuel (0..n-1 tour)
     * @param eqModif l'équipe modifier aprés la saisie des données
     */
    //private void majScore(int indexTour, Equipe eqModif) // {
    //
    // for (int j = 0; j < equipes.size(); j++)
    // {
    // if (equipes.get(j).getNumero() == eqModif.getNumero())
    // {
    // Equipe eq = equipes.get(j);
    // eq.setScores(eqModif.getScores());
    // eq.setPartieGagnee(indexTour, eq.getPartiesGagnees()[indexTour]);
    // break;
    // }
    // }
    // }

    /**
     * Supprime une équipe indiqué de la liste et décalle tous les numéros d'équipes pour maintenir la cohérence
     *
     * @param indexEquipe index de l'équipe à supprimer
     */
    private void supprimerEquipe(int indexEquipe) {
        Equipe.ordonnerEquipe(equipes);
        equipes.remove(indexEquipe);

        for (int i = indexEquipe; i < equipes.size(); i++) {
            equipes.get(i).setNumero(i + 1);
        }
    }

    /**
     * Classe les equipes pour procéder au rencontre du tour et l'affichage dans le tableau indiqué, établit la rencontre de la premiere et derniere équipe lors du
     * premier tour, puis établit les rencontres 2 a 2 pour les autres tours AMELIORABLE algorithimiquement
     *
     * @param table     tableau dans lequel sera affiché les rencontres pour le tour
     * @param indexTour définit si c'est des rencontres pour un premier tour ou pour un n-iéme tour
     */
    private void affichageRencontreEquipe(Table table, int indexTour) {
        String[] strRenc;
        table.removeAll();

        int eqSize = equipes.size();
        if (indexTour == 0) {

            Equipe.ordonnerEquipe(equipes);
            for (int i = 0; i < ((eqSize + 1) / 2); i++) {
                strRenc = new String[5];
                if (eqSize == 1) {
                    Equipe eq = equipes.get(i);
                    strRenc[0] = eq.getNumeroStr();
                    strRenc[1] = eq.getNomsJoueur();
                    eq.getEquipesJouees()[indexTour] = 0;
                } else if (i != (eqSize - 1 - i)) {
                    Equipe eq1 = equipes.get(i);
                    strRenc[0] = eq1.getNumeroStr();
                    strRenc[1] = eq1.getNomsJoueur();

                    Equipe eq2 = equipes.get(eqSize - 1 - i);
                    strRenc[3] = eq2.getNumeroStr();
                    strRenc[4] = eq2.getNomsJoueur();
                    eq1.getEquipesJouees()[indexTour] = eq2.getNumero();
                    eq2.getEquipesJouees()[indexTour] = eq1.getNumero();
                } else // i == (size -i)
                {
                    Equipe eq = equipes.get(i);
                    strRenc[0] = eq.getNumeroStr();
                    strRenc[1] = eq.getNomsJoueur();
                    eq.getEquipesJouees()[indexTour] = 0;
                }
                TableItem ti = new TableItem(table, 0);
                ti.setText(strRenc);
            }
        } else // tour quelconque
        {
            boolean erreurRencontre = false;
            int nbRencontreOk = 0;
            int nbErreur = 0;
            // Fait la rencontre des équipe deux à deux en suivant la liste des
            // scores total
            //Equipe.classerEquipeIndexTour(equipes, indexTour - 1);
            Equipe.classerEquipeScorePondere(epreuves, equipes, indexTour - 1);

            /*
             * Algorithme 1) Test si l'équipe n'a pas d'aversaire pour se tour 2) Récupére la deuxiéme équipe à la suite tant qu'elle a une équipe adverse 3) Test si l'équipe n'a pas deja
             * rencontrer lors des précédents tour 4.1) Si ce n'est pas le cas on recupére les infos dans le tableau et affecte le nouvel adversaire au deux équipe 4.2) Si c'est le cas on prend une
             * nouvelle deuxiéme équipe se situant juste apres
             */
            do {
                if (erreurRencontre) {
                    // TODO Plantage ici avec 6 équipes et 4 tour (revoir la validation du nombre d'équipe en fonction du nombre de tour)
                    Collections.shuffle(equipes.subList(eqSize - (3 + nbErreur), eqSize));
                    erreurRencontre = false;
                    if (nbErreur < eqSize - 1) {
                        nbErreur++;

                    }
                    nbRencontreOk = 0;
                }

                for (Equipe eq : equipes) {
                    eq.getEquipesJouees()[indexTour] = 0;
                }
                for (int i = 0; i < eqSize; i++) {
                    boolean eqValable;

                    Equipe eq1 = equipes.get(i);
                    Equipe eq2;

                    // Test si l'équipe n'a pas d'aversaire pour se tour
                    if (eq1.getEquipesJouees()[indexTour] == 0) {

                        // Récupére la deuxiéme équipe à la suite tant qu'elle n'a
                        // pas d'adversaire
                        int j = i + 1;

                        do {

                            eqValable = true;
                            do {

                                try {
                                    eq2 = equipes.get(j);
                                } catch (Exception e) {
                                    eq2 = null;
                                    erreurRencontre = true;
                                    // TODO Remplacer par un throw qui envoie une exception catch plus bas 
                                    break;
                                }
                                j++;

                            } while (eq2.getEquipesJouees()[indexTour] != 0 && (j < eqSize));

                            if (erreurRencontre) {
                                break;
                            }

                            // Test si l'équipe n'a pas déjà rencontrer lors des
                            // précédents tour
                            for (int k = 0; k < indexTour; k++) {
                                if (eq1.getEquipesJouees()[k] == eq2.getNumero()) {
                                    eqValable = false;
                                    break;
                                }
                            }

                        } while (!eqValable);

                        if (erreurRencontre) {
                            break;
                        }

                        nbRencontreOk++;
                        eq1.getEquipesJouees()[indexTour] = eq2.getNumero();
                        eq2.getEquipesJouees()[indexTour] = eq1.getNumero();
                    }

                }
                // TODO Ici mettre le catch du TODO du dessus
            } while (nbRencontreOk != (eqSize / 2));

            affichageRencontre(table, indexTour);

        }

        // Attribution des épreuves aux rencontres
        TableItem[] items = table.getItems();

        int index = 0;
        int pas;
        final int nombreRencontre = items.length;
        final int nombreEpreuve = epreuves.size();
        int reste = nombreRencontre % nombreEpreuve;

        for (Epreuve epreuve : epreuves) {
            pas = nombreRencontre / nombreEpreuve;

            // si le reste est supérieur à 0, on ajoute 1 au pas afin de bien répartir les rencontres sur toutes les épreuves
            if (reste > 0) {
                pas++;
                reste--;
            }

            for (int j = 0; j < pas; j++) {
                // ajout dans le tableau du nom de l'épreuve
                items[index].setText(INDEX_COLONNE_NOM_EPREUVE, epreuve.getNom());
                // ajout du numéro de terrain, égale à j+1
                items[index].setText(INDEX_COLONNE_NUMERO_TERRAIN, String.valueOf(j + 1));
                index++;
            }
        }

    }

    private static final int INDEX_COLONNE_NUMERO_TERRAIN = 6;
    private static final int INDEX_COLONNE_NOM_EPREUVE = 5;

    private void affichageRencontre(Table table, int indexTour) {
        String[] strRenc;

        boolean[] equipeAfficher = new boolean[equipes.size()];

        for (int i = 0; i < equipeAfficher.length; i++) {
            equipeAfficher[i] = false;
        }

        for (Equipe eq : equipes) {
            if (!equipeAfficher[eq.getNumero() - 1]) {
                strRenc = new String[5];
                Equipe eq2 = getEquipe(eq.getEquipesJouees()[indexTour]);
                strRenc[0] = eq.getNumeroStr();
                strRenc[1] = eq.getNomsJoueur();

                strRenc[3] = eq2.getNumeroStr();
                strRenc[4] = eq2.getNomsJoueur();

                TableItem ti = new TableItem(table, 0);
                ti.setText(strRenc);

                equipeAfficher[eq.getNumero() - 1] = true;
                equipeAfficher[eq2.getNumero() - 1] = true;
            }
        }

    }

    /**
     * Lance la procédure d'ajout ou modification d'une équipe
     *
     * @param tableInsc table SWT d'inscription
     */
    private void ajoutModifEquipe(final Table tableInsc) {

        InscriptionIHM inscriptionDialogue = new InscriptionIHM(fenetre, config);
        int index = tableInsc.getSelectionIndex();
        // Si la ligne selectionné n'est pas une équipe déjà inscrite => nouvelle inscripion
        if (-1 == index) {
            // Si l'inscription n'est pas vérrouiller, on peut ajouter des équipes
            if (!config.isLockedInsc()) {
                // Nouvelle inscription
                Equipe equipe = new Equipe(config);
                equipe = inscriptionDialogue.open(equipe);
                if (equipe != null) {
                    ajoutNouvelleEquipe(equipe, tableInsc);
                    affichageEquipeInsc(tableInsc);
                }
            }
        } else // Modification
        {
            Equipe equipe = inscriptionDialogue.open(getEquipe(index + 1));
            if (equipe != null) {

                int indexEq = equipe.getNumero() - 1;
                equipes.set(indexEq, equipe);

                affichageEquipeInsc(tableInsc);
            }

        }

        tableInsc.deselectAll();
    }

    private void changementConfig() {
        int nbTour = config.getNbTour();
        int nbEpreuve = epreuves.size();
        for (Equipe e : equipes) {
            e.setEquipesJouees(new int[nbTour]);
            e.setPartiesGagnees(new int[nbTour]);
            e.setScores(new int[nbTour][nbEpreuve]);
        }
        config.setTourFini(new boolean[nbTour]);
        config.setLockedInsc(false);

    }

    // Fonction creation de contenu d'IHM **************************************

    /**
     * Fonction creation de contenu d'IHM
     */
    private void createContents() {

        fenetre.setLayout(new FillLayout());
        creerMenuBar();

        creerOnglet(onglet);

        creerInscription(onglet);
        creerTour(onglet);
        creerClassement(onglet);
        // Gestion des evennements***********************************

        onglet.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                int indexOnglet = onglet.getSelectionIndex();

                if (indexOnglet == 0) {
                    affichageEquipeInsc(tableInsc);
                }

                if ((equipes.size() > config.getNbTour()) && config.isLockedInsc())//Conditions d'inscription validés
                {
                    if (indexOnglet > 0 && indexOnglet <= config.getNbTour())//onglets des tours
                    {
                        if (indexOnglet != 1) {
                            if (Equipe.tourFini(equipes, epreuves.size(), indexOnglet - 2)) {
                                affichageRencontreEquipe(tablesRenc.get(indexOnglet - 1), indexOnglet - 1);

                                affichageEquipeScore(indexOnglet - 1, tablesScore.get(indexOnglet - 1));
                            } else {
                                onglet.setSelection(1);
                                MessageBox dialog = new MessageBox(fenetre, SWT.OK);
                                dialog.setText(TexteIHM.Titre.ATTENTION);
                                dialog.setMessage("Le tour précédent n'est pas terminé\n Retour sur le tour 1");
                                dialog.open();
                            }

                        } else {
                            affichageRencontreEquipe(tablesRenc.get(indexOnglet - 1), indexOnglet - 1);

                            affichageEquipeScore(indexOnglet - 1, tablesScore.get(indexOnglet - 1));

                        }

                    }
                    if (onglet.getSelectionIndex() == 1 + config.getNbTour())//onglet classement
                    {
                        //Equipe.classerEquipe(equipes);
                        Equipe.classerEquipeScorePondere(epreuves, equipes, config.getNbTour() - 1);
                        for (int i = 0; i < config.getNbTour(); i++) {
                            if (i != config.getNbTour() - 1) {
                                afficherClassement(i, listTablesClass.get(i));
                            } else {
                                afficherClassemmentFinal(listTablesClass.get(i));
                            }
                        }

                        // afficherClassFinal(tours, listTablesClass.get(listTablesClass.size() - 1));
                    }
                } else //Conditions d'inscription INvalidés
                {
                    if (!(onglet.getSelectionIndex() < 1) && !(onglet.getSelectionIndex() > config.getNbTour())) {
                        MessageBox dialog = new MessageBox(fenetre, SWT.ICON_WARNING);
                        dialog.setText(TexteIHM.Titre.ATTENTION);
                        if (!config.isLockedInsc()) {
                            onglet.setSelection(0);
                            dialog.setMessage("Il faut verrouiller l'inscription avant d'accéder au tour" + "\nRetour sur la page d'inscription");
                        } else if (equipes.size() <= config.getNbTour()) {
                            onglet.setSelection(0);
                            dialog.setMessage("Le nombre d'équipe est insuffisante pour démarrer le tournoi" + "\nRetour sur la page d'inscription");
                        }

                        dialog.open();
                    }

                }

                save.enregistrer(config.getSaveFile(), equipes, epreuves);
                estDonneesAJour = false;
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent arg0) {
            }
        });

    }

    private void creerOnglet(TabFolder onglet) {

        TabItem ongletInsc = new TabItem(onglet, SWT.NONE);
        ongletInsc.setText("Inscription");
        ongletPrincipaux.add(ongletInsc);

        for (int i = 0; i < config.getNbTour(); i++) {
            TabItem o = new TabItem(onglet, SWT.None);
            o.setText("Tour " + (i + 1));
            ongletPrincipaux.add(o);
        }

        TabItem ongletClass = new TabItem(onglet, SWT.None);
        ongletClass.setText("Classement");
        ongletPrincipaux.add(ongletClass);

    }

    private void creerMenuBar() {
        Menu menu = new Menu(fenetre, SWT.BAR);

        // Menu déroulant Fichier
        MenuItem miFichier = new MenuItem(menu, SWT.CASCADE);
        miFichier.setText("Fichier");

        Menu menuFichier = new Menu(fenetre, SWT.DROP_DOWN);
        miFichier.setMenu(menuFichier);
        MenuItem miNew = new MenuItem(menuFichier, SWT.PUSH);
        miNew.setText("Nouveau");

        miNew.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent arg0) {
                NouveauIHM nouveau = new NouveauIHM(fenetre, SWT.DIALOG_TRIM);
                if (null != nouveau.open(config, epreuves)) {
                    equipes.clear();
                    config.fichierRecent(config.getSaveFile());
                    config.setLockedInsc(false);
                    config.setTourFini(new boolean[config.getNbTour()]);
                    save.enregistrer(config.getSaveFile(), equipes, epreuves);
                    initAffichage(tableInsc);
                    onglet.setSelection(0);
                }

            }

            @Override
            public void widgetDefaultSelected(SelectionEvent arg0) {
            }
        });

        MenuItem miOuvrir = new MenuItem(menuFichier, SWT.PUSH);
        miOuvrir.setText("Ouvrir");
        miOuvrir.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                FileDialog fileDial = new FileDialog(fenetre, SWT.OPEN);

                fileDial.setFilterExtensions(new String[]{"*.slr", "*.xml"});
                fileDial.setFilterNames(new String[]{"Fichier Tournoi ", "Fichier Xml"});

                String filePath = fileDial.open();

                if (filePath != null) {
                    save.openSaveFile(filePath, equipes, epreuves);
                    config.fichierRecent(filePath);

                    resetAffichage();
                    createContents();

                    initAffichage(tableInsc);
                    onglet.setSelection(0);
                }
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent arg0) {
            }
        });

        @SuppressWarnings("unused") MenuItem miSeparator = new MenuItem(menuFichier, SWT.SEPARATOR);

        MenuItem miSave = new MenuItem(menuFichier, SWT.PUSH);
        miSave.setText("Enregistrer");
        miSave.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent arg0) {
                save.enregistrer(config.getSaveFile(), equipes, epreuves);
                estDonneesAJour = true;
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent arg0) {
            }
        });

        MenuItem miSaveAs = new MenuItem(menuFichier, SWT.PUSH);
        miSaveAs.setText("Enregistrez sous");
        miSaveAs.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent arg0) {
                FileDialog saveDial = new FileDialog(fenetre, SWT.SAVE);
                saveDial.setFileName("FichierTournoi");

                saveDial.setFilterExtensions(new String[]{"*.SLR", "*.xml"});
                saveDial.setFilterNames(new String[]{"Fichier Tournoi", "Fichier XML"});
                String saveFile = saveDial.open();
                if (saveFile != null) {

                    config.setSaveFile(saveFile);
                    config.fichierRecent(saveFile);
                    save.enregistrer(saveFile, equipes, epreuves);
                    estDonneesAJour = true;
                    fenetre.setText("TourManage - " + config.getSaveFile());

                }

            }

            @Override
            public void widgetDefaultSelected(SelectionEvent arg0) {
            }
        });


        LinkedList<String> previousFile = config.getPrevFile();

        for (final String string : previousFile) {
            if (!string.equals("")) {
                MenuItem miFicherRecent = new MenuItem(menuFichier, SWT.CASCADE);
                String fichier;
                if (string.lastIndexOf("/") != -1) {
                    fichier = string.substring(string.lastIndexOf("/"));
                } else {
                    fichier = string;
                }

                miFicherRecent.setText(fichier);
                miFicherRecent.addSelectionListener(new SelectionListener() {

                    @Override
                    public void widgetSelected(SelectionEvent arg0) {
                        save.openSaveFile(string, equipes, epreuves);
                        config.fichierRecent(string);

                        resetAffichage();
                        createContents();

                        initAffichage(tableInsc);

                    }

                    @Override
                    public void widgetDefaultSelected(SelectionEvent arg0) {
                    }
                });
            }

        }


        MenuItem miExit = new MenuItem(menuFichier, SWT.CASCADE);
        miExit.setText("Quitter");
        miExit.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                save.enregistrer(config.getSaveFile(), equipes, epreuves); // ?
                fenetre.dispose();
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent arg0) {
            }
        });

        MenuItem miSave2 = new MenuItem(menu, SWT.CASCADE);
        miSave2.setText("Enregistrer");
        miSave2.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent arg0) {
                save.enregistrer(config.getSaveFile(), equipes, epreuves);
                estDonneesAJour = true;
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent arg0) {
            }
        });

        MenuItem miOptions = new MenuItem(menu, SWT.CASCADE);
        miOptions.setText("Options");

        Menu mOptions = new Menu(fenetre, SWT.DROP_DOWN);

        MenuItem miPref = new MenuItem(mOptions, SWT.PUSH);
        miPref.setText("Préférences");
        miPref.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                PreferenceIHM prefIHM = new PreferenceIHM(fenetre);

                boolean isOk = prefIHM.open(config, epreuves);
                if (isOk) {
                    changementConfig();

                    resetAffichage();
                    createContents();
                    initAffichage(tableInsc);

                }
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent arg0) {
            }
        });

        MenuItem miReset = new MenuItem(mOptions, SWT.PUSH);
        miReset.setText("Réinitialiser");
        miReset.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent arg0) {

                MessageBox dialog = new MessageBox(fenetre, SWT.YES | SWT.NO);
                dialog.setText("Réinitialisations du tournoi !");
                dialog.setMessage("Etes vous sur de voulair réinitialiser le tournoi ?\n Ceci effacera l'intégralité du tournoi en cours");
                int i = dialog.open();

                if (i == SWT.YES) {
                    save.enregistrer(config.getSaveFile().replaceAll(".slr", "_old.slr"), equipes, epreuves); // Création d'un backup ?
                    estDonneesAJour = true;
                    equipes.clear();
                    initAffichage(tableInsc);

                }
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent arg0) {
            }
        });

        miOptions.setMenu(mOptions);

        fenetre.setMenuBar(menu);
    }

    private ArrayList<String[]> getListeRencontre(int indexTour, Table tableRencontre, boolean uneFeuilleParEquipe) {
        Equipe.ordonnerEquipe(equipes);
        ArrayList<String[]> listRenc = new ArrayList<>();
        //String[] strFeuille;
//        for (Equipe eq : equipes)
//        {
//            if (eq.getEquipesJouees()[indexTour] != 0)
//            {
//                strFeuille = new String[4];
//                strFeuille[0] = Integer.toString(indexTour + 1);
//                strFeuille[1] = eq.getNumeroStr();
//                strFeuille[2] = Integer.toString(eq.getEquipesJouees()[indexTour]);
//                strFeuille[3] =
//
//                listRenc.add(strFeuille);
//            }
//
//        }

        for (TableItem item : tableRencontre.getItems()) {
            String[] strFeuille = new String[5];
            strFeuille[0] = Integer.toString(indexTour + 1);
            strFeuille[1] = item.getText(0); // numéro d'équipe de gauche
            strFeuille[2] = item.getText(3); // numéro d'équipe de droite
            strFeuille[3] = item.getText(6); // numéro terrain
            strFeuille[4] = item.getText(5); // epreuve
            listRenc.add(strFeuille);

            //inversement pour l'autre équipe, si l'on veut une feuille par équipe
            if (uneFeuilleParEquipe) {
                strFeuille = new String[5];
                strFeuille[0] = Integer.toString(indexTour + 1);
                strFeuille[2] = item.getText(0);// numéro d'équipe de gauche
                strFeuille[1] = item.getText(3); // numéro d'équipe de droite
                strFeuille[3] = item.getText(6); // numéro terrain epreuve
                strFeuille[4] = item.getText(5); // epreuve

                listRenc.add(strFeuille);
            }
        }

        return listRenc;
    }

    private void creerInscription(TabFolder onglet) {
        GridData gData;
        Composite compInscription = new Composite(onglet, SWT.None);

        GridLayout gridL = new GridLayout(2, false);
        compInscription.setLayout(gridL);

        ArrayList<String[]> columsInscription = new ArrayList<>();
        columsInscription.add(new String[]{"N°", "35"});
        for (int i = 0; i < config.getNbJoueurEquipe(); i++) {
            columsInscription.add(new String[]{"Nom", "150"});
            columsInscription.add(new String[]{"Prenom", "150"});
            columsInscription.add(new String[]{"Ville", "150"});
            columsInscription.add(new String[]{"", "10"});
        }
        // Supprime la dernière colonne vide
        columsInscription.remove(columsInscription.size() - 1);

        columsInscription.add(new String[]{"Catégorie", "150"});

        columsInscription.add(new String[]{"Présence", "75"});

        // Tableau d'inscription
        final Table tableInsc = creerTable(compInscription, columsInscription);
        this.tableInsc = tableInsc;
        gData = new GridData(GridData.FILL, GridData.FILL, true, true, 1, 6);
        tableInsc.setLayoutData(gData);

        tableInsc.addMouseListener(new MouseListener() {
            @Override
            public void mouseUp(MouseEvent arg0) {
            }

            @Override
            public void mouseDown(MouseEvent arg0) {
            }

            @Override
            public void mouseDoubleClick(MouseEvent arg0) {
                if (config.isLockedInsc()) {
                    MessageBox dialog = new MessageBox(fenetre, SWT.OK);
                    dialog.setText(TexteIHM.Titre.ATTENTION);
                    dialog.setMessage("Les inscriptions sont verrouillées");
                    dialog.open();
                } else {
                    ajoutModifEquipe(tableInsc);
                    //Equipe.effacerScore(equipes);
                }
            }

        });

        // Label tarifs
        Label lTarif = new Label(compInscription, 0);
        lTarif.setText("Tarif par équipe :");
        gData = new GridData(SWT.CENTER, SWT.CENTER, false, false);
        gData.verticalIndent = 15;
        lTarif.setLayoutData(gData);

        lTarif = new Label(compInscription, 0);
        lTarif.setText(config.getTarif() + " Euros");
        gData = new GridData(SWT.CENTER, SWT.CENTER, false, false);
        gData.verticalIndent = 15;
        lTarif.setLayoutData(gData);

        // Bouton ajout/modification inscription
        final Button bt1 = new Button(compInscription, SWT.PUSH);
        bt1.setText("Ajouter/Modifier une équipe");
        bt1.addSelectionListener(new SelectionListener() {
            // TODO Griser le bouton quand les inscriptions sont verrouiller
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                if (config.isLockedInsc()) {
                    ouvrirDialogueTexte(TexteIHM.Titre.ATTENTION, "Les inscriptions sont verrouillées", fenetre, SWT.OK);
                } else {
                    //Equipe.effacerScore(equipes);
                    ajoutModifEquipe(tableInsc);

                }
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent arg0) {
            }
        });
        gData = new GridData(SWT.CENTER, SWT.END, false, false);
        gData.verticalIndent = 20;
        gData.widthHint = 200;
        bt1.setLayoutData(gData);

        final Button btnSuppr = new Button(compInscription, SWT.PUSH);
        btnSuppr.setText("Supprimer une équipe");
        btnSuppr.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent arg0) {
                int index = tableInsc.getSelectionIndex();
                if (index != -1 && !config.isLockedInsc()) {
                    MessageBox dialog = new MessageBox(fenetre, SWT.YES | SWT.NO);
                    dialog.setText("Suppresion équipe");
                    dialog.setMessage("Etes-vous sur de vouloir supprimer cette équipe" + "\n Cela entraine la modification des numéros des équipes");
                    int i = dialog.open();
                    if (i == SWT.YES) {
                        supprimerEquipe(index);
                        initAffichage(tableInsc);
                    }

                    save.enregistrer(config.getSaveFile(), equipes, epreuves);
                    estDonneesAJour = false;
                } else {
                    if (config.isLockedInsc()) {
                        MessageBox dialog = new MessageBox(fenetre, SWT.OK);
                        dialog.setText(TexteIHM.Titre.ATTENTION);
                        dialog.setMessage("Les inscriptions sont verrouillées");
                        dialog.open();
                    }
                }
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent arg0) {
            }
        });
        gData = new GridData(SWT.CENTER, SWT.BEGINNING, false, false);
        gData.widthHint = 200;
        btnSuppr.setLayoutData(gData);

        final Button btLock = new Button(compInscription, SWT.PUSH);
        if (config.isLockedInsc()) {
            btLock.setText("Déverrouiller l'inscription");
            bt1.setEnabled(false);
            btnSuppr.setEnabled(false);
        } else {
            btLock.setText("Verrouiller l'inscription");
            bt1.setEnabled(true);
            btnSuppr.setEnabled(true);
        }

        btLock.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent arg0) {
                if (equipes.size() > 4 && equipes.size() > config.getNbTour()) {
                    if (equipes.size() % 2 == 0) // Le nombre d'équipe doit être pair
                    {

                        if (config.isLockedInsc()) {
                            // Si un score a été saisie, cela signie que le tournoi à commencer alors on ne peut plus dévérouiller les inscriptions 
                            if (!Equipe.aScoreSaisie(equipes)) {
                                config.setLockedInsc(false);
                                bt1.setEnabled(true);
                                btnSuppr.setEnabled(true);
                                btLock.setText("Verrouiller l'inscription");

                            } else {
                                ouvrirDialogueTexte("Tournoi commencé", "Des scores ont déjà été saisie, cela signifie que le tournoi a commencé. " + "\nVous ne pouvez plus ajouter ou supprimer d'équipe", fenetre, SWT.OK);

                            }
                        } else {
                            config.setLockedInsc(true);
                            bt1.setEnabled(false);
                            btnSuppr.setEnabled(false);
                            btLock.setText("Déverrouiller l'inscription");
                        }

                    } else {
                        config.setLockedInsc(false);
                        bt1.setEnabled(true);
                        btnSuppr.setEnabled(true);
                        btLock.setText("Verrouiller l'inscription");

                        MessageBox dialog = new MessageBox(fenetre, SWT.OK);
                        dialog.setText(TexteIHM.Titre.ATTENTION);
                        dialog.setMessage("Le nombre d'équipe est impair\n Veuillez rajouter ou enlevez une équipe");
                        dialog.open();
                    }
                } else {
                    MessageBox dialog = new MessageBox(fenetre, SWT.OK);
                    dialog.setText(TexteIHM.Titre.ATTENTION);
                    dialog.setMessage("Nombre d'équipes inscrite insuffisante");
                    dialog.open();
                }

            }

            @Override
            public void widgetDefaultSelected(SelectionEvent arg0) {
            }
        });
        gData = new GridData(SWT.CENTER, SWT.BEGINNING, false, false);
        gData.widthHint = 200;
        gData.heightHint = 45;
        gData.verticalIndent = 50;
        btLock.setLayoutData(gData);

        btnVerrouillerInsc = btLock;

        // Ajout Menu Clic droit sur le tableau
        Menu mClicDroit = new Menu(fenetre, SWT.POP_UP);

        MenuItem miSuppr = new MenuItem(mClicDroit, SWT.CASCADE);
        miSuppr.setText("Supprimer");
        miSuppr.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent arg0) {
                int index;
                if ((index = tableInsc.getSelectionIndex()) != -1 && config.isLockedInsc()) {
                    MessageBox dialog = new MessageBox(fenetre, SWT.YES | SWT.NO);
                    dialog.setText("Suppresion équipe");
                    dialog.setMessage("Etes-vous sur de vouloir supprimer cette équipe" + "\n Cela entraine la modification des numéros des équipes");
                    int i = dialog.open();
                    if (i == SWT.YES) {
                        supprimerEquipe(index);
                        initAffichage(tableInsc);
                    }

                    save.enregistrer(config.getSaveFile(), equipes, epreuves);
                    estDonneesAJour = false;
                }

            }

            @Override
            public void widgetDefaultSelected(SelectionEvent arg0) {
            }
        });

        // tableInsc.setMenu(mClicDroit);
        ongletPrincipaux.get(0).setControl(compInscription);

    }

    private int ouvrirDialogueTexte(String titre, String message, Shell fenetre, int typeBouton) {
        MessageBox dialog = new MessageBox(fenetre, typeBouton);
        dialog.setText(titre);
        dialog.setMessage(message);
        return dialog.open();
    }

    private void creerTour(TabFolder onglet) {
        for (int i = 0; i < config.getNbTour(); i++) {
            final int finalIndex = i;
            TabFolder ongSubTour = new TabFolder(onglet, SWT.None);

            TabItem ongRencTour = new TabItem(ongSubTour, 0);
            ongRencTour.setText("Rencontres");

            TabItem ongScoreTour = new TabItem(ongSubTour, 0);
            ongScoreTour.setText("Scores");

            ongletPrincipaux.get(i + 1).setControl(ongSubTour);

            // **************
            // Rencontre Tour ***************
            Composite compRencTour = new Composite(ongSubTour, 0);
            gridL = new GridLayout(2, false);
            compRencTour.setLayout(gridL);

            // Titres des tableaux
            Label labRencontre = new Label(compRencTour, 0);
            labRencontre.setText("Tableau des rencontres");
            gData = new GridData(SWT.CENTER, SWT.CENTER, false, false, 2, 4);
            labRencontre.setLayoutData(gData);

            // Table rencontre
            ArrayList<String[]> columsRencontre = new ArrayList<>();
            columsRencontre.add(new String[]{"N°", "35"});
            columsRencontre.add(new String[]{"Noms", "250"});
            columsRencontre.add(new String[]{"", "10"});
            columsRencontre.add(new String[]{"N°", "35"});
            columsRencontre.add(new String[]{"Noms", "250"});
            columsRencontre.add(new String[]{"Epreuves", "250"});
            columsRencontre.add(new String[]{"N° Terrain", "100"});

            final Table tableRenc = creerTable(compRencTour, columsRencontre);
            tablesRenc.add(tableRenc);

            // Bouton d'impression
            Composite compositeRadioFeuilleMarque = new Composite(compRencTour, SWT.BORDER);

            Button btImprimer1 = new Button(compositeRadioFeuilleMarque, SWT.PUSH);
            btImprimer1.setText("Imprimer\n Feuille de Marque");
            gData = new GridData(SWT.CENTER, SWT.TOP, false, false, 2, 1);
            gData.widthHint = 200;

            btImprimer1.setLayoutData(gData);

            btImprimer1.addSelectionListener(new SelectionListener() {

                @Override
                public void widgetSelected(SelectionEvent arg0) {

                    ArrayList<String[]> listRenc = getListeRencontre(finalIndex, tableRenc, uneFeuilleMarqueParEquipe);
                    if (listRenc.size() > 0) {
                        pdf.createPdfFeuilleMarque(finalIndex + 1, listRenc, epreuves);
                    } else {
                        MessageBox dialog = new MessageBox(fenetre, SWT.OK);
                        dialog.setText("Informations");
                        dialog.setMessage("La liste des rencontres est vide");
                        dialog.open();
                    }

                }

                @Override
                public void widgetDefaultSelected(SelectionEvent arg0) {
                }
            });

            GridLayout gridLayout = new GridLayout(2, false);
            compositeRadioFeuilleMarque.setLayout(gridLayout);

            Button radioUneFeuilleParEquipe = new Button(compositeRadioFeuilleMarque, SWT.RADIO);
            gData = new GridData(SWT.LEFT, SWT.TOP, false, false);
            radioUneFeuilleParEquipe.setLayoutData(gData);
            radioUneFeuilleParEquipe.setSelection(uneFeuilleMarqueParEquipe);
            radioUneFeuilleParEquipe.addSelectionListener(new SelectionListener() {

                @Override
                public void widgetSelected(SelectionEvent se) {
                    uneFeuilleMarqueParEquipe = true;
                }

                @Override
                public void widgetDefaultSelected(SelectionEvent se) {

                }
            });
            radioUneFeuilleParEquipe.addPaintListener(pe -> {
                Button thisButton = (Button) pe.getSource();
                if (uneFeuilleMarqueParEquipe) {
                    thisButton.setSelection(true);
                } else {
                    thisButton.setSelection(false);
                }
            });

            Label labelFeuilleParEquipe = new Label(compositeRadioFeuilleMarque, SWT.LEFT);
            labelFeuilleParEquipe.setText("Une feuille par équipe");
            gData = new GridData(SWT.LEFT, SWT.TOP, false, false);
            labelFeuilleParEquipe.setLayoutData(gData);

            Button radioUneFeuilleParRencontre = new Button(compositeRadioFeuilleMarque, SWT.RADIO);
            gData = new GridData(SWT.LEFT, SWT.TOP, false, false);
            radioUneFeuilleParRencontre.setLayoutData(gData);
            radioUneFeuilleParRencontre.setSelection(!uneFeuilleMarqueParEquipe);
            radioUneFeuilleParRencontre.addSelectionListener(new SelectionListener() {

                @Override
                public void widgetSelected(SelectionEvent se) {
                    uneFeuilleMarqueParEquipe = false;
                }

                @Override
                public void widgetDefaultSelected(SelectionEvent se) {

                }
            });
            radioUneFeuilleParRencontre.addPaintListener(pe -> {
                Button thisButton = (Button) pe.getSource();
                if (!uneFeuilleMarqueParEquipe) {
                    thisButton.setSelection(true);
                } else {
                    thisButton.setSelection(false);
                }
            });

            Label labelFeuilleParRencontre = new Label(compositeRadioFeuilleMarque, SWT.LEFT);
            labelFeuilleParRencontre.setText("Une feuille par rencontre");
            gData = new GridData(SWT.LEFT, SWT.TOP, false, false);
            labelFeuilleParRencontre.setLayoutData(gData);

            Button btImprimer2 = new Button(compRencTour, SWT.PUSH);
            btImprimer2.setText("Imprimer\n Feuille Rencontre");
            gData = new GridData(SWT.FILL, SWT.FILL, false, false);
            //gData.widthHint = 200;
            btImprimer2.setLayoutData(gData);

            btImprimer2.addSelectionListener(new SelectionListener() {

                @Override
                public void widgetSelected(SelectionEvent arg0) {

                    ArrayList<String[]> listRenc = getListeRencontre(finalIndex, tableRenc, true);
                    if (listRenc.size() > 0) {
                        pdf.createPdfRencontre(finalIndex + 1, tableRenc);
                    } else {
                        MessageBox dialog = new MessageBox(fenetre, SWT.OK);
                        dialog.setText("Informations");
                        dialog.setMessage("La liste des rencontres est vide");
                        dialog.open();
                    }

                }

                @Override
                public void widgetDefaultSelected(SelectionEvent arg0) {
                }
            });

            ongRencTour.setControl(compRencTour);

            // Score Tour ***********************************
            Composite compScoreTour = new Composite(ongSubTour, 0);

            compScoreTour.setLayout(gridL);

            Label labScore = new Label(compScoreTour, 0);
            labScore.setText("Tableau des scores");
            gData = new GridData(SWT.CENTER, SWT.CENTER, false, false, 2, 1);
            labScore.setLayoutData(gData);

            // Table Score
            ArrayList<String[]> columsScore = new ArrayList<>();
            columsScore.add(new String[]{"N°", "35"});
            for (Epreuve epr : epreuves) {
                columsScore.add(new String[]{epr.getNom(), "125"});
            }
            columsScore.add(new String[]{"Epreuves Gagnees", "100"});

            final Table tableScore = creerTable(compScoreTour, columsScore);
            tablesScore.add(tableScore);

            tableScore.addMouseListener(new MouseListener() {
                @Override
                public void mouseUp(MouseEvent arg0) {
                }

                @Override
                public void mouseDown(MouseEvent arg0) {
                }

                @Override
                public void mouseDoubleClick(MouseEvent arg0) {
                    if ((finalIndex == 0 && !config.getTourFini()[finalIndex]) || (finalIndex != 0 && !config.getTourFini()[finalIndex] && config.getTourFini()[finalIndex - 1])) {
                        int index;
                        if ((index = tableScore.getSelectionIndex()) != -1) {
                            Equipe.ordonnerEquipe(equipes);
                            Equipe eq1 = equipes.get(index);
                            Equipe eq2 = getEquipe(eq1.getEquipesJouees()[finalIndex]);

                            Equipe previousEq1 = new Equipe(eq1);
                            Equipe previousEq2 = new Equipe(eq2);

                            if (eq2 != null) {
                                ScoreIHM scoreIHM = new ScoreIHM(fenetre, epreuves);
                                boolean result = scoreIHM.open(finalIndex, eq1, eq2);

                                if (result) {
                                    affichageEquipeScore(finalIndex, tableScore);
                                    save.enregistrer(config.getSaveFile(), equipes, epreuves);
                                    estDonneesAJour = false;
                                } else {
                                    // On remet les scores à l'état avant ouverture de la fenêtre
                                    eq1.setScores(previousEq1.getScores());
                                    eq2.setScores(previousEq2.getScores());
                                    affichageEquipeScore(finalIndex, tableScore);
                                    //save.enregistrer(config.getSaveFile(), equipes, epreuves);

                                }
                            } else {
                                ouvrirDialogueTexte(TexteIHM.Titre.ERREUR, "L'équipe n'a pas d'adversaire pour ce tour.", fenetre, SWT.OK);

                            }

                        }
                    } else {
                        MessageBox msgBox = new MessageBox(fenetre, SWT.OK);
                        msgBox.setText(TexteIHM.Titre.ERREUR);
                        if (config.getTourFini()[finalIndex]) {
                            msgBox.setMessage("Le tour est fini, vous ne pouvez plus modifier les scores.");
                        } else {
                            msgBox.setMessage("Le tour précédent n'est pas fini. Assurez-vous qu'il est verrouillé.");
                        }
                        msgBox.open();
                    }
                }
            });

            Button btImprimerScore = new Button(compScoreTour, SWT.PUSH);
            btImprimerScore.setText("Imprimer\n Score Tour");
            gData = new GridData(SWT.CENTER, SWT.TOP, false, false);
            gData.widthHint = 200;
            btImprimerScore.setLayoutData(gData);

            btImprimerScore.addSelectionListener(new SelectionListener() {

                @Override
                public void widgetSelected(SelectionEvent se) {
                    pdf.createPdfScoreTour(tableScore, getListeRencontre(finalIndex, tableRenc, true), finalIndex);
                }

                @Override
                public void widgetDefaultSelected(SelectionEvent se) {

                }
            });

            final Button btVerrouillerTour = new Button(compScoreTour, SWT.PUSH);
            if (config.getTourFini()[finalIndex]) {
                btVerrouillerTour.setText("Tour verrouillé");
                btVerrouillerTour.setEnabled(false);
            } else {
                btVerrouillerTour.setText("Verrouiller le tour");
            }
            gData = new GridData(SWT.CENTER, SWT.TOP, false, false);
            gData.widthHint = 200;
            btVerrouillerTour.setLayoutData(gData);
            btVerrouillerTour.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(SelectionEvent se) {
                    if (Equipe.tourFini(equipes, epreuves.size(), finalIndex)) {
                        MessageBox msgBox = new MessageBox(fenetre, SWT.YES | SWT.NO);
                        msgBox.setText(TexteIHM.Titre.ATTENTION);
                        msgBox.setMessage("Êtes-vous sûre de vouloir verrouiller le tour ? Les scores ne seront plus modifiables après.");
                        int result = msgBox.open();

                        if (result == SWT.YES) {
                            config.getTourFini()[finalIndex] = true;
                            btVerrouillerTour.setText("Tour verrouillé");
                            btVerrouillerTour.setEnabled(false);
                        }
                    } else {
                        ouvrirDialogueTexte(TexteIHM.Titre.ERREUR, "Le tour n'est pas fini.", fenetre, SWT.OK);

                    }

                }

            });

            tabBtnVerrouillerTour[finalIndex] = btVerrouillerTour;

            ongScoreTour.setControl(compScoreTour);

        }

    }

    private void creerClassement(TabFolder onglet) {
        gridL = new GridLayout(2, false);

        TabFolder ongSubClas = new TabFolder(onglet, 0);

        for (int i = 0; i < config.getNbTour(); i++) {
            TabItem ongSubClass = new TabItem(ongSubClas, 0);
            if (i != config.getNbTour() - 1) {
                ongSubClass.setText("Tour " + (i + 1));
            } else {
                ongSubClass.setText("Tour " + (i + 1) + " / Final");
            }
            ongletClassement.add(ongSubClass);
        }

        ongletPrincipaux.get(ongletPrincipaux.size() - 1).setControl(ongSubClas);

        ArrayList<String[]> columsClassement = new ArrayList<>();
        columsClassement.add(new String[]{"N°", "35"});
        columsClassement.add(new String[]{"Noms des joueurs", "300"});
        columsClassement.add(new String[]{"Epreuves Gagnées", "140"});
        columsClassement.add(new String[]{"Score", "75"});
        columsClassement.add(new String[]{"% Score", "75"});

        final ArrayList<Table> listTablesClass = new ArrayList<>();

        for (int i = 0; i < config.getNbTour(); i++) {

            Composite compClass = new Composite(ongSubClas, 0);
            compClass.setLayout(gridL);

            if (i == config.getNbTour() - 1) {
                columsClassement.add(0, new String[]{"Place", "75"});
                columsClassement.add(1, new String[]{"Place Catégorie", "0"});
                columsClassement.add(new String[]{"Catégorie", "150"});
                columsClassement.add(new String[]{"Commentaires", "150"});
            }

            Label labClass = new Label(compClass, SWT.CENTER);
            if (i == config.getNbTour() - 1) {
                labClass.setText("Classement Tour " + (i + 1) + " / Final ");

            } else {
                labClass.setText("Classement Tour " + (i + 1));
            }

            gData = new GridData(SWT.CENTER, SWT.CENTER, false, false, 2, 1);
            labClass.setLayoutData(gData);
            final Table tableClass = creerTable(compClass, columsClassement);

            if (i == config.getNbTour() - 1) {

                Button btImprimer = new Button(compClass, SWT.PUSH);
                btImprimer.setText("Imprimer");

                gData = new GridData(SWT.CENTER, SWT.CENTER, false, false);
                btImprimer.setLayoutData(gData);

                btImprimer.addSelectionListener(new SelectionListener() {

                    @Override
                    public void widgetSelected(SelectionEvent arg0) {
                        pdf.createPdfClassement(tableClass, isCategorie);
                    }

                    @Override
                    public void widgetDefaultSelected(SelectionEvent arg0) {
                    }
                });

                Group grpCat = new Group(compClass, 0);
                grpCat.setText("Catégorie");
                gridL = new GridLayout(1, false);
                grpCat.setLayout(gridL);

                String[] categorie = {"Tous", "Homme", "Femme", "Jeune"};

                for (final String string : categorie) {
                    Button radioCat = new Button(grpCat, SWT.RADIO);
                    radioCat.setText(string);
                    radioCat.setSize(100, 10);

                    if ("Tous".equals(string)) {
                        radioCat.setSelection(true);
                        isCategorie = false;
                    }

                    radioCat.addSelectionListener(new SelectionListener() {

                        @Override
                        public void widgetSelected(SelectionEvent arg0) {

                            isCategorie = true;
                            selectionCategorie(tableClass, Categorie.valueOf(string));
                        }

                        @Override
                        public void widgetDefaultSelected(SelectionEvent arg0) {
                        }
                    });

                }

                ongSubClas.addSelectionListener(new SelectionAdapter() {

                    @Override
                    public void widgetSelected(SelectionEvent se) {
                        TabFolder tabFolder = (TabFolder) se.getSource();
                        System.out.println("onglet : " + tabFolder.getSelectionIndex());
                        // super.widgetSelected(se); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public void widgetDefaultSelected(SelectionEvent se) {
                        TabFolder tabFolder = (TabFolder) se.getSource();
                        System.out.println("onglet : " + tabFolder.getSelectionIndex());
                        super.widgetDefaultSelected(se); //To change body of generated methods, choose Tools | Templates.
                    }

                });

            }

            listTablesClass.add(tableClass);

            ongletClassement.get(i).setControl(compClass);

        }

        this.listTablesClass = listTablesClass;
    }

    private void resetAffichage() {
        TabItem[] tItems = onglet.getItems();

        for (TabItem tItem : tItems) {
            tItem.dispose();
        }

        ongletClassement.clear();
        ongletPrincipaux.clear();

        tablesRenc.clear();
        tablesScore.clear();
        listTablesClass.clear();
        tabBtnVerrouillerTour = new Button[config.getNbTour()];
    }

    private Equipe getEquipe(int numero) {

        for (Equipe eq : equipes) {
            if (eq.getNumero() == numero) {
                return eq;
            }
        }
        return null;
    }

}