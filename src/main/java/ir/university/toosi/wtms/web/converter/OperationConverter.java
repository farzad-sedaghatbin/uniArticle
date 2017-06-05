package ir.university.toosi.wtms.web.converter;

import ir.university.toosi.tms.model.entity.Operation;
import ir.university.toosi.tms.model.service.OperationServiceImpl;
import ir.university.toosi.wtms.web.action.operation.HandleOperationAction;
import ir.university.toosi.wtms.web.action.role.HandleRoleAction;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by O_Javaheri on 9/12/2015.
 */
@Named
@RequestScoped
public class OperationConverter implements Converter {
    @EJB
    OperationServiceImpl operationAction;

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        if (s != null && s.trim().length() > 0) {
            try {
                return operationAction.findById(s);
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
            return String.valueOf(((Operation) o).getId());
        } else {
            return null;
        }
    }
}
