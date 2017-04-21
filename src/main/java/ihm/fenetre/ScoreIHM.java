package ihm.fenetre;

import constantes.TexteIHM;
import modele.Epreuve;
import modele.Equipe;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import utils.InterfaceUtils;

import java.util.*;
import java.util.List;


public class ScoreIHM extends Dialog {

    public static final String MESSAGE_SCORE_INCORRECT_NOMBRE_EPREUVE_GAGNE_ANORMAUX = "Veuillez entrer les scores afin que chaque \u00E9preuve soit gagn\u00E9e par une seule \u00E9quipe";
    public static final String SCORE_SUP_SCORE_MAX_EPREUVE = "La valeur saisie est sup\u00E9rieur au score maximal de l'\u00E9preuve" +
            "\n Veuillez  mettre une valeur \u00E9gale ou inf\u00E9rieur au score maximal";
    public static final String MESSAGE_SCORE_NEGATIF = "Le joueur a frapp\u00E9 quelqu'un pour avoir un score n\u00E9gatif ?";
    public static final String MESSAGE_NOT_A_NUMBER = "La valeur saisie n'est pas un nombre";
    public static final String MESSAGE_RAZ_SCORE = "Voulez-vous remettre les scores à z\u00E9ro ?";

    private List<Epreuve> epreuves;
    private Boolean result;
    private Equipe eq1, eq2;

    public ScoreIHM(Shell parent, int style, List<Epreuve> epreuves) {
        super(parent, style);
        this.epreuves = epreuves;
    }

    public ScoreIHM(Shell parent, List<Epreuve> epreuves) {
        this(parent, 0, epreuves);
    }

    public boolean open(int indexTour, Equipe eq1, Equipe eq2) {
        this.eq1 = eq1;
        this.eq2 = eq2;
        result = false;
        Shell parent = getParent();
        Shell fenetre = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);

        fenetre.setSize(325, 250);
        fenetre.setText(TexteIHM.SCORE);

        GridLayout gridL = new GridLayout(3, false);
        //GridLayout gridL = new GridLayout(3, false);
        gridL.horizontalSpacing = 15;
        gridL.verticalSpacing = 8;
        fenetre.setLayout(gridL);

        createContents(fenetre, indexTour, eq1, eq2);

