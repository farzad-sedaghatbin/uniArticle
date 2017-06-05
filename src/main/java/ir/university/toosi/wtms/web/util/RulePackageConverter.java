package ir.university.toosi.wtms.web.util;

import ir.university.toosi.tms.model.entity.Role;
import ir.university.toosi.tms.model.entity.rule.RulePackage;
import ir.university.toosi.tms.model.service.rule.RulePackageServiceImpl;

import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by O_Javaheri on 11/17/2015.
 */
@Named
@RequestScoped
public class RulePackageConverter implements Converter {

    @Inject
    private RulePackageServiceImpl rulePackageService;

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        if(s != null && s.trim().length() > 0) {
            return rulePackageService.findById(s);
        } else {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        if(o != null) {
            return String.valueOf(((RulePackage) o).getId());
        }
        else {
            return null;
        }
    }
}
