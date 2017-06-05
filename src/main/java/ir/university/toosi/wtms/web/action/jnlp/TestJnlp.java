package ir.university.toosi.wtms.web.action.jnlp;


import ir.university.toosi.tms.util.Configuration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author : Mostafa Rastgar
 * @version : 0.8
 */
@WebServlet(urlPatterns = "/jnlp/testAction")
public class TestJnlp extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String base = "http://" + Configuration.getProperty("server.ip") + ":" + Configuration.getProperty("server.port") + getServletContext().getContextPath() + "/jnlp";
        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<jnlp spec=\"1.0+\" codebase=\""+base+"\" href=\"testAction\">\n" +
                "    <information>\n" +
                "        <title>Jnlp Testing</title>\n" +
                "        <vendor>Aria</vendor>\n" +
                "        <homepage href=\""+base+"/\"/>\n" +
                "        <description>Testing Testing</description>\n" +
                "    </information>\n" +
                "    <security>\n" +
                "        <all-permissions/>\n" +
                "    </security>\n" +
                "    <resources>\n" +
                "        <j2se version=\"1.6+\"/>\n" +
                "        <jar href=\"TestJnlp.jar\"/>\n" +
                "    </resources>\n" +
                "    <application-desc main-class=\"ir.university.toosi.wtms.jnlp.CardReader\">\n" +
                "    </application-desc>\n" +
                "</jnlp>";
        byte[] xmlBytes = xml.getBytes("UTF-8");

        res.reset();
        res.setContentType("application/x-java-jnlp-file");
        res.setContentLength(xmlBytes.length);
        res.setHeader("Content-Disposition", "attachment; filename=\"test.jnlp\"");

        OutputStream output = res.getOutputStream();
        output.write(xmlBytes);
        output.flush();
    }
}
