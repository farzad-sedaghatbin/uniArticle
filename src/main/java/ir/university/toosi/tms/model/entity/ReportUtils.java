package ir.university.toosi.tms.model.entity;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.languages.ArabicLigaturizer;
import com.itextpdf.text.pdf.languages.LanguageProcessor;
import ir.university.toosi.tms.model.entity.personnel.Card;
import ir.university.toosi.tms.model.entity.personnel.Person;
import ir.university.toosi.tms.model.entity.zone.Gateway;
import ir.university.toosi.tms.util.DateConverterUtils;
import ir.university.toosi.tms.util.LangUtil;
import ir.university.toosi.wtms.web.util.CalendarUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * @author : Hamed Hatami , Javad Sarhadi , Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */
public class ReportUtils<T extends BaseEntity> {

    private static final String CVS_SEPERATOR_CHAR = "\t";
    private static final String NEW_LINE_CHARACTER = "\r\n";
    public static final String FONT = "/fonts/arial.ttf";

    public void exportPDF(List<T> data, List<String> titles, String reportBase) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Document document = new Document();
            document.addTitle("User Report PDF");
            document.addSubject("User Report PDF");
            document.addAuthor("javad sarhadi");
            document.addCreator("javad sarhadi");
            PdfWriter.getInstance(document, byteArrayOutputStream);
            document.open();
            BaseFont courier = BaseFont.createFont(FONT, BaseFont.IDENTITY_H, true);
            Font subFont = new Font(courier, 12, Font.NORMAL);
            Paragraph catPart = new Paragraph();
            LanguageProcessor al = new ArabicLigaturizer();
            PdfPTable table = new PdfPTable(titles.size());
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
            if (reportBase.equalsIgnoreCase("person")) {
                for (int i = titles.size() - 1; i > -1; i--) {
                    PdfPCell c1 = new PdfPCell(new Phrase(al.process(titles.get(i)), subFont));

                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);

                }
                table.setHeaderRows(1);
                for (T report : data) {
                    ReportEntity reportEntity = (ReportEntity) report;
                    PdfPCell c1 = new PdfPCell(new Phrase(al.process(reportEntity.getSuccess()), subFont));

                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);
                    c1 = new PdfPCell(new Phrase(al.process(reportEntity.getEntryType()), subFont));

                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);
                    c1 = new PdfPCell(new Phrase(al.process(reportEntity.getTrafficTime()), subFont));

                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);
                    c1 = new PdfPCell(new Phrase(al.process(DateConverterUtils.getWithSlash(reportEntity.getTrafficDate())), subFont));

                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);
                    /*c1 = new PdfPCell(new Phrase(al.process(reportEntity.getPdpName()), subFont));
                     
                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);*/
                    c1 = new PdfPCell(new Phrase(al.process(reportEntity.getGateName()), subFont));

                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);
/*                    c1 = new PdfPCell(new Phrase(al.process(reportEntity.getZoneName()), subFont));
                     
                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);*/
/*                    c1 = new PdfPCell(new Phrase(al.process(reportEntity.getPost()), subFont));
                     
                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);*/
                    c1 = new PdfPCell(new Phrase(al.process(reportEntity.getOrganName()), subFont));

                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);
                    c1 = new PdfPCell(new Phrase(al.process(reportEntity.getLastName()), subFont));

                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);
                    c1 = new PdfPCell(new Phrase(al.process(reportEntity.getName()), subFont));

                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);
                }
            } else if (reportBase.equalsIgnoreCase("user")) {
                for (int i = titles.size() - 1; i > -1; i--) {
                    PdfPCell c1 = new PdfPCell(new Phrase(al.process(titles.get(i)), subFont));

                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);

                }
                table.setHeaderRows(1);
                for (T usr : data) {
                    User user = (User) usr;
                    PdfPCell c1 = new PdfPCell(new Phrase(al.process(user.getEnable()), subFont));

                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);
                    c1 = new PdfPCell(new Phrase(al.process(user.getUsername()), subFont));

                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);
                }
            } else if (reportBase.equalsIgnoreCase("gateway")) {
                for (int i = titles.size() - 1; i > -1; i--) {
                    PdfPCell c1 = new PdfPCell(new Phrase(al.process(titles.get(i)), subFont));

                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);

                }
                table.setHeaderRows(1);
                for (T report : data) {
                    ReportEntity reportEntity = (ReportEntity) report;
                    PdfPCell c1 = new PdfPCell(new Phrase(al.process(reportEntity.getSuccess()), subFont));

                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);
                    c1 = new PdfPCell(new Phrase(al.process(reportEntity.getEntryType()), subFont));

                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);
                    c1 = new PdfPCell(new Phrase(al.process(reportEntity.getTrafficTime()), subFont));

                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);
                    c1 = new PdfPCell(new Phrase(al.process(DateConverterUtils.getWithSlash(reportEntity.getTrafficDate())), subFont));

                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);
                    /*c1 = new PdfPCell(new Phrase(al.process(reportEntity.getPdpName()), subFont));
                     
                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);*/
                    c1 = new PdfPCell(new Phrase(al.process(reportEntity.getGateName()), subFont));

                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);
/*                    c1 = new PdfPCell(new Phrase(al.process(reportEntity.getZoneName()), subFont));
                     
                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);*/
