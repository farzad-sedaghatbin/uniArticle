package ir.university.toosi.wtms.web.util;

import ir.university.toosi.tms.model.entity.PC;
import ir.university.toosi.wtms.web.action.pc.HandlePCAction;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by O_Javaheri on 9/12/2015.
 */
@Named
@RequestScoped
public class PcConverter implements Converter {
    @Inject
    HandlePCAction pcAction;

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        if (s != null && s.trim().length() > 0) {
            try {
//                HandlePCAction workGroupAction= (HandlePCAction) facesContext.getExternalContext().getSessionMap().get("handlePCAction");
                return pcAction.findForConverter(Long.parseLong(s));
            } catch (NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid theme."));
            }
        } else {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        if (o != null) {
            return String.valueOf(((PC) o).getId());
        } else {
            return null;
        }
    }
}
