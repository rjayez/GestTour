package IHM.fenetre;

import java.util.ArrayList;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import Modele3P.Configuration;
import Modele3P.Epreuve;
import java.io.File;

public class NouveauIHM extends Dialog
{
	
	protected Object result;
	protected Shell shell;
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
		setText("Nouveau Tournoi");
	}
	
	/**
	 * Open the dialog.
	 * 
     * @param config
     * @param epreuves
	 * @return the result
	 */
	public Object open(Configuration config, ArrayList<Epreuve> epreuves)
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
	private void createContents(final Configuration config, ArrayList<Epreuve> epreuves)
	{
		shell = new Shell(getParent(), getStyle());
		shell.setSize(560, 199);
		shell.setText(getText());
		
		Label lbFichier = new Label(shell, SWT.NONE);
		lbFichier.setBounds(10, 34, 70, 20);
		lbFichier.setText("Fichier : ");
		
		tbFichier = new Text(shell, SWT.BORDER);
		tbFichier.setBounds(86, 31, 424, 26);
		tbFichier.setText("NouveauTournoi.slr");
		
		Button btnNewButton = new Button(shell, SWT.CENTER);
		btnNewButton.addSelectionListener(new SelectionAdapter()
		{
			@Override public void widgetSelected(SelectionEvent e)
			{
				String filePath;
				FileDialog fileDial = new FileDialog(getParent(), SWT.SAVE);
				fileDial.setFilterNames(new String[] { "Fichier Tounoir", "Fichier Xml" });
				fileDial.setFilterExtensions(new String[] { "*.slr", "*.xml" });
				
				filePath = fileDial.open();
				if (filePath != null)
				{
					tbFichier.setText(filePath);
				}
				
			}
		});
		btnNewButton.setImage(SWTResourceManager.getImage(NouveauIHM.class, "/javax/swing/plaf/metal/icons/ocean/directory.gif"));
		btnNewButton.setBounds(516, 29, 28, 30);
		
		Label lblConfiguration = new Label(shell, SWT.NONE);
		lblConfiguration.setBounds(10, 85, 102, 20);
		lblConfiguration.setText("Configuration : ");
		
		Button btnConfig = new Button(shell, SWT.NONE);
		btnConfig.setBounds(118, 80, 130, 30);
		btnConfig.setText("Modifier");
		
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
                        dialog.setText("Attention");
                        dialog.setMessage("Le fichier existe déjà. Veuillez choisir un autre nom");
                        dialog.open();
                    }
					
				}
				else
				{
					MessageBox dialog = new MessageBox(shell, SWT.OK);
					dialog.setText("Attention");
					dialog.setMessage("Il n'y a pas de nom de tournoi spécifié");
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
		btnCancel.setText("Annuler");
		
		Label lblNonImplant = new Label(shell, SWT.NONE);
		lblNonImplant.setBounds(273, 85, 91, 20);
		lblNonImplant.setText("Non implant\u00E9");
		
	}
}
