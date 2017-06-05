package ir.university.toosi.wtms.web.util;

import ir.university.toosi.wtms.web.action.person.HandleCardAction;
import ir.university.toosi.tms.model.entity.personnel.Card;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("CardConverter")

public class CardConverter implements Converter {
    private HandleCardAction handleCardAction;
    public Object getAsObject(FacesContext facesContext, UIComponent component, String s) {
        handleCardAction=ManagedBeanManager.lookup(HandleCardAction.class);
        for (Card card : handleCardAction.getAllCard()) {
            if (card.getCode().equals(s)) {
                return card;
            }
        }
        return null;
    }

    public String getAsString(FacesContext facesContext, UIComponent component, Object o) {
        if (o == null) return null;
        return ((Card) o).getCode();
    }

}
