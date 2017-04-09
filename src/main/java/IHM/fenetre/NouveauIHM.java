package ihm.fenetre;

import constantes.TexteIHM;
import modele.Configuration;
import modele.Epreuve;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import java.io.File;
import java.util.List;

public class NouveauIHM extends Dialog
{

	public static final String FICHIER = "Fichier : ";
	public static final String FICHIER_TOUNOIR = "Fichier Tounoir";
	public static final String FICHIER_XML = "Fichier Xml";
	public static final String JAVAX_SWING_PLAF_METAL_ICONS_OCEAN_DIRECTORY_GIF = "/javax/swing/plaf/metal/icons/ocean/directory.gif";
	public static final String CONFIGURATION = "Configuration : ";
	public static final String MESSAGE_TOURNOI_NON_SPECIFIE = "Il n'y a pas de nom de tournoi sp\u00E9cifi\u00E9";
	public static final String FICHIER_DEJA_EXISTANT = "Le fichier existe d\u00E9jà. Veuillez choisir un autre nom";
	public static final String NOUVEAU_TOURNOI_SLR = "NouveauTournoi.slr";

	private Object result;
	private Shell shell;
	private Text tbFichier;
	
	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public NouveauIHM(Shell parent, int style)
	{
		super(parent, style);
		setText(TexteIHM.NOUVEAU_TOURNOI);
	}
	
	/**
	 * Open the dialog.
	 * 
     * @param config
     * @param epreuves
	 * @return the result
	 */
	public Object open(Configuration config, List<Epreuve> epreuves)
	{
		createContents(config, epreuves);
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed())
		{
			if (!display.readAndDispatch())
			{
				display.sleep();
			}
		}
		return result;
	}
	
	/**
	 * Create contents of the dialog.
	 */
	private void createContents(final Configuration config, List<Epreuve> epreuves)
	{
		shell = new Shell(getParent(), getStyle());
		shell.setSize(560, 199);
		shell.setText(getText());
		
		Label lbFichier = new Label(shell, SWT.NONE);
		lbFichier.setBounds(10, 34, 70, 20);
		lbFichier.setText(FICHIER);
		
		tbFichier = new Text(shell, SWT.BORDER);
		tbFichier.setBounds(86, 31, 424, 26);
		tbFichier.setText(NOUVEAU_TOURNOI_SLR);
		
		Button btnNewButton = new Button(shell, SWT.CENTER);
		btnNewButton.addSelectionListener(new SelectionAdapter()
		{
			@Override public void widgetSelected(SelectionEvent e)
			{
				String filePath;
				FileDialog fileDial = new FileDialog(getParent(), SWT.SAVE);
				fileDial.setFilterNames(new String[] {FICHIER_TOUNOIR, FICHIER_XML});
				fileDial.setFilterExtensions(new String[] {TexteIHM.PATTERN_EXTENSION_SLR, TexteIHM.PATTERN_EXTENTION_XML});
				
				filePath = fileDial.open();
				if (filePath != null)
				{
					tbFichier.setText(filePath);
				}
				
			}
		});
		btnNewButton.setImage(SWTResourceManager.getImage(NouveauIHM.class, JAVAX_SWING_PLAF_METAL_ICONS_OCEAN_DIRECTORY_GIF));
		btnNewButton.setBounds(516, 29, 28, 30);
		
		Label lblConfiguration = new Label(shell, SWT.NONE);
		lblConfiguration.setBounds(10, 85, 102, 20);
		lblConfiguration.setText(CONFIGURATION);
		
		Button btnConfig = new Button(shell, SWT.NONE);
		btnConfig.setBounds(118, 80, 130, 30);
		btnConfig.setText(TexteIHM.MODIFIER);
		
		Button btnOk = new Button(shell, SWT.NONE);
		btnOk.addSelectionListener(new SelectionAdapter()
		{
			@Override public void widgetSelected(SelectionEvent e)
			{
                //TODO test si un fichier avec le même nom existe déjà
				if (!tbFichier.getText().isEmpty())
				{
                    File f = new File(tbFichier.getText());
                    if(!f.isFile())
                    {
                        System.out.println(f.getAbsolutePath());
                        config.setSaveFile(tbFichier.getText());
                        result = true;
                        shell.dispose();
                    }
                    else
                    {
                        MessageBox dialog = new MessageBox(shell, SWT.OK);
                        dialog.setText(TexteIHM.ATTENTION);
                        dialog.setMessage(FICHIER_DEJA_EXISTANT);
                        dialog.open();
                    }
					
				}
				else
				{
					MessageBox dialog = new MessageBox(shell, SWT.OK);
					dialog.setText(TexteIHM.ATTENTION);
					dialog.setMessage(MESSAGE_TOURNOI_NON_SPECIFIE);
					dialog.open();
				}
				
			}
		});
		btnOk.setBounds(147, 126, 90, 30);
		btnOk.setText("Ok");
		
		Button btnCancel = new Button(shell, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter()
		{
			@Override public void widgetSelected(SelectionEvent e)
			{
				result = null;
				shell.dispose();
				
			}
		});
		btnCancel.setBounds(326, 126, 90, 30);
		btnCancel.setText(TexteIHM.ANNULER);
		
		Label lblNonImplant = new Label(shell, SWT.NONE);
		lblNonImplant.setBounds(273, 85, 91, 20);
		lblNonImplant.setText(TexteIHM.NON_IMPLANTE);
		
	}
}
