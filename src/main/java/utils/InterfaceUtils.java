package utils;

import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

/**
 * Created by Romain on 11/04/2017.
 */
public class InterfaceUtils {

    public static int ouvrirDialogueTexte(final String titre, final String message, final Shell fenetre, int typeBouton) {
        MessageBox dialog = new MessageBox(fenetre, typeBouton);
        dialog.setText(titre);
        dialog.setMessage(message);
        return dialog.open();
    }

    public static int ouvrirDialogueTexte(final String titre, final String message, final Shell fenetre) {
        MessageBox dialog = new MessageBox(fenetre);
        dialog.setText(titre);
        dialog.setMessage(message);
        return dialog.open();
    }
}
