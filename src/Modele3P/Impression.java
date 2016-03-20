package Modele3P;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class Impression
{

    private Configuration config;
    private static Impression instance;

    public static Impression getInstance()
    {
        if (instance == null)
        {
            instance = new Impression();
        }

        return instance;
    }

    private Impression()
    {
        config = Configuration.getInstance();
    }
    //TODO Ajouter le nom du fichier de tournoi dans le nom du pdf crée
    public void createPdfFeuilleMarque(String nomFichier, ArrayList<String[]> listRenc, ArrayList<Epreuve> epreuves)
    {
        //TODO Gerer l'affichage selon le nombre d'epreuve
        Document doc = new Document(PageSize.A4.rotate(), 1, 1, 15, 15);
        try
        {

            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(nomFichier));

            doc.open();

            ColumnText column = new ColumnText(writer.getDirectContentUnder());

            int count = 0;
            for (String[] renc : listRenc)
            {

                column.addElement(createTableFeuilleMarque(renc, epreuves));
                count++;
                if (count == 2)
                {
                    column.setSimpleColumn(doc.left(), doc.bottom(), (doc.left() + doc.right()) / 2, doc.top());
                    column.go();
                }
                else if (count == 4)
                {
                    column.setSimpleColumn((doc.left() + doc.right()) / 2, doc.bottom(), doc.right(), doc.top());
                    column.go();
                    doc.newPage();
                    count = 0;
                }
            }
            // column.go();

            doc.close();

            openAdobeViewer(nomFichier);
        }
        catch (FileNotFoundException e)
        {

            e.printStackTrace();
        }
        catch (DocumentException e)
        {

            e.printStackTrace();
        }

    }

    /**
     * Crée un PdfTable représentant une feuille de marque pour le pdf 
     * @param strRenc
     * @param epreuves
     * @return 
     */
    private PdfPTable createTableFeuilleMarque(String[] strRenc, ArrayList<Epreuve> epreuves)
    {

        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(95);
        table.setSpacingAfter(30);
        Font font = new Font(FontFamily.HELVETICA, 20);

        PdfPCell cell = new PdfPCell();
        PdfPCell cellVide = new PdfPCell();
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

        for (Epreuve epreuve : epreuves)
        {
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

    /**
     * Crée le pdf avec le tableau des rencontres des équipes pour le tour défini
     * @param numTour
     * @param table 
     */
    public void createPdfRencontre(int numTour, Table table)
    {
        Document doc = new Document(PageSize.A4, 1, 1, 1, 1);
        try
        {
            String cheminFichier = config.getSaveFile();
            /*
             * StringTokenizer str = new String[] temp = cheminFichier.split("[\\]"); String nomFichier = temp[temp.length - 1].split(".")[0];
             */

            PdfWriter.getInstance(doc, new FileOutputStream("pdf_Rencontre_Tour_" + numTour + ".pdf"));

            doc.open();
            doc.addTitle("Tour n°" + numTour);
            doc.add(createTableRencontre(table, numTour));

            doc.close();

            openAdobeViewer("pdf_Rencontre_Tour_" + numTour + ".pdf");

        }
        catch (FileNotFoundException e)
        {

            e.printStackTrace();
        }
        catch (DocumentException e)
        {

            e.printStackTrace();
        }

    }
    
    /**
     * Crée le tableau des rencontres pour le pdf 
     * @param table
     * @param numTour
     * @return
     * @throws DocumentException 
     */
    private PdfPTable createTableRencontre(Table table, int numTour) throws DocumentException
    {

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
        pdfTable.setWidths(new float[]
        {
            4, 36, 1, 5, 36, 16,8 
        });
        for (TableItem item : items)
        {
            for (int j = 0; j < NOMBRE_COLONNE; j++)
            {
                cell = new PdfPCell(new Phrase(item.getText(j), font));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                pdfTable.addCell(cell);
            }
        }
        
        return pdfTable;
    }

    public void createPdfClassement(Table table, boolean isCategorie)
    {
        Document doc = new Document(PageSize.A4, 1, 1, 1, 1);
        try
        {
            /*
             * String cheminFichier = config.getSaveFile(); String[] temp = cheminFichier.split("\\"); String nomFichier = temp[temp.length - 1].split(".")[0];
             */

            PdfWriter.getInstance(doc, new FileOutputStream("pdf_Classement_final.pdf"));

            doc.open();
            doc.addTitle("Classement");
            doc.add(createTableClassement(table, isCategorie));

            doc.close();
            openAdobeViewer("pdf_Classement_final.pdf");

        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (DocumentException e)
        {

            e.printStackTrace();
        }
    }

    private PdfPTable createTableClassement(Table table, boolean isCategorie) throws DocumentException
    {
        PdfPTable pdfTable;
        if (isCategorie)
        {
            pdfTable = new PdfPTable(8);
        }
        else
        {
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
        if (isCategorie)
        {
            pdfTable.setWidths(new int[]
            {
                5, 6, 5, 38, 13, 8, 12, 25
            });
        }
        else
        {
            pdfTable.setWidths(new int[]
            {
                5, 5, 39, 11, 9, 12, 25
            });

        }

        // Ajout des entetes
        PdfPCell cell = new PdfPCell(new Phrase("Cl."));
        pdfTable.addCell(cell);
        if (isCategorie)
        {
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
        for (int i = 0; i < items.length; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                if (j != 1)
                {
                    cell = new PdfPCell(new Phrase(items[i].getText(j), font));

                    pdfTable.addCell(cell);
                }
                else
                {
                    if (isCategorie)
                    {
                        cell = new PdfPCell(new Phrase(items[i].getText(j), font));

                        pdfTable.addCell(cell);
                    }
                }

            }

        }

        return pdfTable;
    }

    public void createPdfScoreTour(Table table, ArrayList<String[]> listRenc, int indexTour)
    {
        Document doc = new Document(PageSize.A4, 1, 1, 1, 1);
        try
        {
            /*
             * String cheminFichier = config.getSaveFile(); String[] temp = cheminFichier.split("\\"); String nomFichier = temp[temp.length - 1].split(".")[0];
             */

            PdfWriter.getInstance(doc, new FileOutputStream("pdf_Score_Tour"+ (indexTour+1) +".pdf"));
            
            doc.open();
            doc.addTitle("ScoreTour");
            doc.add(createTableScoreTour(table, listRenc));
            System.out.println(config.getSaveFile());
            doc.close();
            openAdobeViewer("pdf_Score_Tour"+ (indexTour+1) +".pdf");

        }
        catch (FileNotFoundException | DocumentException e)
        {

            e.printStackTrace();
        }
    }
    //TODO Gérer selon le nombre d'épreuve
    private PdfPTable createTableScoreTour(Table table, ArrayList<String[]> listRenc) throws DocumentException
    {
        TableItem[] items = table.getItems();
        TableColumn[] columns = table.getColumns();
        int nbColonne = columns.length * 2;
        PdfPTable pdfTable;
        pdfTable = new PdfPTable(nbColonne);

        pdfTable.setWidthPercentage(90);
        Font font = new Font(FontFamily.HELVETICA, 25);
     
        int[] widths = new int[nbColonne];
        for(int i = 0 ; i < nbColonne ; i++)
        {
            widths[i] = 10;
        }
        pdfTable.setWidths(widths);


        PdfPCell cell;
        for (TableColumn column : columns)
        {
            cell = new PdfPCell(new Phrase(column.getText()));
            pdfTable.addCell(cell);
        }
        for (TableColumn column : columns)
        {
            cell = new PdfPCell(new Phrase(column.getText()));
            pdfTable.addCell(cell);
        }
        

        font.setSize(12);
        for (int i = 0; i < items.length; i++)
        {
            for (int j = 0; j < columns.length; j++)
            {
                cell = new PdfPCell(new Phrase(items[i].getText(j), font));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                pdfTable.addCell(cell);
            }
            
            for (int j = 0; j < columns.length; j++)
            {
//                items[Integer.parseInt(listRenc[i][2])]
                cell = new PdfPCell(new Phrase(items[Integer.parseInt(listRenc.get(i)[2])-1].getText(j), font));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                pdfTable.addCell(cell);
            }

        }

        return pdfTable;
    }

    private void openAdobeViewer(String nomFichier)
    {
        // TODO Trouver une manière plus propre d'ouvrir adobe reader
        String[] cmd =
        {
            "C:\\Program Files (x86)\\Adobe\\Reader 11.0\\Reader\\AcroRd32.exe", System.getProperty("user.dir") + "\\" + nomFichier
        };

        try
        {
            Process p = Runtime.getRuntime().exec(cmd);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
