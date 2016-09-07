package IHM.fenetre;

import Modele3P.Configuration;
import Modele3P.Epreuve;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import java.util.ArrayList;

public class PreferenceIHM extends Dialog
{
	private boolean isOk;
	private Configuration result;
	private final ArrayList<Epreuve> epreuves;
	
	/**
	 * @param args
	 */
	public PreferenceIHM(Shell parent)
	{
		super(parent, 0);
		result = Configuration.getInstance();
		epreuves = new ArrayList<>();
	}
	
	public boolean open(Configuration config, ArrayList<Epreuve> epreuves)
	{
		result = config;
		this.epreuves.clear();
		this.epreuves.addAll(epreuves);
		
		Shell parent = getParent();
		Shell fenetre = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		
		fenetre.setText("Preférences");
		fenetre.setSize(500, 400);
		GridLayout gridL = new GridLayout(3, false);
		gridL.horizontalSpacing = 15;
		gridL.verticalSpacing = 8;
		
		fenetre.setLayout(gridL);
		
		createContents(fenetre, epreuves);
		
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
			epreuves.clear();
			epreuves.addAll(this.epreuves);
			config.setNbEpreuve(epreuves.size());
			config = result;
			return true;
		}
		else
		{
			return false;
		}
		
	}
	
	private void createContents(final Shell fenetre, final ArrayList<Epreuve> epreuves)
	{
		GridData gData;
		
		Label lab = new Label(fenetre, 0);
		lab.setText("Tarif par équipe");
		gData = new GridData(SWT.RIGHT, SWT.CENTER, false, false);
		lab.setLayoutData(gData);
		
		final Text tb = new Text(fenetre, 0);
		tb.setText(result.getTarif());
		tb.setSize(200, 10);
		gData = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
		gData.widthHint = 75;
		tb.setLayoutData(gData);
		
		tb.addModifyListener(arg0 -> result.setTarif(tb.getText()));
		
		lab = new Label(fenetre, 0);
		lab.setText("Nombres de joueurs par équipe");
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
		cbNbJoueur.select(result.getNbJoueurEquipe() - 1);
		
		cbNbJoueur.addSelectionListener(new SelectionListener()
		{
			
			@Override public void widgetSelected(SelectionEvent arg0)
			{
				// TODO Proposer la possibilité de mettre un nom d'équipes (ou demander dans la case d'inscription)
				result.setNbJoueurEquipe(cbNbJoueur.getSelectionIndex() + 1);
			}
			
			@Override public void widgetDefaultSelected(SelectionEvent arg0)
			{}
		});
		
		cbNbJoueur.setEnabled(false);
		
		lab = new Label(fenetre, 0);
		lab.setText("Nombres de tour");
		gData = new GridData(SWT.RIGHT, SWT.CENTER, false, false);
		lab.setLayoutData(gData);
		
		final Combo cbNbTour = new Combo(fenetre, 0);
		cbNbTour.setSize(200, 10);
		
		for (int i = 1; i < 10; i++)
		{
			cbNbTour.add(Integer.toString(i));
		}
		cbNbTour.select(result.getNbTour() - 1);
		gData = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
		gData.widthHint = 75;
		cbNbTour.setLayoutData(gData);
		
		cbNbTour.addSelectionListener(new SelectionListener()
		{
			
			@Override public void widgetSelected(SelectionEvent arg0)
			{
				result.setNbTour(cbNbTour.getSelectionIndex() + 1);
				
			}
			
			@Override public void widgetDefaultSelected(SelectionEvent arg0)
			{}
		});
		
		
		final Group groupEpreuve = new Group(fenetre, SWT.NONE);
		groupEpreuve.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		groupEpreuve.setLayout(new GridLayout(3, false));
		groupEpreuve.setText("Epreuves");
		
		for (Epreuve epr : epreuves)
		{
			createInfoEpreuve(fenetre, groupEpreuve, epr);
			
		}
		
		Button btnAdd = new Button(fenetre, SWT.PUSH);
		btnAdd.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false, 3, 1));
		btnAdd.setText("Ajouter Epreuve");
		btnAdd.addSelectionListener(new SelectionListener()
		{
			
			@Override public void widgetSelected(SelectionEvent arg0)
			{
				Epreuve epr = new Epreuve();
				PreferenceIHM.this.epreuves.add(epr);				
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
				dialog.setText("Attention");
				dialog.setMessage("La modification entraine la réinitialisation total des scores" +
						"\n Etes-vous sur ?");
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
		btnAnnuler.setText("Annuler");
		
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
						msgBox.setMessage("Le score maximum doit être supérieur ou égale à un");
						msgBox.setText("Erreur de saisie");
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
					msgBox.setMessage("La valeur saisie n'est pas un nombre");
					msgBox.setText("Erreur de saisie");
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
				if(epreuves.size()>1)
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
					dialog.setText("Attention");
					dialog.setMessage("Vous ne pouvez pas supprimer toutes les épreuves");
					dialog.open();
				}
				
			}
			
			@Override public void widgetDefaultSelected(SelectionEvent arg0)
			{}
		});
	}
}
