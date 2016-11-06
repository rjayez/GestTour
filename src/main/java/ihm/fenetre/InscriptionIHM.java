package ihm.fenetre;

import constantes.TexteIHM;
import modele.Categorie;
import modele.Configuration;
import modele.Equipe;
import modele.Joueur;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

public class InscriptionIHM extends Dialog
{

    public static final String JOUEUR_1 = "Joueur 1";
    public static final String FEMME_DEUX_POINT = "Femme :";
    public static final String PRESENT_DEUX_POINT = "Présent :";

    private enum catJoueur
    {
        nom, prenom, ville
    }

    private Configuration config;

    private Control[] listTab;
    private Equipe result;
    private boolean exitWithOk;

    public InscriptionIHM(Shell parent, int style, Configuration config)
    {
        super(parent, style);
        this.config = config;
        this.exitWithOk = false;
        int nbTab = 5 + 3 * config.getNbJoueurEquipe();
        listTab = new Control[nbTab];
        //result = new Equipe(config);
    }

    public InscriptionIHM(Shell parent, Configuration config)
    {
        this(parent, 0, config);
    }

    public Equipe open(final Equipe equipe)
    {
        result = new Equipe(equipe);
        Shell parent = getParent();
        Shell fenetre = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
        fenetre.setText(TexteIHM.INSCRIPTION_UNE_NOUVELLE_EQUIPE);
        fenetre.setSize(350, 150 + 250 * config.getNbJoueurEquipe());
        
        GridLayout gridLay = new GridLayout(5, false);
        gridLay.horizontalSpacing = 20;
        gridLay.verticalSpacing = 10;
        gridLay.marginRight = 20;
        fenetre.setLayout(gridLay);

        createContents(fenetre, equipe);

        fenetre.addListener(SWT.Close, event -> {
            if(!exitWithOk)
                result = null;
        });
        
        
        fenetre.pack();
        fenetre.open();

        Display display = parent.getDisplay();
        while (!fenetre.isDisposed())
        {
            if (!display.readAndDispatch())
            {
                display.sleep();
            }

        }

        return result;

    }

