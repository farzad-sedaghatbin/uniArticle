package ir.university.toosi.wtms.web.filter;

import ir.university.toosi.wtms.web.helper.GeneralHelper;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * Created by O_Javaheri on 10/26/2015.
 */
@WebFilter("/modena-1.0.1/")
public class AuthenticationFilter implements Filter {


    @Inject
    private GeneralHelper generalHelper;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("hi");
    }

    @Override
    public void destroy() {

    }
}
