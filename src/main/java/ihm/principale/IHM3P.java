package ihm.principale;

import constantes.TexteIHM;
import ihm.fenetre.InscriptionEquipeIHM;
import ihm.fenetre.NouveauIHM;
import ihm.fenetre.PreferenceIHM;
import ihm.fenetre.ScoreIHM;
import modele.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import utils.InterfaceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

import static ihm.fenetre.PreferenceIHM.MESSAGE_REINITIALISATION;

public class IHM3P {

    public static final String TOUR_VERROUILLE = "Tour verrouill\u00E9";
    public static final String VERROUILLER_LE_TOUR = "Verrouiller le tour";
    public static final String MESSAGE_DEMANDE_SAVE_AVANT_EXIT = "Voulez vous enregistrer avant de quitter ?";
    public static final String DEVERROUILLER_INSCRIPTION = "D\u00E9verrouiller l'inscription";
    public static final String VERROUILLER_INSCRIPTION = "Verrouiller l'inscription";
    public static final String FORMAT_NOMBRE_1_DECIMAL = "%,.1f";
    public static final String MESSAGE_TOUR_PRECEDENT_NON_TERMINE = "Le tour pr\u00E9c\u00E9dent n'est pas termin\u00E9\n Retour sur le tour 1";
    public static final String MESSAGE_TOUR_PRECEDENT_NON_VERROUILLE = "Il faut verrouiller l'inscription avant d'acc\u00E9der au tour" + "\nRetour sur la page d'inscription";
    public static final String MESSAGE_NOMBRE_EQUIPE_INSUFFISANTE = "Le nombre d'\u00E9quipe est insuffisante pour d\u00E9marrer le tournoi" + "\nRetour sur la page d'inscription";
    public static final String SLASH = "/";
    public static final String MESSAGE_REINITIALISATION_TOURNOI = "R\u00E9initialisations du tournoi !";
    public static final String COMMENTAIRES = "Commentaires";
    public static final String PLACE = "Place";
    public static final String EPREUVES_GAGNEES = "Epreuves Gagn\u00E9es";
    public static final String PLACE_CATEGORIE = "Place Cat\u00E9gorie";
    public static final String POURCENTAGE_SCORE = "% Score";
    public static final String NOMS_DES_JOUEURS = "Noms des joueurs";
    public static final String NUMERO = "N\u00b0";
    public static final String TOUR_ESPACE = "Tour ";
    public static final String MESSAGE_TOUR_NON_FINI = "Le tour n'est pas fini.";
    public static final String MESSAGE_CONFIRMATION_VERROUILLAGE_TOUR = "\u00cates-vous s\u00fbr de vouloir verrouiller le tour ? Les scores ne seront plus modifiables après.";
    public static final String IMPRIMER_SCORE_TOUR = "Imprimer\n Score Tour";
    public static final String MESSAGE_TOUR_FINI_VERROUILLAGE_SCORE = "Le tour est fini, vous ne pouvez plus modifier les scores.";
    public static final String MESSAGE_TOUR_PRECEDENT_NON_FINI = "Le tour pr\u00E9c\u00E9dent n'est pas fini. Assurez-vous qu'il est verrouill\u00E9.";
    public static final String MESSAGE_ERREUR_EQUIPE_SEULE = "L'\u00E9quipe n'a pas d'adversaire pour ce tour.";
    public static final String TABLEAU_DES_SCORES = "Tableau des scores";
    public static final String MESSAGE_LISTE_RENCONTRE_VIDE = "La liste des rencontres est vide";
    public static final String IMPRIMER_FEUILLE_RENCONTRE = "Imprimer\n Feuille Rencontre";
    public static final String UNE_FEUILLE_PAR_RENCONTRE = "Une feuille par rencontre";
    public static final String UNE_FEUILLE_PAR_EQUIPE = "Une feuille par \u00E9quipe";
    public static final String IMPRIMER_FEUILLE_DE_MARQUE = "Imprimer\n Feuille de Marque";
    public static final String NUMERO_TERRAIN = "N\u00b0 Terrain";
    public static final String TABLEAU_DES_RENCONTRES = "Tableau des rencontres";
    public static final String MESSAGE_CONFIRMATION_SUPPRESSION_EQUIPE = "\u00cates-vous s\u00fbr de vouloir supprimer cette \u00E9quipe" + "\n Cela entraine la modification des " +
            "num\u00E9ros des \u00E9quipes";
    public static final String SUPPRESION_EQUIPE = "Suppresion \u00E9quipe";
    public static final String NOMBRE_EQUIPES_INSCRITE_INSUFFISANTE = "Nombre d'\u00E9quipes inscrite insuffisante";
    public static final String MESSAGE_NOMBRE_EQUIPE_IMPAIR = "Le nombre d'\u00E9quipe est impair\n Veuillez rajouter ou enlevez une \u00E9quipe";
    public static final String TOURNOI_COMMENCE = "Tournoi commenc\u00E9";
    public static final String MESSAGE_TOURNOI_COMMENCE = "Des scores ont d\u00E9jà \u00E9t\u00E9 saisie, cela signifie que le tournoi a commenc\u00E9. " + "\nVous ne pouvez plus ajouter ou supprimer d'\u00E9quipe";
    public static final String MESSAGE_INSCRIPTION_VERROUILLEES = "Les inscriptions sont verrouill\u00E9es";
    public static final String SUPPRIMER_UNE_EQUIPE = "Supprimer une \u00E9quipe";
    public static final String AJOUTER_MODIFIER_UNE_EQUIPE = "Ajouter/Modifier une \u00E9quipe";
    public static final String TARIF_PAR_EQUIPE = "Tarif par \u00E9quipe :";
    public static final String EUROS = "Euros";
    public static final String FICHIER_TOURNOI = "Fichier Tournoi";
    public static final String FICHIER_XML = "Fichier Xml";
    public static final String FICHIER_TOURNOI_SANS_ESPACE = "FichierTournoi";
    public static final String MESSAGE_CONFIRMATION_REINITIALISATION_TOURNOI = "\u00cates-vous s\u00fbr de voulair r\u00E9initialiser le tournoi ?\n Ceci effacera " +
            "l'int\u00E9gralit\u00E9 " +
            "du tournoi en cours";
    public static final String FIN_FICHIER_OLD_SLR = "_old.slr";

