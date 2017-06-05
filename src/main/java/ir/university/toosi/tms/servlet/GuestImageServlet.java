package ir.university.toosi.tms.servlet;

import ir.university.toosi.guest.entity.Guest;
import ir.university.toosi.guest.service.GuestServiceImpl;
import ir.university.toosi.tms.model.entity.personnel.Person;
import ir.university.toosi.wtms.web.helper.GeneralHelper;

import javax.inject.Inject;
import javax.servlet.GenericServlet;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;

/**
 * Created by o_javaheri on 10/3/2015.
 */
@WebServlet(value="/guestImage")
public class GuestImageServlet extends GenericServlet {

    @Inject
    private GuestServiceImpl guestService;
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


            Guest person = guestService.getGuestDao().findById(Long.parseLong(req.getParameter("id")));


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