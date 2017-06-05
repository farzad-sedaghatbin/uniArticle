package ir.university.toosi.wtms.web.servlet;

import ir.university.toosi.wtms.web.action.UserManagementAction;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/calendar"})
public class TMSServlet extends HttpServlet {

    @Inject
    UserManagementAction me;

    @Override
    public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String user = request.getParameter("user");


        request.getRequestDispatcher("/home.htm").forward(request, response);

    }
}
