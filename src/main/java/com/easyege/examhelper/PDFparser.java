package com.easyege.examhelper;

import android.os.Build;
import android.support.annotation.RequiresApi;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;

public class PDFparser {
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void main(String[] args) throws Exception {
        PDDocument doc = PDDocument.load(new File("C:\\Users\\Рома\\Desktop\\215.pdf"));
        PDFTextStripper stripper = new PDFTextStripper();

        String text = stripper.getText(doc);
        System.out.println(text);
        doc.close();


        /* Запись текста и прикрепление картинки к документу pdf

        PDDocument document = PDDocument.load(new File("C:\\Users\\Рома\\Desktop\\216.pdf"));
        PDPage page = document.getPage(0);
        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        //Begin the Content stream
        contentStream.beginText();
        //Setting the font to the Content stream
        contentStream.setFont(PDType1Font.TIMES_ROMAN, 16);
        //Setting the position for the line
        contentStream.setLeading(16f);
        //Setting the position for the line
        contentStream.newLineAtOffset(25, 725);

        String text1 = "This is an example of adding text to a page in the pdf document. we can add as many lines";
        String text2 = "as we want like this using the ShowText()  method of the ContentStream class";

        //Adding text in the form of string
        contentStream.showText(text1);
        contentStream.newLine();
        contentStream.showText(text2);
        //Ending the content stream
        contentStream.endText();

        PDImageXObject pdImage = PDImageXObject.createFromFile("C:\\Users\\Рома\\Desktop\\tick.png", document);
        pdImage.setHeight(300);
        pdImage.setWidth(300);
        contentStream.drawImage(pdImage, 90, 400);
        contentStream.close();

        //Saving the document
        document.save("C:\\Users\\Рома\\Desktop\\216.pdf");
        //Closing the document
        document.close();
        */
    }
}
