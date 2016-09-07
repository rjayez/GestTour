package Modele3P;

import Utils.GestTourUtils;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.List;


public class Impression {

    private final Configuration config;
    private static Impression instance;

    private HashMap<String, List<Epreuve>> mapEpreuveFeuilleMarque = null;

    public static Impression getInstance() {
        if (instance == null) {
            instance = new Impression();
        }

        return instance;
    }

    private Impression() {
        config = Configuration.getInstance();
    }

    //TODO Ajouter le nom du fichier de tournoi dans le nom du pdf crée
    public void createPdfFeuilleMarque(int numeroTour, ArrayList<String[]> listRenc, ArrayList<Epreuve> epreuves) {
        //TODO Gerer l'affichage selon le nombre d'epreuve
        Document doc = new Document(PageSize.A4.rotate(), 1, 1, 15, 15);
        try {

            String complementTitrePdf = GestTourUtils.getNomFichierSansExtension(config.getSaveFile());
            final String nomFichierPdf = "Feuille Rencontre Tour " + numeroTour + " - " + complementTitrePdf + ".pdf";

            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(nomFichierPdf));

            doc.open();

            ColumnText column = new ColumnText(writer.getDirectContentUnder());

            int count = 0;

            for (int i = 0; i < listRenc.size(); i++) {
                String[] renc = listRenc.get(i);
                // Ajout du tableau de marque des scores
                column.addElement(createTableFeuilleMarque(renc, epreuves));
                count++;

                if (count == 2) // Défini la colonne de gauche et écrit les feuilles de marque dans le document
                {
                    column.setSimpleColumn(doc.left(), doc.bottom(), (doc.left() + doc.right()) / 2, doc.top());
                    column.go();
                } else if (count == 4) // Défini la colonne de droite, écrit les feuilles de marque et passe à la page suivante
                {
                    column.setSimpleColumn((doc.left() + doc.right()) / 2, doc.bottom(), doc.right(), doc.top());
                    column.go();
                    doc.newPage();
                    count = 0;
                } else if (listRenc.size() - 1 == i) // Dans le cas d'un nombre impair de feuille de rencontre, écrit la feuille de marque
                {
                    if (count > 2) {
                        column.setSimpleColumn((doc.left() + doc.right()) / 2, doc.bottom(), doc.right(), doc.top());
                    } else {
                        column.setSimpleColumn(doc.left(), doc.bottom(), (doc.left() + doc.right()) / 2, doc.top());
                    }

                    column.go();
                }

            }


            doc.close();

            openPdfFile(nomFichierPdf);
        } catch (FileNotFoundException | DocumentException e) {
            e.printStackTrace();
        }

    }

    /**
     * Crée un PdfTable représentant une feuille de marque pour le pdf
     *
     * @param strRenc
     * @param epreuves liste des epreuves
     * @return renvoie une PdfPTable avec une feuille de marque de construite
     */
    private PdfPTable createTableFeuilleMarque(String[] strRenc, ArrayList<Epreuve> epreuves) throws DocumentException {

        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(95);
        table.setSpacingAfter(30);
        Font font = new Font(FontFamily.HELVETICA, 20);

        PdfPCell cell = new PdfPCell();
        PdfPCell cellVide = new PdfPCell();
        PdfPCell cellSansBordure = new PdfPCell();
        cellSansBordure.setBorder(Rectangle.NO_BORDER);

        PdfPCell cellFleche = new PdfPCell(cellSansBordure);
        try {
            Image imageFleche = Image.getInstance("fleche droite.png");

            cellFleche.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cellFleche.setImage(imageFleche);

        } catch (IOException e) {
            e.printStackTrace();
        }

        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setFixedHeight(50);

        // ligne 1
        cell.setPhrase(new Phrase("Tour n°\n" + strRenc[0], font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Equipe n°\n" + strRenc[1], font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Equipe n°\n" + strRenc[2], font));
        table.addCell(cell);

        initialiserListeEpreuveFeuilleMarque(epreuves);
        List<Epreuve> listEpreuveFeuille = mapEpreuveFeuilleMarque.get(strRenc[4]);

        for (Epreuve epreuve : listEpreuveFeuille) {


            cell.setPhrase(new Phrase(epreuve.getNom() + "\nTerrain " + strRenc[3], font));
            table.addCell(cell);
            table.addCell(cellVide);
            table.addCell(cellVide);
        }

        // dernière ligne
        cell.setPhrase(new Phrase("Total de\n Victoire", font));
        table.addCell(cell);
        table.addCell(cellVide);
        table.addCell(cellVide);

        return table;
    }

     void initialiserListeEpreuveFeuilleMarque(List<Epreuve> epreuves){
        if(mapEpreuveFeuilleMarque == null){
            mapEpreuveFeuilleMarque = new HashMap<>();
            for(int i = 0; i < epreuves.size(); i++){
                List<Epreuve> subList1 = epreuves.subList(i,epreuves.size());
                List<Epreuve> subList2 = epreuves.subList(0, i);
                List<Epreuve> listEpreuveFeuille = new ArrayList<>(subList1);
                listEpreuveFeuille.addAll(subList2);
                mapEpreuveFeuilleMarque.put(epreuves.get(i).getNom(), listEpreuveFeuille);
            }
        }
    }

    /**
     * Crée le pdf avec le tableau des rencontres des équipes pour le tour défini
     *
     * @param numTour
     * @param table
     */
    public void createPdfRencontre(int numTour, Table table) {
        Document doc = new Document(PageSize.A4, 1, 1, 1, 1);
        try {
            String complementTitrePdf = GestTourUtils.getNomFichierSansExtension(config.getSaveFile());
            final String nomFichierPdf = "Rencontre Tour " + numTour + " - " + complementTitrePdf + ".pdf";

            PdfWriter.getInstance(doc, new FileOutputStream(nomFichierPdf));

            doc.open();
            doc.addTitle("Tour n°" + numTour);
            doc.add(createTableRencontre(table, numTour));

            doc.close();

            openPdfFile(nomFichierPdf);

        } catch (FileNotFoundException | DocumentException e) {
            e.printStackTrace();
        }

    }

    /**
     * Crée le tableau des rencontres pour le pdf
     *
     * @param table
     * @param numTour
     * @return
     * @throws DocumentException
     */
    private PdfPTable createTableRencontre(Table table, int numTour) throws DocumentException {

        // Tableau rencontre 7 columns
        final int NOMBRE_COLONNE = 7;
        PdfPTable pdfTable = new PdfPTable(NOMBRE_COLONNE);
        pdfTable.setWidthPercentage(99);
        TableItem[] items = table.getItems();
        Font font = new Font(FontFamily.HELVETICA, 25);
        PdfPCell cell = new PdfPCell(new Phrase("Tour n°" + numTour, font));
        cell.setColspan(NOMBRE_COLONNE);
        pdfTable.addCell(cell);
        font.setSize(15);

        // Ajout des entetes (7 colonnes)
        cell = new PdfPCell(new Phrase("N°"));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        pdfTable.addCell(cell);
        cell = new PdfPCell(new Phrase("Noms"));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        pdfTable.addCell(cell);
        cell = new PdfPCell(new Phrase(""));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        pdfTable.addCell(cell);
        cell = new PdfPCell(new Phrase("N°"));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        pdfTable.addCell(cell);
        cell = new PdfPCell(new Phrase("Noms"));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        pdfTable.addCell(cell);
        cell = new PdfPCell(new Phrase("Epreuves"));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        pdfTable.addCell(cell);
        cell = new PdfPCell(new Phrase("Terrain N°"));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        pdfTable.addCell(cell);


        // pourcentage de la largeur de chaque colonne
        pdfTable.setWidths(new float[]{4, 36, 1, 5, 36, 16, 8});
        for (TableItem item : items) {
            for (int j = 0; j < NOMBRE_COLONNE; j++) {
                cell = new PdfPCell(new Phrase(item.getText(j), font));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                pdfTable.addCell(cell);
            }
        }

        return pdfTable;
    }

    public void createPdfClassement(Table table, boolean isCategorie) {
        Document doc = new Document(PageSize.A4, 1, 1, 1, 1);
        try {
            String complementTitrePdf = GestTourUtils.getNomFichierSansExtension(config.getSaveFile());
            final String nomFichierPDF = "Classement Final - " + complementTitrePdf + ".pdf";
            PdfWriter.getInstance(doc, new FileOutputStream(nomFichierPDF));

            doc.open();
            doc.addTitle("Classement");
            doc.add(createTableClassement(table, isCategorie));

            doc.close();
            openPdfFile(nomFichierPDF);

        } catch (FileNotFoundException | DocumentException e) {
            e.printStackTrace();
        }
    }

    private PdfPTable createTableClassement(Table table, boolean isCategorie) throws DocumentException {
        PdfPTable pdfTable;
        if (isCategorie) {
            pdfTable = new PdfPTable(8);
        } else {
            pdfTable = new PdfPTable(7);
        }

        pdfTable.setWidthPercentage(90);
        TableItem[] items = table.getItems();
        Font font = new Font(FontFamily.HELVETICA, 25);
        /*
         * PdfPCell cell = new PdfPCell(new Phrase("Tour n°1", font));
         * 
         * cell.setColspan(7); pdfTable.addCell(cell);
         */
        if (isCategorie) {
            pdfTable.setWidths(new int[]{5, 6, 5, 38, 13, 8, 12, 25});
        } else {
            pdfTable.setWidths(new int[]{5, 5, 39, 11, 9, 12, 25});

        }

        // Ajout des entetes
        PdfPCell cell = new PdfPCell(new Phrase("Cl."));
        pdfTable.addCell(cell);
        if (isCategorie) {
            cell = new PdfPCell(new Phrase("Cl. Cat."));
            pdfTable.addCell(cell);
        }
        cell = new PdfPCell(new Phrase("N°."));
        pdfTable.addCell(cell);
        cell = new PdfPCell(new Phrase("Noms"));
        pdfTable.addCell(cell);
        cell = new PdfPCell(new Phrase("Epreuves Gagnées"));
        pdfTable.addCell(cell);
        cell = new PdfPCell(new Phrase("Score"));
        pdfTable.addCell(cell);
        cell = new PdfPCell(new Phrase("Catégorie"));
        pdfTable.addCell(cell);
        cell = new PdfPCell(new Phrase("Commentaires"));
        pdfTable.addCell(cell);

        font.setSize(12);
        for (TableItem item : items) {
            for (int j = 0; j < 8; j++) {
                if (j != 1) {
                    cell = new PdfPCell(new Phrase(item.getText(j), font));
                    pdfTable.addCell(cell);
                } else {
                    if (isCategorie) {
                        cell = new PdfPCell(new Phrase(item.getText(j), font));
                        pdfTable.addCell(cell);
                    }
                }
            }
        }

        return pdfTable;
    }

    public void createPdfScoreTour(Table table, ArrayList<String[]> listRenc, int indexTour) {
        Document doc = new Document(PageSize.A4, 1, 1, 1, 1);
        try {
            String complementTitrePdf = GestTourUtils.getNomFichierSansExtension(config.getSaveFile());
            final String nomFichierPDF = "Score Tour " + (indexTour + 1) + " - " + complementTitrePdf + ".pdf";
            PdfWriter.getInstance(doc, new FileOutputStream(nomFichierPDF));

            doc.open();
            doc.addTitle("ScoreTour");
            doc.add(createTableScoreTour(table, listRenc));
            System.out.println(config.getSaveFile());
            doc.close();
            openPdfFile(nomFichierPDF);

        } catch (FileNotFoundException | DocumentException e) {
            e.printStackTrace();
        }
    }

    //TODO Gérer selon le nombre d'épreuve
    private PdfPTable createTableScoreTour(Table table, ArrayList<String[]> listRenc) throws DocumentException {
        TableItem[] items = table.getItems();
        TableColumn[] columns = table.getColumns();
        int nbColonne = columns.length * 2;
        PdfPTable pdfTable;
        pdfTable = new PdfPTable(nbColonne);

        pdfTable.setWidthPercentage(90);
        Font font = new Font(FontFamily.HELVETICA, 25);

        int[] widths = new int[nbColonne];
        for (int i = 0; i < nbColonne; i++) {
            widths[i] = 10;
        }
        pdfTable.setWidths(widths);


        PdfPCell cell;
        for (TableColumn column : columns) {
            cell = new PdfPCell(new Phrase(column.getText()));
            pdfTable.addCell(cell);
        }
        for (TableColumn column : columns) {
            cell = new PdfPCell(new Phrase(column.getText()));
            pdfTable.addCell(cell);
        }


        font.setSize(12);
        for (int i = 0; i < items.length; i++) {
            for (int j = 0; j < columns.length; j++) {
                cell = new PdfPCell(new Phrase(items[i].getText(j), font));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                pdfTable.addCell(cell);
            }

            for (int j = 0; j < columns.length; j++) {
//                items[Integer.parseInt(listRenc[i][2])]
                cell = new PdfPCell(new Phrase(items[Integer.parseInt(listRenc.get(i)[2]) - 1].getText(j), font));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                pdfTable.addCell(cell);
            }

        }

        return pdfTable;
    }

    /**
     * Ouvre le fichier PDF avec l'application par défaut
     * @param nomFichier
     */
    private void openPdfFile(String nomFichier) {

        try {
            Desktop.getDesktop().open(new File(nomFichier));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}