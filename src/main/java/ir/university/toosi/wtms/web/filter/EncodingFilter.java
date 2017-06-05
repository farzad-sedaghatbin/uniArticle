package ir.university.toosi.wtms.web.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class EncodingFilter implements Filter {

    private final static String targetEncoding = "UTF-8";

    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        final HttpServletRequest request = (HttpServletRequest) req;
        request.setCharacterEncoding(targetEncoding);
        final HttpServletResponse response = (HttpServletResponse) res;
        response.setCharacterEncoding(targetEncoding);

        String sessionid = request.getSession().getId();
        String contextPath = request.getContextPath();
        String secure = "";
        if (request.isSecure()) {
            secure = "; Secure";
        }
        response.setHeader("SET-COOKIE", "JSESSIONID=" + sessionid
                + "; Path=" + contextPath + "; HttpOnly" + secure);

        chain.doFilter(request, response);
    }
}