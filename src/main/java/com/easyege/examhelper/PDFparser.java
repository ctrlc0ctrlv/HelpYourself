package com.easyege.examhelper;

import android.os.Build;
import android.support.annotation.RequiresApi;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;

public class PDFparser {
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void main(String[] args) throws Exception {
        /*String string = "I";
        System.out.println("after compress:");
        byte [] compressed = compress(string);
        System.out.println(Arrays.toString(compressed));
        System.out.println("after decompress:");
        String decomp = decompress(compressed);
        System.out.println(decomp);
        System.out.println("just byte array:");
        System.out.println(Arrays.toString(string.getBytes()));
        System.out.println("one more byte array:");
        System.out.println(Arrays.toString(string1.getBytes(StandardCharsets.UTF_8)));*/

        PDDocument doc = PDDocument.load(new File("C:\\Users\\Рома\\Desktop\\215.pdf"));
        PDFTextStripper stripper = new PDFTextStripper();
        String text = stripper.getText(doc);
        System.out.println(text);
        doc.close();
    }


    /*@RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static byte[] compress(String str) throws Exception {
        if (str == null || str.length() == 0) {
            return new byte[0];
        }
        //System.out.println("String length : " + str.length());
        ByteArrayOutputStream obj=new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(obj);
        gzip.write(str.getBytes(StandardCharsets.UTF_8));
        gzip.close();
        //String outStr = obj.toString("UTF-8");
        //System.out.println("Output String length : " + outStr.length());
        return obj.toByteArray();
    }*/

    /*@RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static String decompress(byte [] str) throws Exception {

        //System.out.println("Input String length : " + str.length);
        GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(str));
        BufferedReader bf = new BufferedReader(new InputStreamReader(gis, StandardCharsets.UTF_8));
        StringBuilder outStr = new StringBuilder();
        String line;
        while ((line=bf.readLine())!=null) {
            outStr.append(line);
        }
        //System.out.println("Output String lenght : " + outStr.length());
        return outStr.toString();
    }*/
}
