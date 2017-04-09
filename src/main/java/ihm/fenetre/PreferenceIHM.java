package ihm.fenetre;

import constantes.TexteIHM;
import modele.ConfigTournoi;
import modele.Configuration;
import modele.Epreuve;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import utils.GestTourUtils;

import java.util.ArrayList;
import java.util.List;

public class PreferenceIHM extends Dialog
{
	public static final String TARIF_PAR_EQUIPE = "Tarif par \u00E9quipe";
	public static final String NOMBRES_DE_JOUEURS_PAR_EQUIPE = "Nombres de joueurs par \u00E9quipe";
	public static final String NOMBRES_DE_TOUR = "Nombres de tour";
	public static final String AJOUTER_EPREUVE = "Ajouter Epreuve";
	public static final String MESSAGE_REINITIALISATION = "La modification entraine la r\u00E9initialisation total des scores" + "\n Etes-vous sur ?";
	public static final String MESSAGE_SCORE_MAX = "Le score maximum doit être sup\u00E9rieur ou \u00E9gale à un";
	public static final String LA_VALEUR_SAISIE_N_EST_PAS_UN_NOMBRE = "La valeur saisie n'est pas un nombre";
	public static final String MESSAGE_ERREUR_SUPPRESSION_EPREUVES = "Vous ne pouvez pas supprimer toutes les \u00E9preuves";

	private boolean isOk;
	private List<Epreuve> epreuves;
	private ConfigTournoi configTournoi;
	
	/**
	 * @param parent
	 */
	public PreferenceIHM(final Shell parent)
	{
		super(parent, 0);
		epreuves = new ArrayList<>();
		configTournoi = new ConfigTournoi();
	}
	
	public ConfigTournoi open(final Configuration config, final List<Epreuve> epreuves)
	{
        this.epreuves = GestTourUtils.copyEpreuves(epreuves); // deep copy


		Shell parent = getParent();
		Shell fenetre = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		
		fenetre.setText(TexteIHM.PREFERENCES);
		fenetre.setSize(500, 400);
		GridLayout gridL = new GridLayout(3, false);
		gridL.horizontalSpacing = 15;
		gridL.verticalSpacing = 8;
		
		fenetre.setLayout(gridL);
		
		createContents(fenetre, epreuves, config);
		
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
		
		if (isOk)
		{
			return configTournoi;
		}
		else
		{
			return null;
		}
		
	}
	
