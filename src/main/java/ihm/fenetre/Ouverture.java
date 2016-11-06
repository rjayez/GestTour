package ihm.fenetre;

import constantes.TexteIHM;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

public class Ouverture{

	public static final String CHOISISSEZ_UN_TOURNOI = "Choisissez un tournoi :";
	public static final String NOUVEAU_TOURNOI = "Nouveau tournoi...";

	public Ouverture(Shell parent) {
		parent.setSize(350, 300);
		GridLayout gridL = new GridLayout(2, false);
		GridData gData;
		parent.setLayout(gridL);
		
		Label lab = new Label(parent, 0);
		lab.setText(CHOISISSEZ_UN_TOURNOI);
		gData = new GridData(SWT.CENTER, SWT.CENTER, false, false, 2, 1);
		lab.setLayoutData(gData);
		
		List listFichier = new List(parent, SWT.BORDER);
		
		listFichier.setSize(300, 200);

		// TODO Mais c'est quoi cette liste ?!?
		listFichier.add(NOUVEAU_TOURNOI);
		listFichier.add("Fichier1");
		listFichier.add("Fichier2");
		listFichier.add("Fichier3");
		listFichier.add("Fichier4");
		listFichier.add("");listFichier.add("");listFichier.add("");
		
		gData = new GridData(SWT.CENTER, SWT.CENTER, true, true, 2, 1);
		listFichier.setLayoutData(gData);
		
		Button button = new Button(parent, SWT.PUSH);
		button.setText(TexteIHM.OK);
		button.setSize(100,25);
		gData = new GridData(SWT.CENTER, SWT.CENTER, false, false);
		button.setLayoutData(gData);
		
		button = new Button(parent, SWT.PUSH);
		button.setText(TexteIHM.ANNULER);
		button.setSize(100,25);
		gData = new GridData(SWT.CENTER, SWT.CENTER, false, false);
		button.setLayoutData(gData);
		
		
		
	}
	
}
