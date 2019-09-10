package com.yvanscoop.gestmedecins.services.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.yvanscoop.gestmedecins.entities.Medecin;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class ItextInPdf{

    private String chemin;
    private static final String cheminImage = "src/main/resources/static/images/medecins/";

    private String fileName;

    private int nbre_colonnes;

    private String[] entetes;

    private List<Medecin> medecins;

    public ItextInPdf(String fileName) {
        this.fileName = fileName+".pdf";
        chemin =  System.getProperty("user.home")+"//Downloads//";
    }

    public void setNbre_colonnes(int nbre_colonnes) {
        this.nbre_colonnes = nbre_colonnes;
    }

    public void setEntetes(String[] entetes) {
        this.entetes = entetes;
    }

    public void setMedecins(List<Medecin> medecins) {
        this.medecins = medecins;
    }

    public void imprimer() throws DocumentException, IOException{

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(fileName));

        document.open();

        PdfPTable table = new PdfPTable(nbre_colonnes);
        addTableHeader(table);
        //addRows(table);
        //addCustomRows(table);

        document.add(table);
        document.close();

        PdfReader pdfReader = new PdfReader(fileName);
        PdfStamper pdfStamper
                = new PdfStamper(pdfReader, new FileOutputStream(chemin));

        pdfStamper.setEncryption(
                "userpass".getBytes(),
                "".getBytes(),
                0,
                PdfWriter.ENCRYPTION_AES_256
                );

        pdfStamper.close();

    }

    public void imprimerMedecin() throws DocumentException, IOException{

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(chemin+fileName));

        document.open();

        String title = "Liste des médécins ("+medecins.size()+")";
        Font police = new Font(Font.FontFamily.HELVETICA  , 25, Font.BOLD);

        Paragraph paragraph = new Paragraph(title,police);

        addEmptyLine(paragraph,2);

        paragraph.setAlignment(Element.ALIGN_CENTER);
        document.add(paragraph);

        PdfPTable table = new PdfPTable(nbre_colonnes);
        addTableHeader(table);
        addRowsMedecins(table);

        document.add(table);

        document.close();
    }



    private void addTableHeader(PdfPTable table) {
        Stream.of(entetes)
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(1);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }

    private Paragraph addEmptyLine(Paragraph paragraph,int number){

        for (int i = 0; i < number; i++) {
           paragraph.add(new Paragraph(" "));
        }

        return paragraph;
    }

    private void addRowsMedecins(PdfPTable table) throws IOException, BadElementException {

        for (Medecin medecin: medecins) {
            String imageName = medecin.getId()+"_"+medecin.getNom()+"_"+medecin.getPrenom()+".png";

            table.setHorizontalAlignment(Element.ALIGN_CENTER);

            Path path = Paths.get(cheminImage+imageName);
            Image img = Image.getInstance(path.toAbsolutePath().toString());
            img.scaleAbsolute(30f, 30f);

            PdfPCell imageCell = new PdfPCell(img);
            imageCell.setPadding(5);
            imageCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            imageCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
            table.addCell(imageCell);
            table.addCell(medecin.getMatricule());
            table.addCell(medecin.getNom());
            table.addCell(medecin.getPrenom());
        }

    }

    private String verifyOS(){

        return  System.getProperty("os.name").toLowerCase();
    }
}
