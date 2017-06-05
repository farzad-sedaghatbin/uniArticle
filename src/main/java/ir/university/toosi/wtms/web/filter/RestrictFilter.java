package ir.university.toosi.wtms.web.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author : Hamed Hatami , Arsham Sedaghatbin, Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */


public class RestrictFilter implements Filter {

//    @Inject
//    private GeneralHelper generalHelper;

    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        String path = request.getRequestURI();
        if (path.contains("/login.xhtml") || path.contains("?wsdl") || path.contains(".css") || path.contains(".js")
                || path.contains(".svg") || path.contains("/javax.faces.resource/fonts/")) {
            filterChain.doFilter(request, response);
        } else if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect(request.getContextPath() + "/login.xhtml");
        } else {
            filterChain.doFilter(request, response);
//            User currentUser = generalHelper.getUserService().findByUsername(session.getAttribute("username").toString());
//            User currentUser =  generalHelper.getUserService().findByUsername(session.getAttribute("username").toString());
            boolean flag = true;
//            for(WorkGroup workGroup:currentUser.getWorkGroups()){
//            for (Role role : workGroup.getRoles()) {
//                if (role.getName().equals(request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/") + 1, request.getRequestURI().lastIndexOf(".")))) {
//                    flag = true;
//                    break;
//                }
//                if(flag)
//                    break;
//            }
//            }
//            if (currentUser == null || currentUser.getWorkGroups().iterator().next().getRoles().size() == 0) {
//                flag = false;
//            }
        }
    }

    public void destroy() {

    }
}