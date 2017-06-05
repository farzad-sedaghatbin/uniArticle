package ir.university.toosi.wtms.web.servlet;

import ir.university.toosi.wtms.web.action.HandleCommentAction;
import ir.university.toosi.wtms.web.action.HandleSettingAction;
import ir.university.toosi.wtms.web.action.traffic.HandleTrafficAction;
import ir.university.toosi.wtms.web.action.UserManagementAction;
import ir.university.toosi.wtms.web.action.calendar.HandleCalendarAction;
import ir.university.toosi.wtms.web.action.calendar.HandleDayTypeAction;
import ir.university.toosi.wtms.web.action.event.HandleEventAction;
import ir.university.toosi.wtms.web.action.lookup.HandleLookupAction;
import ir.university.toosi.wtms.web.action.map.HandleMapAction;
import ir.university.toosi.wtms.web.action.monitoring.HandleMonitoringAction;
import ir.university.toosi.wtms.web.action.operation.HandleOperationAction;
import ir.university.toosi.wtms.web.action.organ.HandleOrganAction;
import ir.university.toosi.wtms.web.action.pc.HandlePCAction;
import ir.university.toosi.wtms.web.action.person.HandleCardAction;
import ir.university.toosi.wtms.web.action.person.HandlePersonAction;
import ir.university.toosi.wtms.web.action.report.HandleCardReportAction;
import ir.university.toosi.wtms.web.action.role.HandleRoleAction;
import ir.university.toosi.wtms.web.action.rule.HandleRuleAction;
import ir.university.toosi.wtms.web.action.rule.HandleRuleExceptionAction;
import ir.university.toosi.wtms.web.action.user.HandleUserAction;
import ir.university.toosi.wtms.web.action.workgroup.HandleWorkGroupAction;
import ir.university.toosi.wtms.web.action.zone.*;

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
    private HandleCameraAction handleCameraAction;
    @Inject
    private HandleCardAction handleCardAction;
    @Inject
    private HandlePersonAction handlePersonAction;
    @Inject
    private HandleMonitoringAction handleMonitoringAction;
    @Inject
    private HandlePDPAction handlePDPAction;
    @Inject
    private HandleDeviceAction handleDeviceAction;
    @Inject
    private HandleZoneAction handleZoneAction;
    @Inject
    private HandleGatewayAction handleGatewayAction;
    @Inject
    private HandleCommentAction handleCommentAction;
    @Inject
    private HandleOrganAction handleOrganAction;
    @Inject
    private HandleMapAction handleMapAction;
    @Inject
    private HandleCardReportAction handleCardReportAction;
    @Inject
    private HandleEventAction handleEventAction;
    @Inject
    private HandleTrafficAction handleTrafficAction;
    @Inject
    private HandleDayTypeAction handleDayTypeAction;
    @Inject
    private HandleCalendarAction handleCalendarAction;
    @Inject
    private HandleRuleAction handleRuleAction;
    @Inject
    private HandleRuleExceptionAction handleRuleExceptionAction;
    @Inject
    private HandleUserAction handleUserAction;
    @Inject
    private HandleWorkGroupAction handleWorkGroupAction;
    @Inject
    private HandleRoleAction handleRoleAction;
    @Inject
    private HandlePCAction handlePCAction;
    @Inject
    private HandleLookupAction handleLookupAction;
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

        if (request.getParameter("id").equalsIgnoreCase("camera")) {
            handleCameraAction.begin();
            request.getRequestDispatcher("/zone/list-camera.htm").forward(request, response);
        } else if (request.getParameter("id").equalsIgnoreCase("card")) {
            handleCardAction.begin();
            request.getRequestDispatcher("/card/list-card.htm").forward(request, response);
        } else if (request.getParameter("id").equalsIgnoreCase("person")) {
            handlePersonAction.begin();
            request.getRequestDispatcher("/person/list-person.htm").forward(request, response);
        }  else if (request.getParameter("id").equalsIgnoreCase("sentry")) {
            handleMonitoringAction.beginSentry();
            request.getRequestDispatcher("/monitoring/sentry-monitor.htm").forward(request, response);
        } else if (request.getParameter("id").equalsIgnoreCase("pdp")) {
            handlePDPAction.begin();
            request.getRequestDispatcher("/zone/list-pdp.htm").forward(request, response);
        } else if (request.getParameter("id").equalsIgnoreCase("device-monitoring")) {
            handleDeviceAction.beginDevice();
            request.getRequestDispatcher("/monitoring/device-monitoring.htm").forward(request, response);
        } else if (request.getParameter("id").equalsIgnoreCase("invisible-card")) {
            handleCardAction.beginInvisible();
            request.getRequestDispatcher("/card/list-invisible-card.htm").forward(request, response);
        } else if (request.getParameter("id").equalsIgnoreCase("list-assign-card-to-person")) {
            handleCardAction.beginAssignCardToPersonList();
            request.getRequestDispatcher("/card/list-assign-card-to-person.htm").forward(request, response);
        } else if (request.getParameter("id").equalsIgnoreCase("list-zone")) {
            handleZoneAction.begin();
            request.getRequestDispatcher("/zone/list-zone.htm").forward(request, response);
        } else if (request.getParameter("id").equalsIgnoreCase("list-gateway")) {
            handleGatewayAction.begin();
            request.getRequestDispatcher("/zone/list-gateway.htm").forward(request, response);
        } else if (request.getParameter("id").equalsIgnoreCase("authorization")) {
            handleCommentAction.beginAuthorize();
            request.getRequestDispatcher("/authorization/authorization.htm").forward(request, response);
        } else if (request.getParameter("id").equalsIgnoreCase("comment")) {
            handleCommentAction.begin();
            request.getRequestDispatcher("/comment/list-comment.htm").forward(request, response);
        } else if (request.getParameter("id").equalsIgnoreCase("organ")) {
            handleOrganAction.begin();
            request.getRequestDispatcher("/organ/list-organ.htm").forward(request, response);
        } else if (request.getParameter("id").equalsIgnoreCase("report")) {
            handleCardReportAction.begin();
            request.getRequestDispatcher("/report/list-report.htm").forward(request, response);
        } else if (request.getParameter("id").equalsIgnoreCase("saved-query")) {
            handleCardReportAction.saveQueryBegin();
            request.getRequestDispatcher("/report/list-saved-query.htm").forward(request, response);
        } else if (request.getParameter("id").equalsIgnoreCase("event")) {
            handleEventAction.begin();
            request.getRequestDispatcher("/event/list-event.htm").forward(request, response);
        } else if (request.getParameter("id").equalsIgnoreCase("traffic-log")) {
            handleTrafficAction.begin();
            request.getRequestDispatcher("/traffic/traffic-log.htm").forward(request, response);
        } else if (request.getParameter("id").equalsIgnoreCase("day")) {
            handleDayTypeAction.begin();
            request.getRequestDispatcher("/calendar/list-day.htm").forward(request, response);
        } else if (request.getParameter("id").equalsIgnoreCase("calendar")) {
            handleCalendarAction.begin();
            request.getRequestDispatcher("/calendar/list-calendar.htm").forward(request, response);
        } else if (request.getParameter("id").equalsIgnoreCase("rule")) {
            handleRuleAction.begin();
            request.getRequestDispatcher("/business-rules/list-rule.htm").forward(request, response);
        } else if (request.getParameter("id").equalsIgnoreCase("exception-rule")) {
            handleRuleExceptionAction.begin();
            request.getRequestDispatcher("/business-rules/list-exception-rule.htm").forward(request, response);
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
        } else if (request.getParameter("id").equalsIgnoreCase("lookup")) {
            handleLookupAction.begin();
            request.getRequestDispatcher("/lookup/list-lookup.htm").forward(request, response);
        } else if (request.getParameter("id").equalsIgnoreCase("setting")) {
            handleSettingAction.begin();
            request.getRequestDispatcher("/setting/system-setting.htm").forward(request, response);
        } else if (request.getParameter("id").equalsIgnoreCase("backup")) {
            handleOperationAction.backUp();
            request.getRequestDispatcher("/setting/backup-result.htm").forward(request, response);

        } else if (request.getParameter("id").equalsIgnoreCase("map")) {
            handleMapAction.begin();
            request.getRequestDispatcher("/map/list-map.htm").forward(request, response);

        }


    }


}
