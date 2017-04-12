package modele;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.LinkedList;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

@Getter
public class Configuration {

    private static Configuration instance;

    @Setter
    private int nbEpreuve;
    @Setter
    private int nbJoueurEquipe;
    @Setter
    private int nbTour;
    @Setter
    private String tarif;
    @Setter
    private String saveFile;
    @Setter
    private boolean isLockedInsc;
    @Setter
    private boolean[] tourFini;

    private LinkedList<String> prevFile;




    public static Configuration getInstance() {
        if (instance == null) {
            instance = new Configuration();
        }

        return instance;
    }

    private Configuration(String saveFile, int nbEpreuve, int nbJoueurEquipe, int nbTour, String tarif, boolean isLocked, boolean[] tourFini) {
        this.isLockedInsc = isLocked;
        this.tourFini = tourFini;
        this.nbEpreuve = nbEpreuve;
        this.nbJoueurEquipe = nbJoueurEquipe;
        this.nbTour = nbTour;
        this.tarif = tarif;
        this.saveFile = saveFile;
        this.prevFile = new LinkedList<>();
        try {
            getPreviousFile();
        } catch (SecurityException | BackingStoreException e) {
            e.printStackTrace();
        }
    }

    private Configuration() {
        this("defaut.xml", 3, 2, 3, "6.0", false, new boolean[3]);
    }

    private void getPreviousFile() throws SecurityException, BackingStoreException {
        Preferences pref = Preferences.userRoot().node("gesttour");

        String val = pref.get("prevfile", "");

        if (val.isEmpty()) {
            pref.put("prevfile", " ; ; ; ;");
            pref.flush();
            for (int i = 0; i < 4; i++) {
                prevFile.add("");
            }

        } else {
            String[] values = val.split(";");
            if (values.length > 0) {
                for (int i = 0; i < 4; i++) {
                    prevFile.add(values[i].trim());
                }
            }

        }

    }

    /**
     * Réorganisation de la liste des fichiers récent avec le chemin du fichier
     * passé en paramétre
     */
    public void fichierRecent(String fichier) {
        boolean present = false;
        int i = 0;
        for (String s : prevFile) {
            if (s.equals(fichier)) {
                present = true;
                break;
            }
            i++;
        }

        if (present) {
            Collections.swap(prevFile, 0, i);
        } else {
            prevFile.addFirst(fichier);
            prevFile.removeLast();
        }

        Preferences pref = Preferences.userRoot().node("gesttour");

        String save = "";
        for (String s : prevFile) {
            save += s + " ;";
        }

        pref.put("prevfile", save);
        try {
            pref.flush();
        } catch (BackingStoreException e) {

            e.printStackTrace();
        }
    }

    // Accesseurs
    public LinkedList<String> getPrevFile() {
        return prevFile;
    }

    public void setTourFini(boolean fini, int index) {
        this.tourFini[index] = fini;
    }


}