    private final List<Equipe> equipes;
    private final Sauvegarde save;
    private final Impression pdf;
    private final Configuration config;
    private List<Epreuve> epreuves;

    private Table tableInsc;

    private boolean isCategorie;

    // Variable ihm
    private final Shell fenetre;
    private final Display display;
    private final List<Table> tablesRenc;
    private final List<Table> tablesScore;
    private List<Table> listTablesClass;
    private final List<TabItem> ongletPrincipaux; // 1 Inscription (index 0) + N Tour (indx n° tour) + 1 Classement (index size -1)
    private final List<TabItem> ongletClassement;
    private final TabFolder onglet;
    private Button btnVerrouillerInsc;
    private Button[] tabBtnVerrouillerTour;
    private boolean uneFeuilleMarqueParEquipe;
    private boolean estDonneesAJour; // Variable permettant d'indiquer que le programme est dans un état qui a des données qui ne sont pas sauvegardé

    private GridLayout gridL;
    private GridData gData;

    public IHM3P(List<Equipe> equipes, Sauvegarde save, List<Epreuve> epreuves) {
        this.equipes = equipes;
        this.save = save;
        this.pdf = Impression.getInstance();
        this.config = Configuration.getInstance();
        this.epreuves = epreuves;
        this.uneFeuilleMarqueParEquipe = true;
        display = new Display();
        fenetre = new Shell(display, SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
        fenetre.setText(TexteIHM.GEST_TOUR);
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

    public void lancerIHM() {

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
                save.enregistrer(System.getProperty("user.home") + "\\temp\\" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-YY HH-mm")) + TexteIHM.EXTENSION_SLR, equipes, epreuves);

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

        // Boucle essentiel de l'ihm
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
                tabBtnVerrouillerTour[i].setText(TOUR_VERROUILLE);
                tabBtnVerrouillerTour[i].setEnabled(false);
            } else {
                tabBtnVerrouillerTour[i].setText(VERROUILLER_LE_TOUR);
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
        int i = InterfaceUtils.ouvrirDialogueTexte(TexteIHM.ENREGISTREMENT, MESSAGE_DEMANDE_SAVE_AVANT_EXIT, fenetre, SWT.YES | SWT.NO | SWT.CANCEL);
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
    private Table creerTable(Composite parent, List<String[]> columns) {
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
        infoEquipe.add(4, TexteIHM.CHAINE_VIDE); // blanc pour la zone de séparation du tableau

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
                strInfo[i] = WordUtils.capitalizeFully(j.getNom());
                strInfo[i + 1] = WordUtils.capitalizeFully(j.getPrenom());
                strInfo[i + 2] = WordUtils.capitalizeFully(j.getVille());
                i += 4;
            }

            strInfo[i - 1] = eq.getCategorie().toString();

            if (eq.isPresent()) {
                strInfo[strInfo.length - 1] = TexteIHM.OUI;
            } else {
                strInfo[strInfo.length - 1] = TexteIHM.NON;
            }

            TableItem ti = new TableItem(table, 0);
            ti.setText(strInfo);
        }

        if (config.isLockedInsc()) {
            btnVerrouillerInsc.setText(DEVERROUILLER_INSCRIPTION);
        } else {
            btnVerrouillerInsc.setText(VERROUILLER_INSCRIPTION);
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
            strInfo[4] = String.format(FORMAT_NOMBRE_1_DECIMAL, pourcentageScore);

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
            strInfo[6] = String.format(FORMAT_NOMBRE_1_DECIMAL, pourcentageScore);
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
                strRenc[1] = WordUtils.capitalizeFully(eq.getNomsJoueur());

                strRenc[3] = eq2.getNumeroStr();
                strRenc[4] = WordUtils.capitalizeFully(eq2.getNomsJoueur());

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

        InscriptionEquipeIHM inscriptionDialogue = new InscriptionEquipeIHM(fenetre, config);
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

    private void changementConfig(final ConfigTournoi configTournoi) {
        config.setNbEpreuve(configTournoi.getEpreuves().size());
        config.setTarif(configTournoi.getTarif());
        config.setNbTour(configTournoi.getNombreTour());
    }

    private void resetScoreEquipes() {
        for (Equipe e : equipes) {
            e.setEquipesJouees(new int[config.getNbTour()]);
            e.setPartiesGagnees(new int[config.getNbTour()]);
            e.setScores(new int[config.getNbTour()][config.getNbEpreuve()]);
        }
        config.setLockedInsc(false);
        config.setTourFini(new boolean[config.getNbTour()]);
    }

    // Fonction creation de contenu d'ihm **************************************

    /**
     * Fonction creation de contenu d'ihm
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
                                InterfaceUtils.ouvrirDialogueTexte(TexteIHM.Titre.ATTENTION, MESSAGE_TOUR_PRECEDENT_NON_TERMINE, fenetre, SWT.OK);
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
                }
                else //Conditions d'inscription INvalidés
                {
                    if (!(onglet.getSelectionIndex() < 1) && !(onglet.getSelectionIndex() > config.getNbTour())) {
                        if (!config.isLockedInsc()) {
                            onglet.setSelection(0);
                            InterfaceUtils.ouvrirDialogueTexte(TexteIHM.Titre.ATTENTION, MESSAGE_TOUR_PRECEDENT_NON_VERROUILLE, fenetre, SWT.ICON_WARNING);

                        } else if (equipes.size() <= config.getNbTour()) {
                            onglet.setSelection(0);
                            InterfaceUtils.ouvrirDialogueTexte(TexteIHM.Titre.ATTENTION, MESSAGE_NOMBRE_EQUIPE_INSUFFISANTE, fenetre, SWT.ICON_WARNING);

                        }
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
        ongletInsc.setText(TexteIHM.INSCRIPTION);
        ongletPrincipaux.add(ongletInsc);

        for (int i = 0; i < config.getNbTour(); i++) {
            TabItem o = new TabItem(onglet, SWT.None);
            o.setText(TOUR_ESPACE + (i + 1));
            ongletPrincipaux.add(o);
        }

        TabItem ongletClass = new TabItem(onglet, SWT.None);
        ongletClass.setText(TexteIHM.CLASSEMENT);
        ongletPrincipaux.add(ongletClass);

    }

    private void creerMenuBar() {
        Menu menu = new Menu(fenetre, SWT.BAR);

        // Menu déroulant Fichier
        MenuItem miFichier = new MenuItem(menu, SWT.CASCADE);
        miFichier.setText(TexteIHM.FICHIER);

        Menu menuFichier = new Menu(fenetre, SWT.DROP_DOWN);
        miFichier.setMenu(menuFichier);
        MenuItem miNew = new MenuItem(menuFichier, SWT.PUSH);
        miNew.setText(TexteIHM.NOUVEAU);

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
        miOuvrir.setText(TexteIHM.OUVRIR);
        miOuvrir.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                FileDialog fileDial = new FileDialog(fenetre, SWT.OPEN);

                fileDial.setFilterExtensions(new String[]{TexteIHM.PATTERN_EXTENSION_SLR, TexteIHM.PATTERN_EXTENTION_XML});
                fileDial.setFilterNames(new String[]{FICHIER_TOURNOI, FICHIER_XML});

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
        miSave.setText(TexteIHM.ENREGISTRER);
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
        miSaveAs.setText(TexteIHM.ENREGISTREZ_SOUS);
        miSaveAs.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent arg0) {
                FileDialog saveDial = new FileDialog(fenetre, SWT.SAVE);
                saveDial.setFileName(FICHIER_TOURNOI_SANS_ESPACE);

                saveDial.setFilterExtensions(new String[]{TexteIHM.PATTERN_EXTENSION_SLR, TexteIHM.PATTERN_EXTENTION_XML});
                saveDial.setFilterNames(new String[]{IHM3P.FICHIER_TOURNOI, IHM3P.FICHIER_XML});
                saveDial.setFilterNames(new String[]{IHM3P.FICHIER_TOURNOI, IHM3P.FICHIER_XML});
                String saveFile = saveDial.open();
                if (saveFile != null) {

                    config.setSaveFile(saveFile);
                    config.fichierRecent(saveFile);
                    save.enregistrer(saveFile, equipes, epreuves);
                    estDonneesAJour = true;
                    fenetre.setText("GestTour" + " - " + config.getSaveFile());

                }

            }

            @Override
            public void widgetDefaultSelected(SelectionEvent arg0) {
            }
        });


        LinkedList<String> previousFiles = config.getPrevFile();

        for (final String previousFile : previousFiles) {
            if (StringUtils.isNotBlank(previousFile)) {
                MenuItem miFicherRecent = new MenuItem(menuFichier, SWT.CASCADE);
                String fichier;
                if (previousFile.lastIndexOf(SLASH) != -1) {
                    fichier = previousFile.substring(previousFile.lastIndexOf(SLASH));
                } else {
                    fichier = previousFile;
                }

                miFicherRecent.setText(fichier);
                miFicherRecent.addSelectionListener(new SelectionListener() {

                    @Override
                    public void widgetSelected(SelectionEvent arg0) {
                        save.openSaveFile(previousFile, equipes, epreuves);
                        config.fichierRecent(previousFile);

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
        miExit.setText(TexteIHM.QUITTER);
        miExit.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                save.enregistrer(config.getSaveFile(), equipes, epreuves); // TODO ???
                fenetre.dispose();
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent arg0) {
            }
        });

        MenuItem miSave2 = new MenuItem(menu, SWT.CASCADE);
        miSave2.setText(TexteIHM.ENREGISTRER);
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
        miOptions.setText(TexteIHM.OPTIONS);

        Menu mOptions = new Menu(fenetre, SWT.DROP_DOWN);

        MenuItem miPref = new MenuItem(mOptions, SWT.PUSH);
        miPref.setText(TexteIHM.PREFERENCES);
        miPref.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                PreferenceIHM prefIHM = new PreferenceIHM(fenetre);

                ConfigTournoi result = prefIHM.open(config, epreuves);
                if (result != null) {

                    if(doitReinitialiserTournoi(result))
                    {
                        int i = InterfaceUtils.ouvrirDialogueTexte(TexteIHM.ATTENTION, MESSAGE_REINITIALISATION, fenetre, SWT.YES | SWT.NO);
                        if(i == SWT.YES)
                        {
                            resetScoreEquipes();
                        }
                    }
                    epreuves = result.getEpreuves();
                    changementConfig(result);
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
        miReset.setText(TexteIHM.REINITIALISER);
        miReset.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent arg0) {

                int i = InterfaceUtils.ouvrirDialogueTexte(MESSAGE_REINITIALISATION_TOURNOI, MESSAGE_CONFIRMATION_REINITIALISATION_TOURNOI, fenetre, SWT.YES | SWT.NO);

                if (i == SWT.YES) {
                    save.enregistrer(config.getSaveFile().replaceAll(TexteIHM.EXTENSION_SLR, FIN_FICHIER_OLD_SLR), equipes, epreuves); // Création d'un backup ?
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

    private List<String[]> getListeRencontre(int indexTour, Table tableRencontre, boolean uneFeuilleParEquipe) {
        Equipe.ordonnerEquipe(equipes);
        List<String[]> listRenc = new ArrayList<>();


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

    /**
     * Si les epreuves, nombre de joueur par equipe ou le nombre de tour est different de la configuration actuelle alors on renvoie vrai
     * @param configTournoi
     * @return
     */
    boolean doitReinitialiserTournoi(final ConfigTournoi configTournoi){
        return !(configTournoi.getEpreuves().equals(epreuves)
                && configTournoi.getNombreJoueurEquipe() == config.getNbJoueurEquipe()
                && configTournoi.getNombreTour() == config.getNbTour());
    }

    private void creerInscription(TabFolder onglet) {
        GridData gData;
        Composite compInscription = new Composite(onglet, SWT.None);

        GridLayout gridL = new GridLayout(2, false);
        compInscription.setLayout(gridL);

        ArrayList<String[]> columsInscription = new ArrayList<>();
        columsInscription.add(new String[]{NUMERO, "35"});
        for (int i = 0; i < config.getNbJoueurEquipe(); i++) {
            columsInscription.add(new String[]{TexteIHM.NOM, "150"});
            columsInscription.add(new String[]{TexteIHM.PRENOM, "150"});
            columsInscription.add(new String[]{TexteIHM.VILLE, "150"});
            columsInscription.add(new String[]{TexteIHM.CHAINE_VIDE, "10"});
        }
        // Supprime la dernière colonne vide
        columsInscription.remove(columsInscription.size() - 1);

        columsInscription.add(new String[]{TexteIHM.CATEGORIE, "150"});

        columsInscription.add(new String[]{TexteIHM.PRESENCE, "75"});

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
                    InterfaceUtils.ouvrirDialogueTexte(TexteIHM.Titre.ATTENTION, MESSAGE_INSCRIPTION_VERROUILLEES , fenetre, SWT.OK);
                } else {
                    ajoutModifEquipe(tableInsc);
                    //Equipe.effacerScore(equipes);
                }
            }

        });

        // Label tarifs
        Label lTarif = new Label(compInscription, 0);
        lTarif.setText(TARIF_PAR_EQUIPE);
        gData = new GridData(SWT.CENTER, SWT.CENTER, false, false);
        gData.verticalIndent = 15;
        lTarif.setLayoutData(gData);

        lTarif = new Label(compInscription, 0);
        lTarif.setText(config.getTarif() + " " + EUROS);
        gData = new GridData(SWT.CENTER, SWT.CENTER, false, false);
        gData.verticalIndent = 15;
        lTarif.setLayoutData(gData);

        // Bouton ajout/modification inscription
        final Button bt1 = new Button(compInscription, SWT.PUSH);
        bt1.setText(AJOUTER_MODIFIER_UNE_EQUIPE);
        bt1.addSelectionListener(new SelectionListener() {
            // TODO Griser le bouton quand les inscriptions sont verrouiller
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                if (config.isLockedInsc()) {
                    InterfaceUtils.ouvrirDialogueTexte(TexteIHM.Titre.ATTENTION, MESSAGE_INSCRIPTION_VERROUILLEES, fenetre, SWT.OK);
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
        btnSuppr.setText(SUPPRIMER_UNE_EQUIPE);
        btnSuppr.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent arg0) {
                int index = tableInsc.getSelectionIndex();
                if (index != -1 && !config.isLockedInsc()) {
                    int i = InterfaceUtils.ouvrirDialogueTexte(SUPPRESION_EQUIPE, IHM3P.MESSAGE_CONFIRMATION_SUPPRESSION_EQUIPE, fenetre, SWT.YES | SWT.NO);
                    if (i == SWT.YES) {
                        supprimerEquipe(index);
                        initAffichage(tableInsc);
                    }

                    save.enregistrer(config.getSaveFile(), equipes, epreuves);
                    estDonneesAJour = false;
                } else {
                    if (config.isLockedInsc()) {
                        InterfaceUtils.ouvrirDialogueTexte(TexteIHM.Titre.ATTENTION, MESSAGE_INSCRIPTION_VERROUILLEES, fenetre, SWT.OK);
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
            btLock.setText(DEVERROUILLER_INSCRIPTION);
            bt1.setEnabled(false);
            btnSuppr.setEnabled(false);
        } else {
            btLock.setText(VERROUILLER_INSCRIPTION);
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
                                btLock.setText(VERROUILLER_INSCRIPTION);

                            } else {
                                InterfaceUtils.ouvrirDialogueTexte(TOURNOI_COMMENCE, MESSAGE_TOURNOI_COMMENCE, fenetre, SWT.OK);

                            }
                        } else {
                            config.setLockedInsc(true);
                            bt1.setEnabled(false);
                            btnSuppr.setEnabled(false);
                            btLock.setText(DEVERROUILLER_INSCRIPTION);
                        }

                    } else {
                        config.setLockedInsc(false);
                        bt1.setEnabled(true);
                        btnSuppr.setEnabled(true);
                        btLock.setText(VERROUILLER_INSCRIPTION);

                        InterfaceUtils.ouvrirDialogueTexte(TexteIHM.Titre.ATTENTION, MESSAGE_NOMBRE_EQUIPE_IMPAIR, fenetre, SWT.OK);
                    }
                } else {
                    InterfaceUtils.ouvrirDialogueTexte(TexteIHM.Titre.ATTENTION, NOMBRE_EQUIPES_INSCRITE_INSUFFISANTE, fenetre, SWT.OK);
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
        miSuppr.setText(TexteIHM.SUPPRIMER);
        miSuppr.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent arg0) {
                int index;
                if ((index = tableInsc.getSelectionIndex()) != -1 && config.isLockedInsc()) {
                    int i = InterfaceUtils.ouvrirDialogueTexte(SUPPRESION_EQUIPE, MESSAGE_CONFIRMATION_SUPPRESSION_EQUIPE, fenetre, SWT.YES | SWT.NO);
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

    private void creerTour(TabFolder onglet) {
        for (int i = 0; i < config.getNbTour(); i++) {
            final int finalIndex = i;
            TabFolder ongSubTour = new TabFolder(onglet, SWT.None);

            TabItem ongRencTour = new TabItem(ongSubTour, 0);
            ongRencTour.setText(TexteIHM.RENCONTRES);

            TabItem ongScoreTour = new TabItem(ongSubTour, 0);
            ongScoreTour.setText(TexteIHM.SCORES);

            ongletPrincipaux.get(i + 1).setControl(ongSubTour);

            // **************
            // Rencontre Tour ***************
            Composite compRencTour = new Composite(ongSubTour, 0);
            gridL = new GridLayout(2, false);
            compRencTour.setLayout(gridL);

            // Titres des tableaux
            Label labRencontre = new Label(compRencTour, 0);
            labRencontre.setText(TABLEAU_DES_RENCONTRES);
            gData = new GridData(SWT.CENTER, SWT.CENTER, false, false, 2, 4);
            labRencontre.setLayoutData(gData);

            // Table rencontre
            ArrayList<String[]> columsRencontre = new ArrayList<>();
            columsRencontre.add(new String[]{NUMERO, "35"});
            columsRencontre.add(new String[]{TexteIHM.NOMS, "250"});
            columsRencontre.add(new String[]{TexteIHM.CHAINE_VIDE, "10"});
            columsRencontre.add(new String[]{NUMERO, "35"});
            columsRencontre.add(new String[]{TexteIHM.NOMS, "250"});
            columsRencontre.add(new String[]{TexteIHM.EPREUVES, "250"});
            columsRencontre.add(new String[]{NUMERO_TERRAIN, "100"});

            final Table tableRenc = creerTable(compRencTour, columsRencontre);
            tablesRenc.add(tableRenc);

            // Bouton d'impression
            Composite compositeRadioFeuilleMarque = new Composite(compRencTour, SWT.BORDER);

            Button btImprimer1 = new Button(compositeRadioFeuilleMarque, SWT.PUSH);
            btImprimer1.setText(IMPRIMER_FEUILLE_DE_MARQUE);
            gData = new GridData(SWT.CENTER, SWT.TOP, false, false, 2, 1);
            gData.widthHint = 200;

            btImprimer1.setLayoutData(gData);

            btImprimer1.addSelectionListener(new SelectionListener() {

                @Override
                public void widgetSelected(SelectionEvent arg0) {

                    List<String[]> listRenc = getListeRencontre(finalIndex, tableRenc, uneFeuilleMarqueParEquipe);
                    if (listRenc.size() > 0) {
                        pdf.createPdfFeuilleMarque(finalIndex + 1, listRenc, epreuves);
                    } else {
                        InterfaceUtils.ouvrirDialogueTexte(TexteIHM.INFORMATIONS, MESSAGE_LISTE_RENCONTRE_VIDE, fenetre, SWT.OK);
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
            labelFeuilleParEquipe.setText(UNE_FEUILLE_PAR_EQUIPE);
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
            labelFeuilleParRencontre.setText(UNE_FEUILLE_PAR_RENCONTRE);
            gData = new GridData(SWT.LEFT, SWT.TOP, false, false);
            labelFeuilleParRencontre.setLayoutData(gData);

            Button btImprimer2 = new Button(compRencTour, SWT.PUSH);
            btImprimer2.setText(IMPRIMER_FEUILLE_RENCONTRE);
            gData = new GridData(SWT.FILL, SWT.FILL, false, false);
            //gData.widthHint = 200;
            btImprimer2.setLayoutData(gData);

            btImprimer2.addSelectionListener(new SelectionListener() {

                @Override
                public void widgetSelected(SelectionEvent arg0) {

                    List<String[]> listRenc = getListeRencontre(finalIndex, tableRenc, true);
                    if (listRenc.size() > 0) {
                        pdf.createPdfRencontre(finalIndex + 1, tableRenc);
                    } else {
                        InterfaceUtils.ouvrirDialogueTexte(TexteIHM.INFORMATIONS, MESSAGE_LISTE_RENCONTRE_VIDE, fenetre, SWT.OK);
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
            labScore.setText(TABLEAU_DES_SCORES);
            gData = new GridData(SWT.CENTER, SWT.CENTER, false, false, 2, 1);
            labScore.setLayoutData(gData);

            // Table Score
            ArrayList<String[]> columsScore = new ArrayList<>();
            columsScore.add(new String[]{NUMERO, "35"});
            for (Epreuve epr : epreuves) {
                columsScore.add(new String[]{epr.getNom(), "125"});
            }
            columsScore.add(new String[]{IHM3P.EPREUVES_GAGNEES, "100"});

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
                                InterfaceUtils.ouvrirDialogueTexte(TexteIHM.Titre.ERREUR, MESSAGE_ERREUR_EQUIPE_SEULE, fenetre, SWT.OK);
                            }

                        }
                    } else {
                        if (config.getTourFini()[finalIndex]) {
                            InterfaceUtils.ouvrirDialogueTexte(TexteIHM.Titre.ERREUR, MESSAGE_TOUR_FINI_VERROUILLAGE_SCORE, fenetre, SWT.OK);
                        } else {
                            InterfaceUtils.ouvrirDialogueTexte(TexteIHM.Titre.ERREUR, MESSAGE_TOUR_PRECEDENT_NON_FINI, fenetre, SWT.OK);
                        }
                    }
                }
            });

            Button btImprimerScore = new Button(compScoreTour, SWT.PUSH);
            btImprimerScore.setText(IMPRIMER_SCORE_TOUR);
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
                btVerrouillerTour.setText(TOUR_VERROUILLE);
                btVerrouillerTour.setEnabled(false);
            } else {
                btVerrouillerTour.setText(VERROUILLER_LE_TOUR);
            }
            gData = new GridData(SWT.CENTER, SWT.TOP, false, false);
            gData.widthHint = 200;
            btVerrouillerTour.setLayoutData(gData);
            btVerrouillerTour.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(SelectionEvent se) {
                    if (Equipe.tourFini(equipes, epreuves.size(), finalIndex)) {
                        int result = InterfaceUtils.ouvrirDialogueTexte(TexteIHM.Titre.ATTENTION, MESSAGE_CONFIRMATION_VERROUILLAGE_TOUR, fenetre, SWT.YES | SWT.NO);

                        if (result == SWT.YES) {
                            config.getTourFini()[finalIndex] = true;
                            btVerrouillerTour.setText(TOUR_VERROUILLE);
                            btVerrouillerTour.setEnabled(false);
                        }
                    } else {
                        InterfaceUtils.ouvrirDialogueTexte(TexteIHM.Titre.ERREUR, MESSAGE_TOUR_NON_FINI, fenetre, SWT.OK);

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
                ongSubClass.setText(TOUR_ESPACE + (i + 1));
            } else {
                ongSubClass.setText(TOUR_ESPACE + (i + 1) + " / Final");
            }
            ongletClassement.add(ongSubClass);
        }

        ongletPrincipaux.get(ongletPrincipaux.size() - 1).setControl(ongSubClas);

        List<String[]> columsClassement = new ArrayList<>();
        columsClassement.add(new String[]{NUMERO, "35"});
        columsClassement.add(new String[]{NOMS_DES_JOUEURS, "300"});
        columsClassement.add(new String[]{EPREUVES_GAGNEES, "140"});
        columsClassement.add(new String[]{TexteIHM.SCORE, "75"});
        columsClassement.add(new String[]{POURCENTAGE_SCORE, "75"});

        final List<Table> listTablesClass = new ArrayList<>();

        for (int i = 0; i < config.getNbTour(); i++) {

            Composite compClass = new Composite(ongSubClas, 0);
            compClass.setLayout(gridL);

            if (i == config.getNbTour() - 1) {
                columsClassement.add(0, new String[]{PLACE, "75"});
                columsClassement.add(1, new String[]{PLACE_CATEGORIE, "0"});
                columsClassement.add(new String[]{TexteIHM.CATEGORIE, "150"});
                columsClassement.add(new String[]{COMMENTAIRES, "150"});
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
                btImprimer.setText(TexteIHM.IMPRIMER);

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
                grpCat.setText(TexteIHM.CATEGORIE);
                gridL = new GridLayout(1, false);
                grpCat.setLayout(gridL);

                String[] categorie = {TexteIHM.TOUS, TexteIHM.HOMME, TexteIHM.FEMME, TexteIHM.JEUNE};

                for (final String string : categorie) {
                    Button radioCat = new Button(grpCat, SWT.RADIO);
                    radioCat.setText(string);
                    radioCat.setSize(100, 10);

                    if (TexteIHM.TOUS.equals(string)) {
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
