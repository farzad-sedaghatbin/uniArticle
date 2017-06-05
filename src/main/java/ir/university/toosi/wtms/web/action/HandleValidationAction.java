package ir.university.toosi.wtms.web.action;

import ir.university.toosi.wtms.web.action.zone.HandleGatewayAction;
import ir.university.toosi.tms.util.Configuration;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Named(value = "handleValidationAction")
@SessionScoped
public class HandleValidationAction implements Serializable {
    @Inject
    private UserManagementAction me;
    @Inject
    private HandleGatewayAction handleGatewayAction;


    public void validateIpFormat(FacesContext context, UIComponent component, Object value) {
        if (value != null && !validateFormat(String.valueOf(value), "ip_regular")) {
            createErrorMessage(context, component, me.getValue("invalid.ip"));
        }
    }

    public void validateEmailFormat(FacesContext context, UIComponent component, Object value) {
        if (value != null && !validateFormat(String.valueOf(value), "email_regular")) {
            createErrorMessage(context, component, me.getValue("invalid.email"));
        }
    }

    private static boolean validateFormat(String value, String regex) {
        Pattern pattern = Pattern.compile(Configuration.getProperty(regex));
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

    private void createErrorMessage(FacesContext context, UIComponent component, String errorMsg) {
        FacesMessage message = new FacesMessage();
        message.setSeverity(FacesMessage.SEVERITY_ERROR);
        message.setSummary(errorMsg);
        message.setDetail(errorMsg);
        context.addMessage(component.getClientId(), message);
        throw new ValidatorException(message);
    }
}