/*                    c1 = new PdfPCell(new Phrase(al.process(reportEntity.getPost()), subFont));
                     
                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);*/
                    c1 = new PdfPCell(new Phrase(al.process(reportEntity.getOrganName()), subFont));

                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);
                    c1 = new PdfPCell(new Phrase(al.process(reportEntity.getLastName()), subFont));

                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);
                    c1 = new PdfPCell(new Phrase(al.process(reportEntity.getName()), subFont));

                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);
                }
            } else if (reportBase.equalsIgnoreCase("zone")) {
                for (int i = titles.size() - 1; i > -1; i--) {
                    PdfPCell c1 = new PdfPCell(new Phrase(al.process(titles.get(i)), subFont));

                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);

                }
                table.setHeaderRows(1);
                for (T report : data) {
                    ReportEntity reportEntity = (ReportEntity) report;
                    PdfPCell c1 = new PdfPCell(new Phrase(al.process(reportEntity.getSuccess()), subFont));

                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);
                    c1 = new PdfPCell(new Phrase(al.process(reportEntity.getEntryType()), subFont));

                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);
                    c1 = new PdfPCell(new Phrase(al.process(reportEntity.getTrafficTime()), subFont));

                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);
                    c1 = new PdfPCell(new Phrase(al.process(DateConverterUtils.getWithSlash(reportEntity.getTrafficDate())), subFont));

                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);
                    /*c1 = new PdfPCell(new Phrase(al.process(reportEntity.getPdpName()), subFont));
                     
                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);*/
                   /* c1 = new PdfPCell(new Phrase(al.process(reportEntity.getGateName()), subFont));
                     
                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);*/
                    c1 = new PdfPCell(new Phrase(al.process(reportEntity.getZoneName()), subFont));

                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);
                    /*c1 = new PdfPCell(new Phrase(al.process(reportEntity.getPost()), subFont));
                     
                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);*/
                    c1 = new PdfPCell(new Phrase(al.process(reportEntity.getOrganName()), subFont));

                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);
                    c1 = new PdfPCell(new Phrase(al.process(reportEntity.getLastName()), subFont));

                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);
                    c1 = new PdfPCell(new Phrase(al.process(reportEntity.getName()), subFont));

                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);
                }
            } else if (reportBase.equalsIgnoreCase("card")) {
                for (int i = titles.size() - 1; i > -1; i--) {
                    PdfPCell c1 = new PdfPCell(new Phrase(al.process(titles.get(i)), subFont));

                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);

                }
                table.setHeaderRows(1);
                for (T report : data) {
                    ReportEntity reportEntity = (ReportEntity) report;
                    PdfPCell c1 = new PdfPCell(new Phrase(al.process(reportEntity.getSuccess()), subFont));

                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);
                    c1 = new PdfPCell(new Phrase(al.process(reportEntity.getEntryType()), subFont));

                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);
                    c1 = new PdfPCell(new Phrase(al.process(reportEntity.getTrafficTime()), subFont));

                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);
                    c1 = new PdfPCell(new Phrase(al.process(DateConverterUtils.getWithSlash(reportEntity.getTrafficDate())), subFont));

                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);
                    /*c1 = new PdfPCell(new Phrase(al.process(reportEntity.getPdpName()), subFont));
                     
                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);*/
                    c1 = new PdfPCell(new Phrase(al.process(reportEntity.getGateName()), subFont));

                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);
/*                    c1 = new PdfPCell(new Phrase(al.process(reportEntity.getZoneName()), subFont));
                     
                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);*/
/*                    c1 = new PdfPCell(new Phrase(al.process(reportEntity.getPost()), subFont));
                     
                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);*/
                    c1 = new PdfPCell(new Phrase(al.process(reportEntity.getOrganName()), subFont));

                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);
                    c1 = new PdfPCell(new Phrase(al.process(reportEntity.getLastName()), subFont));

                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);
                    c1 = new PdfPCell(new Phrase(al.process(reportEntity.getName()), subFont));

                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);
                }
            } else if (reportBase.equalsIgnoreCase("pdp")) {
                for (int i = titles.size() - 1; i > -1; i--) {
                    PdfPCell c1 = new PdfPCell(new Phrase(al.process(titles.get(i)), subFont));

                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);

                }
                table.setHeaderRows(1);
                for (T report : data) {
                    ReportEntity reportEntity = (ReportEntity) report;
                    PdfPCell c1 = new PdfPCell(new Phrase(al.process(reportEntity.getSuccess()), subFont));

                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);
                    c1 = new PdfPCell(new Phrase(al.process(reportEntity.getEntryType()), subFont));

                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);
                    c1 = new PdfPCell(new Phrase(al.process(reportEntity.getTrafficTime()), subFont));

                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);
                    c1 = new PdfPCell(new Phrase(al.process(DateConverterUtils.getWithSlash(reportEntity.getTrafficDate())), subFont));

                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);
                    c1 = new PdfPCell(new Phrase(al.process(reportEntity.getPdpName()), subFont));

                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);
                   /* c1 = new PdfPCell(new Phrase(al.process(reportEntity.getGateName()), subFont));
                     
                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);*/
/*                    c1 = new PdfPCell(new Phrase(al.process(reportEntity.getZoneName()), subFont));
                     
                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);*/
/*                    c1 = new PdfPCell(new Phrase(al.process(reportEntity.getPost()), subFont));
                     
                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);*/
                    c1 = new PdfPCell(new Phrase(al.process(reportEntity.getOrganName()), subFont));

                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);
                    c1 = new PdfPCell(new Phrase(al.process(reportEntity.getLastName()), subFont));

                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);
                    c1 = new PdfPCell(new Phrase(al.process(reportEntity.getName()), subFont));

                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);
                }
            } else if (reportBase.equalsIgnoreCase("time")) {
                for (int i = titles.size() - 1; i > -1; i--) {
                    PdfPCell c1 = new PdfPCell(new Phrase(al.process(titles.get(i)), subFont));

                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);
                }
                table.setHeaderRows(1);
                for (T report : data) {
                    ReportEntity reportEntity = (ReportEntity) report;
                    PdfPCell c1 = new PdfPCell(new Phrase(al.process(reportEntity.getSuccess()), subFont));

                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);
                    c1 = new PdfPCell(new Phrase(al.process(reportEntity.getEntryType()), subFont));

                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);
                    c1 = new PdfPCell(new Phrase(al.process(reportEntity.getTrafficTime()), subFont));

                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);
                    c1 = new PdfPCell(new Phrase(al.process(DateConverterUtils.getWithSlash(reportEntity.getTrafficDate())), subFont));

                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);
                    /*c1 = new PdfPCell(new Phrase(al.process(reportEntity.getPdpName()), subFont));
                     
                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);*/
                    c1 = new PdfPCell(new Phrase(al.process(reportEntity.getGateName()), subFont));

                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);
/*                    c1 = new PdfPCell(new Phrase(al.process(reportEntity.getZoneName()), subFont));
                     
                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);*/
