package ir.university.toosi.wtms.web.servlet;

import ir.university.toosi.wtms.web.action.HandleSettingAction;
import ir.university.toosi.wtms.web.action.UserManagementAction;
import ir.university.toosi.wtms.web.action.event.HandleEventAction;
import ir.university.toosi.wtms.web.action.operation.HandleOperationAction;
import ir.university.toosi.wtms.web.action.pc.HandlePCAction;
import ir.university.toosi.wtms.web.action.role.HandleRoleAction;
import ir.university.toosi.wtms.web.action.user.HandleUserAction;
import ir.university.toosi.wtms.web.action.workgroup.HandleWorkGroupAction;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.io.IOException;

@WebServlet(urlPatterns = {"/menuHandler"})
public class MenuHandlerServlet extends HttpServlet {



    @Inject
    private HandleEventAction handleEventAction;

    @Inject
    private HandleUserAction handleUserAction;
    @Inject
    private HandleWorkGroupAction handleWorkGroupAction;
    @Inject
    private HandleRoleAction handleRoleAction;
    @Inject
    private HandlePCAction handlePCAction;


    @Inject
    private HandleSettingAction handleSettingAction;
    @Inject
    private HandleOperationAction handleOperationAction;
    @Inject
    private UserManagementAction me;

    @Override
    public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

  if (request.getParameter("id").equalsIgnoreCase("event")) {
            handleEventAction.begin();
            request.getRequestDispatcher("/event/list-event.htm").forward(request, response);
        } else if (request.getParameter("id").equalsIgnoreCase("user")) {
            handleUserAction.begin();
            request.getRequestDispatcher("/user/list-user.htm").forward(request, response);
        } else if (request.getParameter("id").equalsIgnoreCase("workgroup")) {
            handleWorkGroupAction.begin();
            request.getRequestDispatcher("/workgroup/list-workgroup.htm").forward(request, response);
        } else if (request.getParameter("id").equalsIgnoreCase("role")) {
            handleRoleAction.begin();
            request.getRequestDispatcher("/role/list-role.htm").forward(request, response);
        } else if (request.getParameter("id").equalsIgnoreCase("pc")) {
            handlePCAction.begin();
            request.getRequestDispatcher("/pc/list-pc.htm").forward(request, response);
        } else if (request.getParameter("id").equalsIgnoreCase("setting")) {
            handleSettingAction.begin();
            request.getRequestDispatcher("/setting/system-setting.htm").forward(request, response);
        } else if (request.getParameter("id").equalsIgnoreCase("backup")) {
            handleOperationAction.backUp();
            request.getRequestDispatcher("/setting/backup-result.htm").forward(request, response);

        }


    }


}
