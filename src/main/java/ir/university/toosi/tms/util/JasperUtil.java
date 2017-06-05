package ir.university.toosi.tms.util;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Map;

public class JasperUtil {



    public static synchronized void generatePDFWithoutDataSource(String jrxmlFileName, String pdfFileName, Map parameterMap) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", "attachment;filename=" + pdfFileName);
            response.addHeader("Cache-Control", "no-cache");
            ClassLoader loader = JasperUtil.class.getClassLoader();
            InputStream reportStream = loader.getResourceAsStream(jrxmlFileName);
            JasperExportManager.exportReportToPdfStream(JasperFillManager.fillReport(JasperCompileManager.compileReport(reportStream), parameterMap, new JREmptyDataSource()), byteArrayOutputStream);
            response.setContentLength(byteArrayOutputStream.size());
            ServletOutputStream servletOutputStream = response.getOutputStream();
            byteArrayOutputStream.writeTo(servletOutputStream);
            byteArrayOutputStream.flush();
            servletOutputStream.flush();
            servletOutputStream.close();
            byteArrayOutputStream.close();
            reportStream.close();
            facesContext.responseComplete();
        } catch (JRException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void generatePDFWithDataSource(String jrxmlFileName, String pdfFileName, Map parameterMap, List datasourceTemp) {

        try {

            FacesContext facesContext = FacesContext.getCurrentInstance();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", "attachment;filename=" + pdfFileName);
            response.addHeader("Cache-Control", "no-cache");
            ClassLoader loader = JasperUtil.class.getClassLoader();
            InputStream reportStream = loader.getResourceAsStream("reports" + File.separator + jrxmlFileName);
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(datasourceTemp);
            JasperExportManager.exportReportToPdfStream(JasperFillManager.fillReport(JasperCompileManager.compileReport(reportStream), parameterMap, dataSource), byteArrayOutputStream);
            response.setContentLength(byteArrayOutputStream.size());
            ServletOutputStream servletOutputStream = response.getOutputStream();
            byteArrayOutputStream.writeTo(servletOutputStream);
            byteArrayOutputStream.flush();
            servletOutputStream.flush();
            servletOutputStream.close();
            byteArrayOutputStream.close();
            reportStream.close();
            facesContext.responseComplete();
        } catch (JRException e) {
            e.printStackTrace();
        } catch (IOException ex) {

        }
    }

    public static synchronized void generatePDFWithDataSourceSQL(String jrxmlFileName, String pdfFileName, Map parameterMap) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", "attachment;filename=" + pdfFileName);
            response.addHeader("Cache-Control", "no-cache");
            ClassLoader loader = JasperUtil.class.getClassLoader();
            InputStream reportStream = loader.getResourceAsStream("reports" + File.separator + jrxmlFileName);
            JasperDesign jasperDesign = JRXmlLoader.load(reportStream);
            Connection conn = DriverManager.getConnection("jdbc:derby://127.0.0.1:1527/db/kernel;", "kernel", "kernel");
            JasperExportManager.exportReportToPdfStream(JasperFillManager.fillReport(JasperCompileManager.compileReport(jasperDesign), parameterMap, conn), byteArrayOutputStream);
            response.setContentLength(byteArrayOutputStream.size());
            ServletOutputStream servletOutputStream = response.getOutputStream();
            byteArrayOutputStream.writeTo(servletOutputStream);
            byteArrayOutputStream.flush();
            servletOutputStream.flush();
            servletOutputStream.close();
            byteArrayOutputStream.close();
            reportStream.close();
            facesContext.responseComplete();

        } catch (Exception ex) {
            String connectMsg = "Could not create the report " + ex.getMessage() + " " + ex.getLocalizedMessage();
            ex.printStackTrace();
        }

    }
}