        fenetre.pack();
        fenetre.open();
        Display display = parent.getDisplay();
        while (!fenetre.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }

        }

        return result;

    }

    private void createContents(final Shell fenetre, final int indexTour, final Equipe eq1, final Equipe eq2) {

        GridData gData;

        Label labEquipe = new Label(fenetre, 0);
        labEquipe.setText("Equipe :");

        Label labJ1 = new Label(fenetre, SWT.CENTER);
        labJ1.setText("N\u00b0" + eq1.getNumeroStr());
        gData = new GridData();
        gData.horizontalAlignment = SWT.CENTER;
        labJ1.setLayoutData(gData);

        Label labEq2 = new Label(fenetre, 0);
        labEq2.setText("N\u00b0" + eq2.getNumeroStr());
        gData = new GridData();
        gData.horizontalAlignment = SWT.CENTER;
        labEq2.setLayoutData(gData);

        createScoreEpreuve(fenetre, indexTour, eq1, eq2);

        final Button btOk = new Button(fenetre, SWT.PUSH);
        btOk.setText("OK");
        gData = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
        gData.widthHint = 100;
        btOk.setLayoutData(gData);
        btOk.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {

                compterPartieGagne(indexTour, eq1, eq2);
                if ((eq1.getPartiesGagnees()[indexTour] + eq2.getPartiesGagnees()[indexTour]) == epreuves.size() || (estScoreNul(indexTour, eq1) && estScoreNul(indexTour, eq2))) {
                    result = true;
                    fenetre.dispose();
                } else {
                    result = false;
                    InterfaceUtils.ouvrirDialogueTexte(TexteIHM.ATTENTION_EXCLAMATION, MESSAGE_SCORE_INCORRECT_NOMBRE_EPREUVE_GAGNE_ANORMAUX, fenetre, SWT.ERROR);
                }
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent arg0) {
            }
        });

        Button btAnnuler = new Button(fenetre, SWT.PUSH);
        btAnnuler.setText("Annuler");
        gData = new GridData(SWT.RIGHT, SWT.CENTER, false, false);
        gData.widthHint = 100;
        btAnnuler.setLayoutData(gData);

        btAnnuler.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                result = false;
                fenetre.dispose();
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent arg0) {
            }
        });

        //Button btnFleche = new Button(fenetre, SWT.FLAT|SWT.ARROW|SWT.DOWN);
        Button btnRAZ = new Button(fenetre, SWT.PUSH);
        //btnFleche.setText("&Arrow Button");
        btnRAZ.setText(TexteIHM.RAZ);
        gData = new GridData(SWT.RIGHT, SWT.CENTER, false, false);
        gData.horizontalAlignment = SWT.RIGHT;
        btnRAZ.setLayoutData(gData);

        btnRAZ.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent se) {

                int reponse = InterfaceUtils.ouvrirDialogueTexte(TexteIHM.RAZ, MESSAGE_RAZ_SCORE, fenetre, SWT.YES | SWT.NO);
                if (reponse == SWT.YES) {
                    for (int i = 0; i < epreuves.size(); i++) {
                        eq1.setScores(indexTour, i, 0);
                        eq2.setScores(indexTour, i, 0);
                    }
                    eq1.setPartieGagnee(indexTour, 0);
                    eq2.setPartieGagnee(indexTour, 0);
                    result = true;
                    fenetre.dispose();
                }

            }

        });
    }

    private void createScoreEpreuve(final Shell fenetre, final int indexTour, final Equipe eq1, final Equipe eq2) {

        for (int i = 0; i < epreuves.size(); i++) {
            final Epreuve epr = epreuves.get(i);
            final Integer index = i;

            Label labNom = new Label(fenetre, SWT.CENTER);
            labNom.setText(epr.getNom());
            GridData gData = new GridData(SWT.RIGHT, SWT.CENTER, false, false);
            labNom.setLayoutData(gData);

            final Text tbScoreEq1 = new Text(fenetre, SWT.BORDER | SWT.CENTER);

            tbScoreEq1.setSize(100, 10);
            tbScoreEq1.setText(eq1.getScores(indexTour, i));
            gData = new GridData(SWT.CENTER, SWT.CENTER, false, false);
            gData.widthHint = 40;
            tbScoreEq1.setLayoutData(gData);
            tbScoreEq1.addListener(SWT.KeyUp, event -> {
                if (SWT.CR == event.keyCode || SWT.KEYPAD_CR == event.keyCode) {
                    compterPartieGagne(indexTour, eq1, eq2);
                    if ((eq1.getPartiesGagnees()[indexTour] + eq2.getPartiesGagnees()[indexTour]) == epreuves.size() || (estScoreNul(indexTour, eq1) && estScoreNul(indexTour, eq2))) {
                        result = true;
                        fenetre.dispose();
                    } else {
                        result = false;
                        InterfaceUtils.ouvrirDialogueTexte(TexteIHM.ATTENTION_EXCLAMATION, MESSAGE_SCORE_INCORRECT_NOMBRE_EPREUVE_GAGNE_ANORMAUX, fenetre, SWT.ERROR);
                    }
                }
            });

            tbScoreEq1.addFocusListener(new FocusListener() {

                @Override
                public void focusLost(FocusEvent arg0) {
                    try {
                        Integer tbValue = Integer.parseInt(tbScoreEq1.getText());
                        if (tbValue >= epr.getNbPointGagnant()) {
                            if (tbValue > epr.getNbPointGagnant()) {
                                InterfaceUtils.ouvrirDialogueTexte(TexteIHM.ERREUR_DE_SAISIE, SCORE_SUP_SCORE_MAX_EPREUVE, fenetre);
                            }

                            tbScoreEq1.setText(Integer.toString(epr.getNbPointGagnant()));
                            eq1.setScores(indexTour, index, epr.getNbPointGagnant());

                        } else if (tbValue < 0) {
                            tbScoreEq1.setText("0");
                            InterfaceUtils.ouvrirDialogueTexte(TexteIHM.ERREUR_DE_SAISIE, MESSAGE_SCORE_NEGATIF, fenetre);
                        } else // Valeur correct et inférieur au nombre de point gagnant
                        {
                            eq1.setScores(indexTour, index, tbValue);
                        }

                    } catch (Exception e) {
                        InterfaceUtils.ouvrirDialogueTexte(TexteIHM.ERREUR_DE_SAISIE, MESSAGE_NOT_A_NUMBER, fenetre);
                    }
                }

                @Override
                public void focusGained(FocusEvent arg0) {
                    tbScoreEq1.selectAll();
                }
            });

            final Text tbScoreEq2 = new Text(fenetre, SWT.BORDER | SWT.CENTER);
            tbScoreEq2.setSize(100, 10);
            tbScoreEq2.setText(eq2.getScores(indexTour, i));
            gData = new GridData(SWT.CENTER, SWT.CENTER, false, false);
            gData.widthHint = 40;
            tbScoreEq2.setLayoutData(gData);


            tbScoreEq2.addFocusListener(new FocusListener() {
                @Override
                public void focusLost(FocusEvent arg0) {
                    try {
                        int tbValue = Integer.parseInt(tbScoreEq2.getText());

                        if (tbValue >= epr.getNbPointGagnant()) {
                            if (tbValue > epr.getNbPointGagnant()) {
                                InterfaceUtils.ouvrirDialogueTexte(TexteIHM.ERREUR_DE_SAISIE, SCORE_SUP_SCORE_MAX_EPREUVE, fenetre);
                            }

                            tbScoreEq2.setText(Integer.toString(epr.getNbPointGagnant()));
                            eq2.setScores(indexTour, index, epr.getNbPointGagnant());

                        } else if (tbValue < 0) {
                            tbScoreEq2.setText("0");
                            InterfaceUtils.ouvrirDialogueTexte(TexteIHM.ERREUR_DE_SAISIE, MESSAGE_SCORE_NEGATIF, fenetre);
                        } else // Valeur correct !
                        {
                            eq2.setScores(indexTour, index, tbValue);
                        }

                    } catch (Exception e) {
                        InterfaceUtils.ouvrirDialogueTexte(TexteIHM.ERREUR_DE_SAISIE, MESSAGE_NOT_A_NUMBER, fenetre);
                    }
                }

                @Override
                public void focusGained(FocusEvent arg0) {
                    tbScoreEq2.selectAll();

                }
            });
        }

    }

    private void compterPartieGagne(int indexTour, Equipe eq1, Equipe eq2) {
        eq1.setPartieGagnee(indexTour, 0);
        eq2.setPartieGagnee(indexTour, 0);
        for (int i = 0; i < epreuves.size(); i++) {
            if (eq1.getScores()[indexTour][i] > eq2.getScores()[indexTour][i]) {
                eq1.setPartieGagnee(indexTour, eq1.getPartiesGagnees()[indexTour] + 1);
            }

            if (eq1.getScores()[indexTour][i] < eq2.getScores()[indexTour][i]) {
                eq2.setPartieGagnee(indexTour, eq2.getPartiesGagnees()[indexTour] + 1);
            }

        }
    }

    private boolean estScoreNul(int indexTour, Equipe eq) {
        int[][] scoreTemp = eq.getScores();

        for (int i = 0; i < scoreTemp[indexTour].length; i++) {
            if (scoreTemp[indexTour][i] != 0) {
                return false;
            }
        }

        return true;

    }

}
