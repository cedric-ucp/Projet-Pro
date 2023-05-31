package document;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import utils.Utils;

import java.io.FileOutputStream;
import java.util.logging.Level;

public class PDFDocument extends Document {
    private Document document = new Document(PageSize.A4 , 50 , 50 , 50 , 50);
    public PDFDocument(){
        try{
            PdfWriter.getInstance(document , new FileOutputStream("AuditReport.pdf"));
            addMetaInformation();
        }
        catch(Exception e){
            Utils.getLogger().log(Level.SEVERE , e.getMessage());
        }
    }
    private void addMetaInformation(){
        document.addAuthor("Etame Cedric");
        document.addCreationDate();
        document.addTitle("Audit Report");
        document.addHeader("header" , "SUP DE VINCI");
        document.addSubject("Audit Report");
        Phrase phraseBefore = new Phrase("SUP DE VINCI");
        Phrase phraseAfter = new Phrase("ETAME CEDRIC");
        HeaderFooter footer = new HeaderFooter(phraseBefore , phraseAfter);
        footer.setAlignment(HeaderFooter.ALIGN_JUSTIFIED);
        document.setFooter(footer);
    }
    public void addParagraph(String paragraph){
        try {
            document.open();
            document.add(new Paragraph(paragraph));
            document.close();
        }
        catch(Exception e){
            Utils.getLogger().log(Level.SEVERE , e.getMessage());
        }
    }
}