	private void createContents(final Shell fenetre, final List<Epreuve> epreuves, final Configuration configuration)
	{
		GridData gData;
		
		Label lab = new Label(fenetre, 0);
		lab.setText(TARIF_PAR_EQUIPE);
		gData = new GridData(SWT.RIGHT, SWT.CENTER, false, false);
		lab.setLayoutData(gData);
		
		final Text tb = new Text(fenetre, 0);
		tb.setText(configuration.getTarif());
		tb.setSize(200, 10);
		gData = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
		gData.widthHint = 75;
		tb.setLayoutData(gData);
		
		tb.addModifyListener(arg0 -> configTournoi.setTarif(tb.getText()));
		
		lab = new Label(fenetre, 0);
		lab.setText(NOMBRES_DE_JOUEURS_PAR_EQUIPE);
		gData = new GridData(SWT.RIGHT, SWT.CENTER, false, false);
		lab.setLayoutData(gData);
		
		final Combo cbNbJoueur = new Combo(fenetre, 0);
		cbNbJoueur.setSize(200, 10);
		
		gData = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
		gData.widthHint = 75;
		cbNbJoueur.setLayoutData(gData);
		for (int i = 1; i < 15; i++)
		{
			cbNbJoueur.add(Integer.toString(i));
		}
		cbNbJoueur.select(configuration.getNbJoueurEquipe() - 1);
		
		cbNbJoueur.addSelectionListener(new SelectionListener()
		{
			
			@Override public void widgetSelected(SelectionEvent arg0)
			{
				// TODO Proposer la possibilité de mettre un nom d'équipes (ou demander dans la case d'inscription)
				configTournoi.setNombreTour(cbNbJoueur.getSelectionIndex() + 1);
			}
			
			@Override public void widgetDefaultSelected(SelectionEvent arg0)
			{}
		});
		
		cbNbJoueur.setEnabled(false);
		
		lab = new Label(fenetre, 0);
		lab.setText(NOMBRES_DE_TOUR);
		gData = new GridData(SWT.RIGHT, SWT.CENTER, false, false);
		lab.setLayoutData(gData);
		
		final Combo cbNbTour = new Combo(fenetre, 0);
		cbNbTour.setSize(200, 10);
		
		for (int i = 1; i < 10; i++)
		{
			cbNbTour.add(Integer.toString(i));
		}
		cbNbTour.select(configuration.getNbTour() - 1);
		gData = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
		gData.widthHint = 75;
		cbNbTour.setLayoutData(gData);
		
		cbNbTour.addSelectionListener(new SelectionListener()
		{
			
			@Override public void widgetSelected(SelectionEvent arg0)
			{
				configTournoi.setNombreTour(cbNbTour.getSelectionIndex() + 1);
			}
			
			@Override public void widgetDefaultSelected(SelectionEvent arg0)
			{}
		});
		
		
		final Group groupEpreuve = new Group(fenetre, SWT.NONE);
		groupEpreuve.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		groupEpreuve.setLayout(new GridLayout(3, false));
		groupEpreuve.setText(TexteIHM.EPREUVES);
		
		for (Epreuve epr : epreuves)
		{
			createInfoEpreuve(fenetre, groupEpreuve, epr);
		}
		
		Button btnAdd = new Button(fenetre, SWT.PUSH);
		btnAdd.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false, 3, 1));
		btnAdd.setText(AJOUTER_EPREUVE);
		btnAdd.addSelectionListener(new SelectionListener()
		{
			
			@Override public void widgetSelected(SelectionEvent arg0)
			{
				Epreuve epr = new Epreuve();
				epreuves.add(epr);
				createInfoEpreuve(fenetre, groupEpreuve, epr);
				fenetre.pack();
			}
			
			@Override public void widgetDefaultSelected(SelectionEvent arg0)
			{}
		});
		
		Button btnOk = new Button(fenetre, SWT.PUSH);
		btnOk.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		btnOk.setText("Ok");
		
		btnOk.addSelectionListener(new SelectionListener()
		{
			
			@Override public void widgetSelected(SelectionEvent arg0)
			{
				MessageBox dialog = new MessageBox(fenetre, SWT.YES | SWT.NO);
				dialog.setText(TexteIHM.ATTENTION);
				dialog.setMessage(MESSAGE_REINITIALISATION);
				int i = dialog.open();
				if(i == SWT.YES)
				{
					isOk = true;
					fenetre.close();
				}
			}
			
			@Override public void widgetDefaultSelected(SelectionEvent arg0)
			{}
		});
		
		Button btnAnnuler = new Button(fenetre, SWT.PUSH);
		btnAnnuler.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		btnAnnuler.setText(TexteIHM.ANNULER);
		
		btnAnnuler.addSelectionListener(new SelectionListener()
		{
			
			@Override public void widgetSelected(SelectionEvent arg0)
			{
				isOk = false;
				fenetre.close();
			}
			
			@Override public void widgetDefaultSelected(SelectionEvent arg0)
			{	
				
			}
		});
		
	}
	
	private void createInfoEpreuve(final Shell fenetre, final Composite parent, final Epreuve epr)
	{
		GridData gData;
		final Text tbNomEpr = new Text(parent, SWT.SINGLE | SWT.LEAD | SWT.BORDER);
		gData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		// gData.widthHint = 250;
		tbNomEpr.setLayoutData(gData);
		tbNomEpr.setText(epr.getNom());
		tbNomEpr.addModifyListener(arg0 -> epr.setNom(tbNomEpr.getText()));
		
		final Text tbPointEpr = new Text(parent, SWT.SINGLE | SWT.LEAD | SWT.BORDER);
		gData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gData.widthHint = 50;
		tbPointEpr.setLayoutData(gData);
		tbPointEpr.setText(Integer.toString(epr.getNbPointGagnant()));
		
		tbPointEpr.addFocusListener(new FocusListener()
		{
			
			@Override public void focusLost(FocusEvent arg0)
			{
				try
				{
					Integer tbValue = Integer.parseInt(tbPointEpr.getText());
					if (tbValue < 1)
					{
						
						MessageBox msgBox = new MessageBox(fenetre);
						msgBox.setMessage(MESSAGE_SCORE_MAX);
						msgBox.setText(TexteIHM.ERREUR_DE_SAISIE);
						msgBox.open();
						
						tbPointEpr.setText("1");
						epr.setNbPointGagnant(1);
						
					}
					else
					// Valeur correct et inférieur au nombre de point gagnant
					{
						epr.setNbPointGagnant(tbValue);
					}
					
				}
				catch (Exception e)
				{
					
					MessageBox msgBox = new MessageBox(fenetre);
					msgBox.setMessage(LA_VALEUR_SAISIE_N_EST_PAS_UN_NOMBRE);
					msgBox.setText(TexteIHM.ERREUR_DE_SAISIE);
					msgBox.open();
				}
				
			}
			
			@Override public void focusGained(FocusEvent arg0)
			{}
		});
	
		final Button btSuppr = new Button(parent, SWT.PUSH);
		btSuppr.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		btSuppr.setText("-");
		btSuppr.addSelectionListener(new SelectionListener()
		{
			
			@Override public void widgetSelected(SelectionEvent arg0)
			{
				if(epreuves.size() > 1)
				{
					epreuves.remove(epr);
					tbNomEpr.dispose();
					tbPointEpr.dispose();
					btSuppr.dispose();
					parent.pack();
					fenetre.pack();
				}
				else
				{
					MessageBox dialog = new MessageBox(fenetre, SWT.OK);
					dialog.setText(TexteIHM.ATTENTION);
					dialog.setMessage(MESSAGE_ERREUR_SUPPRESSION_EPREUVES);
					dialog.open();
				}
				
			}
			
			@Override public void widgetDefaultSelected(SelectionEvent arg0)
			{}
		});
	}


}
