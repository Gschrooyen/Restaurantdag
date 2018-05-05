package be.sint_andries.controller.PDF_makers;

import be.sint_andries.model.Bestelling;
import be.sint_andries.model.Gerecht;
import be.sint_andries.model.Klant;
import be.sint_andries.model.Tijdstip;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PDFMaker {

    public static void makeKlantPDF(List<Klant> klanten, String path) throws FileNotFoundException, DocumentException {
        Document document = new Document();

        PdfWriter.getInstance(document, new FileOutputStream(".." + File.separator  + path));
        document.open();
        document.add(new LineSeparator(5F, 100F, BaseColor.BLACK, 6, 0));
        document.add(new Paragraph("overzicht van alle klanten", new Font(Font.FontFamily.TIMES_ROMAN, 25)));
        document.add(new LineSeparator(5F, 100F, BaseColor.BLACK, 6, -4));
        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
        PdfPCell cel1 = getCell("Tijdstip", PdfPCell.ALIGN_LEFT);
        System.out.println(cel1);
        cel1.setPaddingTop(5);
        table.addCell(cel1);
        cel1 = getCell("Naam", PdfPCell.ALIGN_CENTER);
        cel1.setPaddingTop(5);
        table.addCell(cel1);
        cel1 = getCell("Groepsnaam", PdfPCell.ALIGN_CENTER);
        cel1.setPaddingTop(5);
        table.addCell(cel1);
        cel1 = getCell("Aantal volwassenen", PdfPCell.ALIGN_CENTER);
        cel1.setPaddingTop(5);
        table.addCell(cel1);
        cel1 = getCell("Aantal kinderen", PdfPCell.ALIGN_CENTER);
        cel1.setPaddingTop(5);
        table.addCell(cel1);
        cel1 = getCell("Totaal: ", PdfPCell.ALIGN_RIGHT);
        cel1.setPaddingTop(5);
        table.addCell(cel1);

        for (Klant k :
                klanten) {

            table.addCell(getCell(k.getTijdstip().toString(), PdfPCell.ALIGN_BASELINE));
            table.addCell(getCell(k.getNaam(), PdfPCell.ALIGN_BASELINE));
            if (k.getGroepsNaam() != null){
                table.addCell(getCell(k.getGroepsNaam(), PdfPCell.ALIGN_BASELINE));
            }else {
                table.addCell(getCell("", PdfPCell.ALIGN_BASELINE));
            }
            table.addCell(getCell(k.getVolwassenen() + "", PdfPCell.ALIGN_BASELINE));
            table.addCell(getCell(k.getKinderen() + "", PdfPCell.ALIGN_BASELINE));
            table.addCell(getCell("€" + k.getPrijs().toString(), PdfPCell.ALIGN_BASELINE));
        }

        document.add(table);
        document.close();
    }


    public static void makeKlantSpecificPDF(List<Klant> klanten, String path, boolean desserten) throws FileNotFoundException, DocumentException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(".." + File.separator  + path));
        document.open();

        for (Klant k : klanten) {
            //creating the top of the document
            String groepsnaam = k.getGroepsNaam() == null ? "" : k.getGroepsNaam();
            //make the containing table top
            PdfPTable bigTableTop = new PdfPTable(1);
            bigTableTop.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
            bigTableTop.setWidthPercentage(100);
            //make the table for laying out dishes top
            PdfPTable gerechtenTableTop = new PdfPTable(3);
            gerechtenTableTop.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
            gerechtenTableTop.setWidthPercentage(100);

            //filling the small table
            PdfPCell cel1 = getCell(k.getTijdstip().toString(), PdfPCell.ALIGN_BASELINE);
            document.setMarginMirroringTopBottom(true);

            gerechtenTableTop.addCell(cel1);
            gerechtenTableTop.addCell(getCell("Gerecht", PdfPCell.ALIGN_BASELINE));
            gerechtenTableTop.addCell(getCell("Aantal", PdfPCell.ALIGN_BASELINE));

            //adding 2 tables and adding to the document
            Paragraph p = new Paragraph(k.getNaam() + "                " + groepsnaam, new Font(Font.FontFamily.TIMES_ROMAN, 25));
            p.add(new LineSeparator(5F, 100F, BaseColor.BLACK, 6, -4));
            PdfPCell pdfPCell = new PdfPCell(p);
            pdfPCell.setBorder(Rectangle.NO_BORDER);
            bigTableTop.addCell(pdfPCell);


            for (Bestelling b :
                    k.getBestelling()) {
                if (b.getGerecht().isDessert().getValue() == desserten) {
                    gerechtenTableTop.addCell(getCell("", PdfPCell.ALIGN_BASELINE));
                    gerechtenTableTop.addCell(getCell(b.getGerecht().toString(), PdfPCell.ALIGN_BASELINE));
                    gerechtenTableTop.addCell(getCell(b.getAantal() + "", PdfPCell.ALIGN_BASELINE));
                    gerechtenTableTop.completeRow();
                }
            }

            PdfPCell cell = new PdfPCell(gerechtenTableTop);
            cell.setBorder(Rectangle.NO_BORDER);
            PdfPCell cell1 = new PdfPCell(cell);
            cel1.setBorder(Rectangle.NO_BORDER);
            cell1.setMinimumHeight(document.getPageSize().getHeight() / 2 - document.topMargin() - document.bottomMargin() - 20);
            if (desserten){
                cell1.setMinimumHeight(cell1.getMinimumHeight() / 2 + 20);
            }
            bigTableTop.addCell(cell1);
            document.add(bigTableTop);
            document.add(new Phrase("test", new Font(Font.FontFamily.TIMES_ROMAN, 12, -1, BaseColor.WHITE)));
        }
        document.close();
    }

    public static void makeTotalenPDF(List<Klant> klanten, String path) throws FileNotFoundException, DocumentException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(".." + File.separator  + path));
        document.open();

        Map<Gerecht, Integer> totalen = new HashMap<>();

        for (Klant k :
                klanten) {
            for (Bestelling b :
                    k.getBestelling()) {
                totalen.put(b.getGerecht(), totalen.getOrDefault(b.getGerecht(), 0) + b.getAantal());
            }
        }

        document.add(new Phrase("Overzicht Totalen", new Font(Font.FontFamily.TIMES_ROMAN, 30, Font.BOLD)));
        PdfPTable table = new PdfPTable(2);
        List<Gerecht> keys = new ArrayList<>(totalen.keySet());
        keys = keys.stream().sorted().collect(Collectors.toList());
        keys.forEach(System.out::println);
        for (Gerecht g :
                keys) {
            int aantal = totalen.get(g);

            table.addCell(getCell(g.getNaam(), new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD)));
            table.addCell(aantal+"");
        }
        document.add(table);

        document.close();
    }

    public static void makeKeukenPDF(List<Klant> klanten, String path) throws FileNotFoundException, DocumentException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(".." + File.separator  + path));
        document.open();
        document.add(new Paragraph("KeukenRapport", new Font(Font.FontFamily.TIMES_ROMAN, 40, Font.BOLD)));
        document.add(new LineSeparator(5, 100, BaseColor.BLACK, 6, -4));
        HashMap<Tijdstip, List<Bestelling>> tijdstipBestellingHashMap = new HashMap<Tijdstip, List<Bestelling>>();
        for (Klant k :
                klanten) {
            if (tijdstipBestellingHashMap.containsKey(k.getTijdstip())){
                List<Bestelling> bestellings = tijdstipBestellingHashMap.get(k.getTijdstip());
                bestellings.addAll(k.getBestelling());
                tijdstipBestellingHashMap.put(k.getTijdstip(), bestellings);
            }else {
                List<Bestelling> bestellings = new ArrayList<Bestelling>(k.getBestelling());
                tijdstipBestellingHashMap.put(k.getTijdstip(), bestellings);
            }
        }

        List<Tijdstip> keys = tijdstipBestellingHashMap.keySet().stream().sorted().collect(Collectors.toList());

        for (Tijdstip t :
                keys) {
            document.add(new Paragraph(t.toString(), new Font(Font.FontFamily.TIMES_ROMAN, 35, Font.BOLD)));
            document.add(new LineSeparator(5F, 100F, BaseColor.BLACK, 6, -4));
            HashMap<Gerecht, Integer> gerechtIntegerHashMap = new HashMap<>();
            List<Bestelling> bestellingList = tijdstipBestellingHashMap.get(t);
            System.out.println(bestellingList.size());
            for (Bestelling b :
                    bestellingList) {
                gerechtIntegerHashMap.put(b.getGerecht(), gerechtIntegerHashMap.getOrDefault(b.getGerecht(), 0) + b.getAantal());
            }
            List<Gerecht> keylist = gerechtIntegerHashMap.keySet().stream().sorted().collect(Collectors.toList());
            for (Gerecht g :
                    keylist) {
                Integer aantal = gerechtIntegerHashMap.get(g);
                PdfPTable table = new PdfPTable(2);
                table.addCell(getCell(g.getNaam(), new Font(Font.FontFamily.TIMES_ROMAN, 18)));
                table.addCell(getCell(aantal+"", new Font(Font.FontFamily.TIMES_ROMAN, 18)));
                document.add(table);

            }
            System.out.println(t);
        }
        
        
        
        document.close();
    }

    public static void makeTafelverdelingPDF(List<Klant> klanten, String path) throws FileNotFoundException, DocumentException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(".." + File.separator  + path));
        document.open();
        Paragraph paragraph = new Paragraph("Tafelverdeling", new Font(Font.FontFamily.TIMES_ROMAN, 35, Font.BOLD));
        paragraph.add(new LineSeparator(5F, 100, BaseColor.BLACK, 6, -12));
        document.add(paragraph);
        document.add(Chunk.NEWLINE);
        HashMap<Tijdstip, List<Klant>> tijdstipKlantHashMap = new HashMap<>();
        for (Klant k :
                klanten) {
            if (tijdstipKlantHashMap.containsKey(k.getTijdstip())){
                List<Klant> klants = tijdstipKlantHashMap.get(k.getTijdstip());
                klants.add(k);
                tijdstipKlantHashMap.put(k.getTijdstip(), klants);
            }else {
                List<Klant> klants = new ArrayList<Klant>();
                klants.add(k);
                tijdstipKlantHashMap.put(k.getTijdstip(), klants);
            }
        }

        List<Tijdstip> tijdstipList = new ArrayList<>(tijdstipKlantHashMap.keySet());
        tijdstipList = tijdstipList.stream().sorted(Tijdstip::compareTo).collect(Collectors.toList());

        for (Tijdstip t :
                tijdstipList) {
            document.add(new Paragraph("vanaf " + t + " uur", new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD)));
            document.add(new LineSeparator(3F, 100F, BaseColor.BLACK, 6, -5));
            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            Font font = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
            table.addCell(getCell("Tijdstip", font));
            table.addCell(getCell("naam", font));
            table.addCell(getCell("groepsnaam", font));
            table.addCell(getCell("volwassenen", font));
            table.addCell(getCell("kinderen", font));
            table.addCell(getCell("totaal", font));

            for (Klant k :
                    tijdstipKlantHashMap.get(t)) {
                table.addCell(getCell(k.getTijdstip().toString(), PdfPCell.ALIGN_BASELINE));
                table.addCell(getCell(k.getNaam(), PdfPCell.ALIGN_BASELINE));
                table.addCell(getCell(k.getGroepsNaam(), PdfPCell.ALIGN_BASELINE));
                table.addCell(getCell(k.getVolwassenen()+"", PdfPCell.ALIGN_BASELINE));
                table.addCell(getCell(k.getKinderen()+"", PdfPCell.ALIGN_BASELINE));
                table.addCell(getCell(k.getAantalPers()+"", PdfPCell.ALIGN_BASELINE));
            }
            document.add(table);
        }

        document.close();
    }

    private static PdfPCell getCell(String text, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setPadding(8);
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }

    private static PdfPCell getCell(String text, Font font){
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(8);
        cell.setHorizontalAlignment(Element.ALIGN_BASELINE);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }
}
