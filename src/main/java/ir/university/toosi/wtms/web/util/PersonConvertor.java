package ir.university.toosi.wtms.web.util;

import ir.university.toosi.wtms.web.action.zone.HandleGatewayAction;
import ir.university.toosi.tms.model.entity.personnel.Person;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("PersonConvertor")

public class PersonConvertor implements Converter {
    private HandleGatewayAction handleGatewayAction;
    public Object getAsObject(FacesContext facesContext, UIComponent component, String s) {
        handleGatewayAction= ManagedBeanManager.lookup(HandleGatewayAction.class);
        for (Person person : handleGatewayAction.getPersons()) {
            if (person.getPersonnelNo().equals(s)) {
                return person;
            }
        }
        return null;
    }

    public String getAsString(FacesContext facesContext, UIComponent component, Object o) {
        if (o == null) return null;
        return ((Person) o).getPersonnelNo();
    }

}
