package ir.university.toosi.wtms.web.util;

import ir.university.toosi.tms.model.entity.Role;
import ir.university.toosi.wtms.web.action.role.HandleRoleAction;

import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by omid on 10/15/2015.
 */
@Named
@RequestScoped
public class RoleConverter implements Converter {

    @Inject
    private HandleRoleAction roleAction;

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        if(s != null && s.trim().length() > 0) {
            return roleAction.findById(s);
        } else {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        if(o != null) {
            return String.valueOf(((Role) o).getId());
        }
        else {
            return null;
        }

    }
}
