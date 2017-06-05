package ir.university.toosi.wtms.web.converter;

import ir.university.toosi.tms.model.entity.WorkGroup;
import ir.university.toosi.tms.model.entity.objectValue.init.Gate;
import ir.university.toosi.tms.model.entity.zone.Gateway;
import ir.university.toosi.wtms.web.action.workgroup.HandleWorkGroupAction;
import ir.university.toosi.wtms.web.action.zone.HandleGatewayAction;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by O_Javaheri on 10/17/2015.
 */
@Named
@RequestScoped
public class GatewayConverter implements Converter {
    @Inject
    HandleGatewayAction gatewayAction;
    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        if(s != null && s.trim().length() > 0) {
            try {
//                HandleWorkGroupAction workGroupAction= (HandleWorkGroupAction) facesContext.getExternalContext().getSessionMap().get("handleWorkGroupAction");
                return gatewayAction.findForConverter(s);
            } catch(NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid theme."));
            }
        }
        else {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        if(o != null) {
            return String.valueOf(((Gateway) o).getId());
        }
        else {
            return null;
        }
    }
}
