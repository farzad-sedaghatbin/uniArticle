package ir.university.toosi.wtms.web.servlet;


import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.tms.model.entity.personnel.Person;
import ir.university.toosi.wtms.web.helper.GeneralHelper;
//import ir.university.toosi.tms.model.entity.personnel.Person;
import ir.university.toosi.wtms.web.util.ManagedBeanManager;
import ir.university.toosi.wtms.web.util.RESTfulClientUtil;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;

/**
 * @author : Hamed Hatami , Javad Sarhadi , Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 1.0
 */
@WebServlet(value = "/personImageLoader")
public class PersonImageLoaderServlet extends ImageLoaderServlet {
    @Override
    public void service(ServletRequest servletRequest, ServletResponse response) throws ServletException, IOException {
        Long id = Long.valueOf(servletRequest.getParameter("id"));
        GeneralHelper generalHelper = ManagedBeanManager.lookup(GeneralHelper.class);
        generalHelper.getWebServiceInfo().setServiceName("/findPersonById");
        Person currentPerson = null;
        try {
            currentPerson = new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(generalHelper.getWebServiceInfo().getServerUrl(), generalHelper.getWebServiceInfo().getServiceName(), String.valueOf(id)), Person.class);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        super.service(response, currentPerson.getPicture());
    }
}