/*                    c1 = new PdfPCell(new Phrase(al.process(reportEntity.getPost()), subFont));
                     
                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);*/
                    c1 = new PdfPCell(new Phrase(al.process(reportEntity.getOrganName()), subFont));

                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);
                    c1 = new PdfPCell(new Phrase(al.process(reportEntity.getLastName()), subFont));

                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);
                    c1 = new PdfPCell(new Phrase(al.process(reportEntity.getName()), subFont));

                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);
                }
            } else if (reportBase.equalsIgnoreCase("staticPerson")) {
                for (int i = titles.size() - 1; i > -1; i--) {
                    PdfPCell c1 = new PdfPCell(new Phrase(al.process(titles.get(i)), subFont));

                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);
                }
                table.setHeaderRows(1);
                if (data.get(0) instanceof Person) {
                    for (T report : data) {
                        Person reportEntity = (Person) report;
                        PdfPCell c1 = new PdfPCell(new Phrase(al.process(reportEntity.getNationalCode()), subFont));

                   /* c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);
                    c1 = new PdfPCell(new Phrase(al.process(reportEntity.getEmail()), subFont));

                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);
                    c1 = new PdfPCell(new Phrase(al.process(reportEntity.getAddress()), subFont));

                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);
                    c1 = new PdfPCell(new Phrase(al.process(reportEntity.getMobile()), subFont));

                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);*/
                    /*c1 = new PdfPCell(new Phrase(al.process(reportEntity.getPdpName()), subFont));
                     
                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);*/
                        /*c1 = new PdfPCell(new Phrase(al.process(reportEntity.getPersonnelNo()), subFont));

                        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        table.addCell(c1);*/
/*                    c1 = new PdfPCell(new Phrase(al.process(reportEntity.getZoneName()), subFont));
                     
                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);*/
/*                    c1 = new PdfPCell(new Phrase(al.process(reportEntity.getPost()), subFont));
                     
                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);*/
                        c1 = new PdfPCell(new Phrase(al.process(reportEntity.getLastName()), subFont));

                        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        table.addCell(c1);
                        c1 = new PdfPCell(new Phrase(al.process(reportEntity.getName()), subFont));

                        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        table.addCell(c1);
                    }
                } else {
                    for (T report : data) {
                        ReportEntity reportEntity = (ReportEntity) report;
                        PdfPCell c1 = new PdfPCell(new Phrase(al.process(reportEntity.getLastName()), subFont));
                        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        table.addCell(c1);
                        c1 = new PdfPCell(new Phrase(al.process(reportEntity.getName()), subFont));

                        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        table.addCell(c1);
                    }
                }
            } else if (reportBase.equalsIgnoreCase("staticCard")) {
                for (int i = titles.size() - 1; i > -1; i--) {
                    PdfPCell c1 = new PdfPCell(new Phrase(al.process(titles.get(i)), subFont));

                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);
                }
                table.setHeaderRows(1);
                if (data.get(0) instanceof Card) {
                    for (T report : data) {
                        Card reportEntity = (Card) report;
                        PdfPCell c1 = new PdfPCell(new Phrase(al.process(String.valueOf(reportEntity.getCode())), subFont));
                        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        table.addCell(c1);
                        c1 = new PdfPCell(new Phrase(al.process(reportEntity.getPerson().getLastName()), subFont));
                        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        table.addCell(c1);
                        c1 = new PdfPCell(new Phrase(al.process(reportEntity.getPerson().getName()), subFont));
                        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        table.addCell(c1);
                    }
                } else {
                    for (T report : data) {
                        ReportEntity reportEntity = (ReportEntity) report;
                        PdfPCell c1 = new PdfPCell(new Phrase(al.process(reportEntity.getCardCode()), subFont));
                        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        table.addCell(c1);
                        c1 = new PdfPCell(new Phrase(al.process(reportEntity.getLastName()), subFont));
                        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        table.addCell(c1);
                        c1 = new PdfPCell(new Phrase(al.process(reportEntity.getName()), subFont));
                        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        table.addCell(c1);
                    }
                }
            } else if (reportBase.equalsIgnoreCase("staticGateway")) {
                for (int i = titles.size() - 1; i > -1; i--) {
                    PdfPCell c1 = new PdfPCell(new Phrase(al.process(titles.get(i)), subFont));

                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c1);
                }
                table.setHeaderRows(1);
                if (data.get(0) instanceof Gateway) {
                    for (T report : data) {
                        Gateway reportEntity = (Gateway) report;
                        PdfPCell c1 = new PdfPCell(new Phrase(al.process(reportEntity.getName()), subFont));
                        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        table.addCell(c1);
                    }
                } else {
                    for (T report : data) {
                        ReportEntity reportEntity = (ReportEntity) report;
                        PdfPCell c1 = new PdfPCell(new Phrase(al.process(reportEntity.getGateName()), subFont));
                        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        table.addCell(c1);
                        c1 = new PdfPCell(new Phrase(al.process(reportEntity.getLastName()), subFont));
                        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        table.addCell(c1);
                        c1 = new PdfPCell(new Phrase(al.process(reportEntity.getName()), subFont));
                        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        table.addCell(c1);
                    }
                }
            }
            catPart.add(table);
            document.add(catPart);
            document.close();
            byteArrayOutputStream.close();
            readyForDownload(byteArrayOutputStream.toByteArray(), "pdf", LangUtil.getEnglishNumber(CalendarUtil.getDateWithoutSlash(new Date(), new Locale("fa"), "yyyyMMdd")) + ".pdf");
        } catch (
                Exception e
                )

        {
            e.printStackTrace();
        }

    }

    public void exportExcel(List<T> data, List<String> titles, String reportBase) {
        Workbook currentWorkbook = null;
        try {
            currentWorkbook = new HSSFWorkbook();
            Sheet sheet = currentWorkbook.createSheet("گزارش");
            sheet.autoSizeColumn(0);

            if (reportBase.equalsIgnoreCase("person")) {
                int rowCounter = 0;
                Row row = sheet.createRow(rowCounter);
                int cellCounter = 0;
                int rowSize = titles.size();
                Cell cell;
                cell = row.createCell(cellCounter);

                for (String title : titles) {
                    cell = row.createCell(cellCounter);
                    cellCounter++;
                    cell.setCellValue(title);
                }

                for (T report : data) {
                    ReportEntity reportEntity = (ReportEntity) report;
                    rowCounter++;
                    row = sheet.createRow(rowCounter);
                    cellCounter = 0;
                    cell = row.createCell(cellCounter);
                    String s1 = reportEntity.getName();
                    cell.setCellValue(s1);
                    cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getLastName();
                    cell.setCellValue(s1);
                    cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getOrganName();
                    cell.setCellValue(s1);
                    /*cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getPost();
                    cell.setCellValue(s1);*/
                    cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getGateName();
                    cell.setCellValue(s1);
                    /*cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getZoneName();
                    cell.setCellValue(s1);*/
                   /* cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getPdpName();
                    cell.setCellValue(s1);*/
                    cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = DateConverterUtils.getWithSlash(reportEntity.getTrafficDate());
                    cell.setCellValue(s1);
                    cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getTrafficTime();
                    cell.setCellValue(s1);
                    cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getEntryType();
                    cell.setCellValue(s1);
                    cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getSuccess();
                    cell.setCellValue(s1);
                }
                for (int i = 0; i < titles.size(); i++) {
                    sheet.autoSizeColumn(i);
                }

                for (int i = titles.size(); i < (rowSize + titles.size()); i++) {
                    sheet.autoSizeColumn(i);
                }

            } else if (reportBase.equalsIgnoreCase("user")) {
                int rowCounter = 0;
                Row row = sheet.createRow(rowCounter);
                int cellCounter = 0;
                int rowSize = titles.size();
                Cell cell;
                cell = row.createCell(cellCounter);

                for (String title : titles) {
                    cell = row.createCell(cellCounter);
                    cellCounter++;
                    cell.setCellValue(title);
                }

                for (T report : data) {
                    ReportEntity reportEntity = (ReportEntity) report;
                    rowCounter++;
                    row = sheet.createRow(rowCounter);
                    cellCounter = 0;
                    cell = row.createCell(cellCounter);
                    String s1 = reportEntity.getName();
                    cell.setCellValue(s1);
                    cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getLastName();
                    cell.setCellValue(s1);
                    cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getOrganName();
                    cell.setCellValue(s1);
                    /*cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getPost();
                    cell.setCellValue(s1);*/
                    cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getGateName();
                    cell.setCellValue(s1);
                    /*cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getZoneName();
                    cell.setCellValue(s1);*/
                   /* cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getPdpName();
                    cell.setCellValue(s1);*/
                    cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = DateConverterUtils.getWithSlash(reportEntity.getTrafficDate());
                    cell.setCellValue(s1);
                    cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getTrafficTime();
                    cell.setCellValue(s1);
                    cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getEntryType();
                    cell.setCellValue(s1);
                    cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getSuccess();
                    cell.setCellValue(s1);
                }
                for (int i = 0; i < titles.size(); i++) {
                    sheet.autoSizeColumn(i);
                }

                for (int i = titles.size(); i < (rowSize + titles.size()); i++) {
                    sheet.autoSizeColumn(i);
                }
            } else if (reportBase.equalsIgnoreCase("gateway")) {
                int rowCounter = 0;
                Row row = sheet.createRow(rowCounter);
                int cellCounter = 0;
                int rowSize = titles.size();
                Cell cell;
                cell = row.createCell(cellCounter);

                for (String title : titles) {
                    cell = row.createCell(cellCounter);
                    cellCounter++;
                    cell.setCellValue(title);
                }

                for (T report : data) {
                    ReportEntity reportEntity = (ReportEntity) report;
                    rowCounter++;
                    row = sheet.createRow(rowCounter);
                    cellCounter = 0;
                    cell = row.createCell(cellCounter);
                    String s1 = reportEntity.getName();
                    cell.setCellValue(s1);
                    cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getLastName();
                    cell.setCellValue(s1);
                    cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getOrganName();
                    cell.setCellValue(s1);
                    /*cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getPost();
                    cell.setCellValue(s1);*/
                    cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getGateName();
                    cell.setCellValue(s1);
                    /*cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getZoneName();
                    cell.setCellValue(s1);*/
                   /* cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getPdpName();
                    cell.setCellValue(s1);*/
                    cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = DateConverterUtils.getWithSlash(reportEntity.getTrafficDate());
                    cell.setCellValue(s1);
                    cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getTrafficTime();
                    cell.setCellValue(s1);
                    cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getEntryType();
                    cell.setCellValue(s1);
                    cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getSuccess();
                    cell.setCellValue(s1);
                }
                for (int i = 0; i < titles.size(); i++) {
                    sheet.autoSizeColumn(i);
                }

                for (int i = titles.size(); i < (rowSize + titles.size()); i++) {
                    sheet.autoSizeColumn(i);
                }
            } else if (reportBase.equalsIgnoreCase("zone")) {
                int rowCounter = 0;
                Row row = sheet.createRow(rowCounter);
                int cellCounter = 0;
                int rowSize = titles.size();
                Cell cell;
                cell = row.createCell(cellCounter);

                for (String title : titles) {
                    cell = row.createCell(cellCounter);
                    cellCounter++;
                    cell.setCellValue(title);
                }

                for (T report : data) {
                    ReportEntity reportEntity = (ReportEntity) report;
                    rowCounter++;
                    row = sheet.createRow(rowCounter);
                    cellCounter = 0;
                    cell = row.createCell(cellCounter);
                    String s1 = reportEntity.getName();
                    cell.setCellValue(s1);
                    cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getLastName();
                    cell.setCellValue(s1);
                    cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getOrganName();
                    cell.setCellValue(s1);
                    /*cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getPost();
                    cell.setCellValue(s1);*/
                    /*cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getGateName();
                    cell.setCellValue(s1);*/
                    cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getZoneName();
                    cell.setCellValue(s1);
                   /* cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getPdpName();
                    cell.setCellValue(s1);*/
                    cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = DateConverterUtils.getWithSlash(reportEntity.getTrafficDate());
                    cell.setCellValue(s1);
                    cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getTrafficTime();
                    cell.setCellValue(s1);
                    cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getEntryType();
                    cell.setCellValue(s1);
                    cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getSuccess();
                    cell.setCellValue(s1);
                }
                for (int i = 0; i < titles.size(); i++) {
                    sheet.autoSizeColumn(i);
                }

                for (int i = titles.size(); i < (rowSize + titles.size()); i++) {
                    sheet.autoSizeColumn(i);
                }
            } else if (reportBase.equalsIgnoreCase("card")) {
                int rowCounter = 0;
                Row row = sheet.createRow(rowCounter);
                int cellCounter = 0;
                int rowSize = titles.size();
                Cell cell;
                cell = row.createCell(cellCounter);

                for (String title : titles) {
                    cell = row.createCell(cellCounter);
                    cellCounter++;
                    cell.setCellValue(title);
                }

                for (T report : data) {
                    ReportEntity reportEntity = (ReportEntity) report;
                    rowCounter++;
                    row = sheet.createRow(rowCounter);
                    cellCounter = 0;
                    cell = row.createCell(cellCounter);
                    String s1 = reportEntity.getName();
                    cell.setCellValue(s1);
                    cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getLastName();
                    cell.setCellValue(s1);
                    cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getOrganName();
                    cell.setCellValue(s1);
                    /*cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getPost();
                    cell.setCellValue(s1);*/
                    cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getGateName();
                    cell.setCellValue(s1);
                    /*cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getZoneName();
                    cell.setCellValue(s1);*/
                   /* cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getPdpName();
                    cell.setCellValue(s1);*/
                    cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = DateConverterUtils.getWithSlash(reportEntity.getTrafficDate());
                    cell.setCellValue(s1);
                    cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getTrafficTime();
                    cell.setCellValue(s1);
                    cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getEntryType();
                    cell.setCellValue(s1);
                    cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getSuccess();
                    cell.setCellValue(s1);
                }
                for (int i = 0; i < titles.size(); i++) {
                    sheet.autoSizeColumn(i);
                }

                for (int i = titles.size(); i < (rowSize + titles.size()); i++) {
                    sheet.autoSizeColumn(i);
                }
            } else if (reportBase.equalsIgnoreCase("pdp")) {
                int rowCounter = 0;
                Row row = sheet.createRow(rowCounter);
                int cellCounter = 0;
                int rowSize = titles.size();
                Cell cell;
                cell = row.createCell(cellCounter);

                for (String title : titles) {
                    cell = row.createCell(cellCounter);
                    cellCounter++;
                    cell.setCellValue(title);
                }

                for (T report : data) {
                    ReportEntity reportEntity = (ReportEntity) report;
                    rowCounter++;
                    row = sheet.createRow(rowCounter);
                    cellCounter = 0;
                    cell = row.createCell(cellCounter);
                    String s1 = reportEntity.getName();
                    cell.setCellValue(s1);
                    cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getLastName();
                    cell.setCellValue(s1);
                    cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getOrganName();
                    cell.setCellValue(s1);
                    /*cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getPost();
                    cell.setCellValue(s1);*/
                   /* cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getGateName();
                    cell.setCellValue(s1);*/
                    /*cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getZoneName();
                    cell.setCellValue(s1);*/
                    cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getPdpName();
                    cell.setCellValue(s1);
                    cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = DateConverterUtils.getWithSlash(reportEntity.getTrafficDate());
                    cell.setCellValue(s1);
                    cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getTrafficTime();
                    cell.setCellValue(s1);
                    cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getEntryType();
                    cell.setCellValue(s1);
                    cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getSuccess();
                    cell.setCellValue(s1);
                }
                for (int i = 0; i < titles.size(); i++) {
                    sheet.autoSizeColumn(i);
                }

                for (int i = titles.size(); i < (rowSize + titles.size()); i++) {
                    sheet.autoSizeColumn(i);
                }
            } else if (reportBase.equalsIgnoreCase("time")) {
                int rowCounter = 0;
                Row row = sheet.createRow(rowCounter);
                int cellCounter = 0;
                int rowSize = titles.size();
                Cell cell;
                cell = row.createCell(cellCounter);

                for (String title : titles) {
                    cell = row.createCell(cellCounter);
                    cellCounter++;
                    cell.setCellValue(title);
                }

                for (T report : data) {
                    ReportEntity reportEntity = (ReportEntity) report;
                    rowCounter++;
                    row = sheet.createRow(rowCounter);
                    cellCounter = 0;
                    cell = row.createCell(cellCounter);
                    String s1 = reportEntity.getName();
                    cell.setCellValue(s1);
                    cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getLastName();
                    cell.setCellValue(s1);
                    cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getOrganName();
                    cell.setCellValue(s1);
                    /*cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getPost();
                    cell.setCellValue(s1);*/
                    cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getGateName();
                    cell.setCellValue(s1);
                    /*cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getZoneName();
                    cell.setCellValue(s1);*/
                   /* cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getPdpName();
                    cell.setCellValue(s1);*/
                    cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = DateConverterUtils.getWithSlash(reportEntity.getTrafficDate());
                    cell.setCellValue(s1);
                    cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getTrafficTime();
                    cell.setCellValue(s1);
                    cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getEntryType();
                    cell.setCellValue(s1);
                    cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getSuccess();
                    cell.setCellValue(s1);
                }
                for (int i = 0; i < titles.size(); i++) {
                    sheet.autoSizeColumn(i);
                }

                for (int i = titles.size(); i < (rowSize + titles.size()); i++) {
                    sheet.autoSizeColumn(i);
                }
            } else if (reportBase.equalsIgnoreCase("staticPerson")) {
                int rowCounter = 0;
                Row row = sheet.createRow(rowCounter);
                int cellCounter = 0;
                int rowSize = titles.size();
                Cell cell;
                cell = row.createCell(cellCounter);

                for (String title : titles) {
                    cell = row.createCell(cellCounter);
                    cellCounter++;
                    cell.setCellValue(title);
                }
                if (data.get(0) instanceof Person) {
                    for (T report : data) {
                        Person reportEntity = (Person) report;
                        rowCounter++;
                        row = sheet.createRow(rowCounter);
                        cellCounter = 0;
                        cell = row.createCell(cellCounter);
                        String s1 = reportEntity.getName();
                        cell.setCellValue(s1);
                        cellCounter++;
                        cell = row.createCell(cellCounter);
                        s1 = reportEntity.getLastName();
                        cell.setCellValue(s1);
                    }
                } else {
                    for (T report : data) {
                        ReportEntity reportEntity = (ReportEntity) report;
                        rowCounter++;
                        row = sheet.createRow(rowCounter);
                        cellCounter = 0;
                        cell = row.createCell(cellCounter);
                        String s1 = reportEntity.getName();
                        cell.setCellValue(s1);
                        cellCounter++;
                        cell = row.createCell(cellCounter);
                        s1 = reportEntity.getLastName();
                        cell.setCellValue(s1);
                    }
                }
                for (int i = 0; i < titles.size(); i++) {
                    sheet.autoSizeColumn(i);
                }

                for (int i = titles.size(); i < (rowSize + titles.size()); i++) {
                    sheet.autoSizeColumn(i);
                }
            } else if (reportBase.equalsIgnoreCase("staticCard")) {
                int rowCounter = 0;
                Row row = sheet.createRow(rowCounter);
                int cellCounter = 0;
                int rowSize = titles.size();
                Cell cell;
                cell = row.createCell(cellCounter);

                for (String title : titles) {
                    cell = row.createCell(cellCounter);
                    cellCounter++;
                    cell.setCellValue(title);
                }
                if (data.get(0) instanceof Card) {
                    for (T report : data) {
                        Card reportEntity = (Card) report;
                        rowCounter++;
                        row = sheet.createRow(rowCounter);
                        cellCounter = 0;
                        cell = row.createCell(cellCounter);
                        String s1 = reportEntity.getPerson().getName();
                        cell.setCellValue(s1);
                        cellCounter++;
                        cell = row.createCell(cellCounter);
                        s1 = reportEntity.getPerson().getLastName();
                        cell.setCellValue(s1);
                        cellCounter++;
                        cell = row.createCell(cellCounter);
                        s1 = reportEntity.getCode();
                        cell.setCellValue(s1);
                    }
                } else {
                    for (T report : data) {
                        ReportEntity reportEntity = (ReportEntity) report;
                        rowCounter++;
                        row = sheet.createRow(rowCounter);
                        cellCounter = 0;
                        cell = row.createCell(cellCounter);
                        String s1 = reportEntity.getName();
                        cell.setCellValue(s1);
                        cellCounter++;
                        cell = row.createCell(cellCounter);
                        s1 = reportEntity.getLastName();
                        cell.setCellValue(s1);
                        cellCounter++;
                        cell = row.createCell(cellCounter);
                        s1 = reportEntity.getCardCode();
                        cell.setCellValue(s1);
                    }
                }
                for (int i = 0; i < titles.size(); i++) {
                    sheet.autoSizeColumn(i);
                }

                for (int i = titles.size(); i < (rowSize + titles.size()); i++) {
                    sheet.autoSizeColumn(i);
                }
            } else if (reportBase.equalsIgnoreCase("staticGateway")) {
                int rowCounter = 0;
                Row row = sheet.createRow(rowCounter);
                int cellCounter = 0;
                int rowSize = titles.size();
                Cell cell;
                cell = row.createCell(cellCounter);

                for (String title : titles) {
                    cell = row.createCell(cellCounter);
                    cellCounter++;
                    cell.setCellValue(title);
                }
                if (data.get(0) instanceof Gateway) {
                    for (T report : data) {
                        Gateway reportEntity = (Gateway) report;
                        rowCounter++;
                        row = sheet.createRow(rowCounter);
                        cellCounter = 0;
                        cell = row.createCell(cellCounter);
                        String s1 = reportEntity.getName();
                        cell.setCellValue(s1);
                    }
                } else {
                    for (T report : data) {
                        ReportEntity reportEntity = (ReportEntity) report;
                        rowCounter++;
                        row = sheet.createRow(rowCounter);
                        cellCounter = 0;
                        cell = row.createCell(cellCounter);
                        String s1 = reportEntity.getName();
                        cell.setCellValue(s1);
                        cellCounter++;
                        cell = row.createCell(cellCounter);
                        s1 = reportEntity.getLastName();
                        cell.setCellValue(s1);
                        cellCounter++;
                        cell = row.createCell(cellCounter);
                        s1 = reportEntity.getGateName();
                        cell.setCellValue(s1);
                    }
                }
                for (int i = 0; i < titles.size(); i++) {
                    sheet.autoSizeColumn(i);
                }

                for (int i = titles.size(); i < (rowSize + titles.size()); i++) {
                    sheet.autoSizeColumn(i);
                }
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            currentWorkbook.write(byteArrayOutputStream);
            readyForDownload(byteArrayOutputStream.toByteArray(), "vnd.ms-excel", LangUtil.getEnglishNumber(CalendarUtil.getDateWithoutSlash(new Date(), new Locale("fa"), "yyyyMMdd")) + ".xls");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

    }

    public void exportCsv(List<T> data, List<String> titles, String reportBase) {
        try {
            String fileContent = "";
            int rowSize = 0;
            if (reportBase.equalsIgnoreCase("person")) {
                for (String title : titles) {
                    fileContent += title;
                    fileContent += CVS_SEPERATOR_CHAR;
                }

                for (T report : data) {
                    ReportEntity reportEntity = (ReportEntity) report;
                    fileContent += NEW_LINE_CHARACTER;
                    fileContent += reportEntity.getName();
                    fileContent += CVS_SEPERATOR_CHAR;
                    fileContent += reportEntity.getLastName();
                    fileContent += CVS_SEPERATOR_CHAR;
                    fileContent += reportEntity.getOrganName();
                    fileContent += CVS_SEPERATOR_CHAR;
                    fileContent += reportEntity.getGateName();
                    fileContent += CVS_SEPERATOR_CHAR;
                    fileContent += DateConverterUtils.getWithSlash(reportEntity.getTrafficDate());
                    fileContent += CVS_SEPERATOR_CHAR;
                    fileContent += reportEntity.getTrafficTime();
                    fileContent += CVS_SEPERATOR_CHAR;
                    fileContent += reportEntity.getEntryType();
                    fileContent += CVS_SEPERATOR_CHAR;
                    fileContent += reportEntity.getSuccess();
                    fileContent += CVS_SEPERATOR_CHAR;
                }
            } else if (reportBase.equalsIgnoreCase("user")) {


                for (String title : titles) {
                    fileContent += title;
                    fileContent += CVS_SEPERATOR_CHAR;
                }

                for (T report : data) {
                    ReportEntity reportEntity = (ReportEntity) report;
                    fileContent += NEW_LINE_CHARACTER;
                    fileContent += reportEntity.getName();
                    fileContent += CVS_SEPERATOR_CHAR;
                    fileContent += reportEntity.getLastName();
                    fileContent += CVS_SEPERATOR_CHAR;
                    fileContent += reportEntity.getOrganName();
                    fileContent += CVS_SEPERATOR_CHAR;
                    fileContent += reportEntity.getGateName();
                    fileContent += CVS_SEPERATOR_CHAR;
                    fileContent += DateConverterUtils.getWithSlash(reportEntity.getTrafficDate());
                    fileContent += CVS_SEPERATOR_CHAR;
                    fileContent += reportEntity.getTrafficTime();
                    fileContent += CVS_SEPERATOR_CHAR;
                    fileContent += reportEntity.getEntryType();
                    fileContent += CVS_SEPERATOR_CHAR;
                    fileContent += reportEntity.getSuccess();
                    fileContent += CVS_SEPERATOR_CHAR;
                }
            } else if (reportBase.equalsIgnoreCase("gateway")) {

                for (String title : titles) {
                    fileContent += title;
                    fileContent += CVS_SEPERATOR_CHAR;
                }

                for (T report : data) {
                    ReportEntity reportEntity = (ReportEntity) report;
                    fileContent += NEW_LINE_CHARACTER;
                    fileContent += reportEntity.getName();
                    fileContent += CVS_SEPERATOR_CHAR;
                    fileContent += reportEntity.getLastName();
                    fileContent += CVS_SEPERATOR_CHAR;
                    fileContent += reportEntity.getOrganName();
                    fileContent += CVS_SEPERATOR_CHAR;
                    fileContent += reportEntity.getGateName();
                    fileContent += CVS_SEPERATOR_CHAR;
                    /*cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getZoneName();
                    cell.setCellValue(s1);*/
                   /* cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getPdpName();
                    cell.setCellValue(s1);*/
                    fileContent += DateConverterUtils.getWithSlash(reportEntity.getTrafficDate());
                    fileContent += CVS_SEPERATOR_CHAR;
                    fileContent += reportEntity.getTrafficTime();
                    fileContent += CVS_SEPERATOR_CHAR;
                    fileContent += reportEntity.getEntryType();
                    fileContent += CVS_SEPERATOR_CHAR;
                    fileContent += reportEntity.getSuccess();
                    fileContent += CVS_SEPERATOR_CHAR;
                }
            } else if (reportBase.equalsIgnoreCase("zone")) {

                for (String title : titles) {
                    fileContent += title;
                }

                for (T report : data) {
                    ReportEntity reportEntity = (ReportEntity) report;
                    fileContent += NEW_LINE_CHARACTER;
                    fileContent += reportEntity.getName();
                    fileContent += CVS_SEPERATOR_CHAR;
                    fileContent += reportEntity.getLastName();
                    fileContent += CVS_SEPERATOR_CHAR;
                    fileContent += reportEntity.getOrganName();
                    fileContent += CVS_SEPERATOR_CHAR;
                    fileContent += reportEntity.getZoneName();
                    fileContent += CVS_SEPERATOR_CHAR;

                   /* cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getPdpName();
                    cell.setCellValue(s1);*/
                    fileContent += DateConverterUtils.getWithSlash(reportEntity.getTrafficDate());
                    fileContent += CVS_SEPERATOR_CHAR;
                    fileContent += reportEntity.getTrafficTime();
                    fileContent += CVS_SEPERATOR_CHAR;
                    fileContent += reportEntity.getEntryType();
                    fileContent += CVS_SEPERATOR_CHAR;
                    fileContent += reportEntity.getSuccess();
                    fileContent += CVS_SEPERATOR_CHAR;
                }
            } else if (reportBase.equalsIgnoreCase("card")) {
                for (String title : titles) {
                    fileContent += title;
                    fileContent += CVS_SEPERATOR_CHAR;
                }

                for (T report : data) {
                    ReportEntity reportEntity = (ReportEntity) report;
                    fileContent += NEW_LINE_CHARACTER;
                    fileContent += reportEntity.getName();
                    fileContent += CVS_SEPERATOR_CHAR;
                    fileContent += reportEntity.getLastName();
                    fileContent += CVS_SEPERATOR_CHAR;
                    fileContent += reportEntity.getOrganName();
                    fileContent += CVS_SEPERATOR_CHAR;
                    fileContent += reportEntity.getGateName();
                    fileContent += CVS_SEPERATOR_CHAR;
                    /*cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getZoneName();
                    cell.setCellValue(s1);*/
                   /* cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getPdpName();
                    cell.setCellValue(s1);*/
                    fileContent += DateConverterUtils.getWithSlash(reportEntity.getTrafficDate());
                    fileContent += CVS_SEPERATOR_CHAR;
                    fileContent += reportEntity.getTrafficTime();
                    fileContent += CVS_SEPERATOR_CHAR;
                    fileContent += reportEntity.getEntryType();
                    fileContent += CVS_SEPERATOR_CHAR;
                    fileContent += reportEntity.getSuccess();
                    fileContent += CVS_SEPERATOR_CHAR;
                }
            } else if (reportBase.equalsIgnoreCase("pdp")) {
                for (String title : titles) {
                    fileContent += title;
                    fileContent += CVS_SEPERATOR_CHAR;
                }

                for (T report : data) {
                    ReportEntity reportEntity = (ReportEntity) report;
                    fileContent += NEW_LINE_CHARACTER;
                    fileContent += reportEntity.getName();
                    fileContent += CVS_SEPERATOR_CHAR;
                    fileContent += reportEntity.getLastName();
                    fileContent += CVS_SEPERATOR_CHAR;
                    fileContent += reportEntity.getOrganName();
                    fileContent += CVS_SEPERATOR_CHAR;
                    /*cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getPost();
                    cell.setCellValue(s1);*/
                   /* cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getGateName();
                    cell.setCellValue(s1);*/
                    /*cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getZoneName();
                    cell.setCellValue(s1);*/
                    fileContent += reportEntity.getPdpName();
                    fileContent += CVS_SEPERATOR_CHAR;
                    fileContent += DateConverterUtils.getWithSlash(reportEntity.getTrafficDate());
                    fileContent += CVS_SEPERATOR_CHAR;
                    fileContent += reportEntity.getTrafficTime();
                    fileContent += CVS_SEPERATOR_CHAR;
                    fileContent += reportEntity.getEntryType();
                    fileContent += CVS_SEPERATOR_CHAR;
                    fileContent += reportEntity.getSuccess();
                    fileContent += CVS_SEPERATOR_CHAR;
                }
            } else if (reportBase.equalsIgnoreCase("time")) {

                for (String title : titles) {
                    fileContent += title;
                    fileContent += CVS_SEPERATOR_CHAR;
                }

                for (T report : data) {
                    ReportEntity reportEntity = (ReportEntity) report;
                    fileContent += NEW_LINE_CHARACTER;
                    fileContent += reportEntity.getName();
                    fileContent += CVS_SEPERATOR_CHAR;
                    fileContent += reportEntity.getLastName();
                    fileContent += CVS_SEPERATOR_CHAR;
                    fileContent += reportEntity.getOrganName();
                    fileContent += CVS_SEPERATOR_CHAR;
                    /*cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getPost();
                    cell.setCellValue(s1);*/
                    fileContent += reportEntity.getGateName();
                    fileContent += CVS_SEPERATOR_CHAR;
                    /*cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getZoneName();
                    cell.setCellValue(s1);*/
                   /* cellCounter++;
                    cell = row.createCell(cellCounter);
                    s1 = reportEntity.getPdpName();
                    cell.setCellValue(s1);*/
                    fileContent += DateConverterUtils.getWithSlash(reportEntity.getTrafficDate());
                    fileContent += CVS_SEPERATOR_CHAR;
                    fileContent += reportEntity.getTrafficTime();
                    fileContent += CVS_SEPERATOR_CHAR;
                    fileContent += reportEntity.getEntryType();
                    fileContent += CVS_SEPERATOR_CHAR;
                    fileContent += reportEntity.getSuccess();
                    fileContent += CVS_SEPERATOR_CHAR;
                }
            } else if (reportBase.equalsIgnoreCase("staticPerson")) {

                for (String title : titles) {
                    fileContent += title;
                    fileContent += CVS_SEPERATOR_CHAR;
                }
                if (data.get(0) instanceof Person) {
                    for (T report : data) {
                        Person reportEntity = (Person) report;
                        fileContent += NEW_LINE_CHARACTER;
                        fileContent += reportEntity.getName();
                        fileContent += CVS_SEPERATOR_CHAR;
                        fileContent += reportEntity.getLastName();
                        fileContent += CVS_SEPERATOR_CHAR;
                    }
                } else {
                    for (T report : data) {
                        ReportEntity reportEntity = (ReportEntity) report;
                        fileContent += NEW_LINE_CHARACTER;
                        fileContent += reportEntity.getName();
                        fileContent += CVS_SEPERATOR_CHAR;
                        fileContent += reportEntity.getLastName();
                        fileContent += CVS_SEPERATOR_CHAR;
                    }
                }
            } else if (reportBase.equalsIgnoreCase("staticCard")) {
                for (String title : titles) {
                    fileContent += title;
                    fileContent += CVS_SEPERATOR_CHAR;
                }
                if (data.get(0) instanceof Card) {
                    for (T report : data) {
                        Card reportEntity = (Card) report;
                        fileContent += NEW_LINE_CHARACTER;
                        fileContent += reportEntity.getPerson().getName();
                        fileContent += CVS_SEPERATOR_CHAR;
                        fileContent += reportEntity.getPerson().getLastName();
                        fileContent += CVS_SEPERATOR_CHAR;
                        fileContent += reportEntity.getCode();
                        fileContent += CVS_SEPERATOR_CHAR;
                    }
                } else {
                    for (T report : data) {
                        ReportEntity reportEntity = (ReportEntity) report;
                        fileContent += NEW_LINE_CHARACTER;
                        fileContent += reportEntity.getName();
                        fileContent += CVS_SEPERATOR_CHAR;
                        fileContent += reportEntity.getLastName();
                        fileContent += CVS_SEPERATOR_CHAR;
                        fileContent += reportEntity.getCardCode();
                        fileContent += CVS_SEPERATOR_CHAR;
                    }
                }
            } else if (reportBase.equalsIgnoreCase("staticGateway")) {

                for (String title : titles) {
                    fileContent += title;
                    fileContent += CVS_SEPERATOR_CHAR;
                }
                if (data.get(0) instanceof Gateway) {
                    for (T report : data) {
                        Gateway reportEntity = (Gateway) report;
                        fileContent += NEW_LINE_CHARACTER;
                        fileContent += reportEntity.getName();
                        fileContent += CVS_SEPERATOR_CHAR;
                    }
                } else {
                    for (T report : data) {
                        ReportEntity reportEntity = (ReportEntity) report;
                        fileContent += NEW_LINE_CHARACTER;
                        fileContent += reportEntity.getName();
                        fileContent += CVS_SEPERATOR_CHAR;
                        fileContent += reportEntity.getLastName();
                        fileContent += CVS_SEPERATOR_CHAR;
                        fileContent += reportEntity.getGateName();
                        fileContent += CVS_SEPERATOR_CHAR;
                    }
                }
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(fileContent.length());
            BufferedWriter myWriter = new BufferedWriter(new OutputStreamWriter(byteArrayOutputStream));
            myWriter.write(fileContent);
            myWriter.flush();
            myWriter.close();
            readyForDownload(byteArrayOutputStream.toByteArray(), "text", LangUtil.getEnglishNumber(CalendarUtil.getDateWithoutSlash(new Date(), new Locale("fa"), "yyyyMMdd")) + ".txt");
        } catch (Exception e) {

        }
    }

    public void readyForDownload(byte[] byteArrayOutputStream, String type, String fileName) throws IOException {

        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
        response.setContentType("application/" + type);
        response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
        response.addHeader("Cache-Control", "no-cache");
        response.setContentLength(byteArrayOutputStream.length);
        ServletOutputStream servletOutputStream = response.getOutputStream();
        servletOutputStream.write(byteArrayOutputStream);
        servletOutputStream.flush();
        servletOutputStream.close();
        context.responseComplete();
    }

    private String getCellData(Cell myCell) {
        String cellData = "";
        if (myCell == null) {
            cellData += CVS_SEPERATOR_CHAR;
        } else {
            switch (myCell.getCellType()) {
                case Cell.CELL_TYPE_STRING:
                case Cell.CELL_TYPE_BOOLEAN:
                    cellData += myCell.getRichStringCellValue() + CVS_SEPERATOR_CHAR;
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    cellData += getNumericValue(myCell);
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    cellData += getFormulaValue(myCell);
                default:
                    cellData += CVS_SEPERATOR_CHAR;
            }
        }
        return cellData;
    }

    private String getFormulaValue(Cell myCell) {
        String cellData = "";
        if (myCell.getCachedFormulaResultType() == Cell.CELL_TYPE_STRING || myCell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
            cellData += myCell.getRichStringCellValue() + CVS_SEPERATOR_CHAR;
        } else if (myCell.getCachedFormulaResultType() == Cell.CELL_TYPE_NUMERIC) {
            cellData += getNumericValue(myCell) + CVS_SEPERATOR_CHAR;
        }
        return cellData;
    }

    private String getNumericValue(Cell myCell) {
        String cellData = "";
        cellData += new BigDecimal(myCell.getNumericCellValue()).toString() + CVS_SEPERATOR_CHAR;
        return cellData;
    }

}