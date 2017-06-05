package ir.university.toosi.wtms.web.servlet;

import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author : Hamed Hatami , Javad Sarhadi , Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 1.0
 */
public abstract class ImageLoaderServlet extends HttpServlet {

    public void service(ServletResponse servletResponse, byte[] img) throws IOException, ServletException {
        if (img != null && img.length != 0) {
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            response.setHeader("Content-Length", String.valueOf(img.length));
            response.setContentType("image/png");
            response.setHeader("Content-Disposition", "attachment;filename=chart.png");
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache,no-store,max-age=0");
            response.setHeader("Expires", "0");
            OutputStream out = response.getOutputStream();
            out.write(img);
            out.flush();
            out.close();
        }

    }
}
