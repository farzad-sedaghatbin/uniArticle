package ir.university.toosi.wtms.web.action;


import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.Serializable;

/**
 * @author : Hamed Hatami , Arsham Sedaghatbin, Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Named(value = "logoutAction")
@SessionScoped
public class LogoutAction implements Serializable {

    @Inject
    private UserManagementAction me;

    public Boolean setCookie(String localeLanguage) {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
            Cookie langCookie = new Cookie("locale", localeLanguage.trim());
            //langCookie.setMaxAge(86400);
            response.addCookie(langCookie);
            return true;
        } catch (Exception ioe) {
            return false;
        }
    }

    public Boolean removeCookie() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        Cookie[] cookieArray = request.getCookies();

        if (cookieArray != null)
            for (Cookie cookie : cookieArray)
                if (cookie.getName().equalsIgnoreCase("locale"))
                    cookie.setMaxAge(0);

        return true;
    }

    public void logout() {

        if (removeCookie())
            setCookie(FacesContext.getCurrentInstance().getViewRoot().getLocale().toString());

        FacesContext faces = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) faces.getExternalContext().getSession(false);
        session.removeAttribute(UserManagementAction.usernameInSession);
        session.removeAttribute(UserManagementAction.INVALID_TRY);
        session.invalidate();

       me.redirect("/login.xhtml");
    }
}