    private void createContents(final Shell fenetre, final Equipe equipe)
    {

		// TODO Prevenir quand le champs nom et prenom sont vide
        GridData gData;
        
        final Joueur[] joueurs;
        if (equipe.getJoueurs() != null)
        {
            joueurs = equipe.getJoueurs();
        }
        else
        {
            joueurs = new Joueur[config.getNbJoueurEquipe()];
            for (int i = 0; i < joueurs.length; i++)
            {
                joueurs[i] = new Joueur();
            }
        }
        @SuppressWarnings("unused")
        Label labTrich = new Label(fenetre, 0);
        Label labelJoueur1 = new Label(fenetre, SWT.CENTER);
        labelJoueur1.setText(JOUEUR_1);
        gData = new GridData(SWT.CENTER);
        labelJoueur1.setLayoutData(gData);

        // Crée les textbox ligne par ligne pour le nombre de joueur
        for (int i = 1; i < config.getNbJoueurEquipe(); i++)
        {

            Label labelJoueur2 = new Label(fenetre, SWT.CENTER);
            labelJoueur2.setText("Joueur " + (i + 1));
            gData.horizontalAlignment = SWT.CENTER;
            gData.horizontalSpan = 2;
            labelJoueur2.setLayoutData(gData);
        }
        
        Label labPrenom = new Label(fenetre, SWT.CENTER);
        labPrenom.setText(TexteIHM.PRENOM_DEUX_POINT);
        gData = new GridData(SWT.RIGHT, SWT.CENTER, false, false);
        labPrenom.setLayoutData(gData);

        ajouterTextBox(fenetre, joueurs, catJoueur.prenom, 0);

        Label labNom = new Label(fenetre, SWT.CENTER);
        labNom.setText(TexteIHM.NOM_DEUX_POINT);
        gData = new GridData(SWT.RIGHT, SWT.CENTER, false, false);
        labNom.setLayoutData(gData);

        ajouterTextBox(fenetre, joueurs, catJoueur.nom, 1);

       

        Label labVille = new Label(fenetre, SWT.CENTER);
        labVille.setText(TexteIHM.VILLE_DEUX_POINT);
        gData = new GridData(SWT.RIGHT, SWT.CENTER, false, false);
        labVille.setLayoutData(gData);

        ajouterTextBox(fenetre, joueurs, catJoueur.ville, 2);

        // Partie sélection catégorie homme
        Label labHomme = new Label(fenetre, 0);
        labHomme.setText(TexteIHM.HOMME_DEUX_POINT);
        gData = new GridData(SWT.RIGHT, SWT.CENTER, false, false);
        labHomme.setLayoutData(gData);

        Button checkHomme = new Button(fenetre, SWT.RADIO);

        int indexTab = 3 * config.getNbJoueurEquipe();
        listTab[indexTab++] = checkHomme;

        checkHomme.setSize(10, 10);

        if (result.getCategorie() == Categorie.Homme)
        {
            checkHomme.setSelection(true);
        }

        gData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1);
        checkHomme.setLayoutData(gData);
        checkHomme.addSelectionListener(new SelectionListener()
        {

            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                result.setCategorie(Categorie.Homme);
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent arg0)
            {
            }
        });

        // Partie sélection catégorie femme
        Label labFemme = new Label(fenetre, 0);
        labFemme.setText(FEMME_DEUX_POINT);
        gData = new GridData(SWT.RIGHT, SWT.CENTER, false, false);
        labFemme.setLayoutData(gData);

        Button checkFemme = new Button(fenetre, SWT.RADIO);

        listTab[indexTab++] = checkFemme;

        checkFemme.setSize(10, 10);
        gData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1);
        if (result.getCategorie() == Categorie.Femme)
        {
            checkFemme.setSelection(true);
        }
        checkFemme.setLayoutData(gData);
        checkFemme.addSelectionListener(new SelectionListener()
        {

            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                result.setCategorie(Categorie.Femme);
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent arg0)
            {
            }
        });

        // Partie sélection catégorie jeune
        Label labJeune = new Label(fenetre, 0);
        labJeune.setText(TexteIHM.JEUNE_DEUX_POINT);
        gData = new GridData(SWT.RIGHT, SWT.CENTER, false, false);
        labJeune.setLayoutData(gData);

        Button checkJeune = new Button(fenetre, SWT.RADIO);
        if (result.getCategorie() == Categorie.Jeune)
        {
            checkJeune.setSelection(true);
        }
        listTab[indexTab++] = checkJeune;

        checkJeune.setSize(10, 10);
        gData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1);
        checkJeune.setLayoutData(gData);
        checkJeune.addSelectionListener(new SelectionListener()
        {

            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                result.setCategorie(Categorie.Jeune);
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent arg0)
            {
            }
        });

        // Partie radio présence
        GridLayout gridLayoutDataPresence = new GridLayout(5, false);
        gridLayoutDataPresence.horizontalSpacing = 20;
        gridLayoutDataPresence.verticalSpacing = 10;
        gridLayoutDataPresence.marginRight = 20;
        Composite compositePresence = new Composite(fenetre, 0);
        compositePresence.setLayout(gridLayoutDataPresence);
        
        gData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 5, 1);
        compositePresence.setLayoutData(gData);
        
        Label labelPresence = new Label(compositePresence, 0);
        labelPresence.setText(PRESENT_DEUX_POINT);
        gData = new GridData(SWT.RIGHT, SWT.CENTER, false, false);
        labelPresence.setLayoutData(gData);
        
        Label labelPresenceOui = new Label(compositePresence,0);
        labelPresenceOui.setText(TexteIHM.OUI);
        gData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        labelPresenceOui.setLayoutData(gData);
        
        Button checkPresenceOui = new Button(compositePresence, SWT.RADIO);
        gData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        checkPresenceOui.setLayoutData(gData);
        checkPresenceOui.addSelectionListener(new SelectionListener()
        {

            @Override
            public void widgetSelected(SelectionEvent se)
            {
                result.setPresent(true);
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent se)
            {
                
            }
        });
        
       
        Label labelPresenceNon = new Label(compositePresence,0);
        labelPresenceNon.setText(TexteIHM.NON);
        gData = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
        labelPresenceNon.setLayoutData(gData);
    
        
        Button checkPresenceNon = new Button(compositePresence, SWT.RADIO);
        gData = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
        checkPresenceNon.setSize(10, 10);
        checkPresenceNon.setLayoutData(gData);
        checkPresenceNon.addSelectionListener(new SelectionListener()
        {

            @Override
            public void widgetSelected(SelectionEvent se)
            {
                result.setPresent(false);
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent se)
            {
         
            }
        });
        
      
        
        if(result.isPresent())
            checkPresenceOui.setSelection(true);
        else
            checkPresenceNon.setSelection(true);
        
        // Partie bouton fenetre
        Button btOk = new Button(fenetre, SWT.PUSH);

        listTab[indexTab++] = btOk;

        btOk.setText(TexteIHM.OK);
        btOk.setSize(100, 10);
        gData = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 3, 1);
        gData.widthHint = 100;
        btOk.setLayoutData(gData);
        btOk.addSelectionListener(new SelectionListener()
        {

            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                result.setJoueurs(joueurs);
                exitWithOk = true;
                fenetre.close();
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent arg0)
            {
            }
        });

        Button btAnnuler = new Button(fenetre, SWT.PUSH);

        listTab[indexTab] = btAnnuler;


        btAnnuler.setText(TexteIHM.ANNULER);
        btAnnuler.setSize(100, 10);
        gData = new GridData(SWT.CENTER, SWT.CENTER, false, false, 2, 1);
        gData.widthHint = 100;
        btAnnuler.setLayoutData(gData);
        btAnnuler.addSelectionListener(new SelectionListener()
        {

            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                exitWithOk = false;
                fenetre.close();
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent arg0)
            {
            }
        });

        fenetre.setTabList(listTab);

    }

    private void ajouterTextBox(final Shell fenetre, final Joueur[] joueurs, final catJoueur catJ, Integer indexTab)
    {

        for (int i = 0; i < joueurs.length; i++)
        {
            Text tb = new Text(fenetre, SWT.BORDER);
            tb.setSize(250, 10);
            GridData gData = new GridData(SWT.FILL);
            gData.grabExcessHorizontalSpace = true;
            gData.widthHint = 150;
            gData.horizontalSpan = 2;
            
            tb.setLayoutData(gData);
            tb.setTabs(indexTab);

            listTab[indexTab + i * 3] = tb;

            if (catJ == catJoueur.nom)
            {
                tb.setText(joueurs[i].getNom());
            }
            if (catJ == catJoueur.prenom)
            {
                tb.setText(joueurs[i].getPrenom());
            }
            if (catJ == catJoueur.ville)
            {
                tb.setText(joueurs[i].getVille());
            }

            //validation avec bouton Entrer
            Listener keyListener = event -> {
                if (SWT.CR == event.keyCode || SWT.KEYPAD_CR == event.keyCode)
                {
                    result.setJoueurs(joueurs);
                    exitWithOk = true;
                    fenetre.close();
                }
            };

            tb.addListener(SWT.KeyUp, keyListener);

            final Joueur joueur = joueurs[i];
            tb.addModifyListener(arg0 -> {
                if (catJ == catJoueur.nom)
                {
                    joueur.setNom(((Text) arg0.widget).getText());
                }
                if (catJ == catJoueur.prenom)
                {
                    joueur.setPrenom(((Text) arg0.widget).getText());
                }
                if (catJ == catJoueur.ville)
                {
                    joueur.setVille(((Text) arg0.widget).getText());
                }

            });
        }
    }
    

}
