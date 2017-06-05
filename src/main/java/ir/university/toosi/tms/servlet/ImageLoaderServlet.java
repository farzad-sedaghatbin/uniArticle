package ir.university.toosi.tms.servlet;

import ir.university.toosi.tms.model.entity.personnel.Person;
import ir.university.toosi.tms.model.service.personnel.PersonServiceImpl;
import ir.university.toosi.wtms.web.helper.GeneralHelper;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;

/**
 * Created by o_javaheri on 10/3/2015.
 */
@WebServlet(value="/loadImage")
public class ImageLoaderServlet extends GenericServlet {

    @Inject
    private PersonServiceImpl personService;
    @Inject
    private GeneralHelper generalHelper;

    public void service(ServletRequest req, ServletResponse res)

    {
        ServletOutputStream stream = null;
        try {
            stream = res.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {


            Person person = personService.findById(Long.parseLong(req.getParameter("id")));


            if (null != person.getPicture()) {
                stream.write(person.getPicture());
                stream.flush();
            } else {
                stream.write(generalHelper.getAnonymous());
                stream.flush();
            }
            stream.close();
        }catch (Exception e){
            try {
                stream.write(generalHelper.getAnonymous());
                stream.flush();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }


}