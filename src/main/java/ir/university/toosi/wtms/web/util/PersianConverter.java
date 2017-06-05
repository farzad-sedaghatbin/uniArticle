package ir.university.toosi.wtms.web.util;

import ir.university.toosi.tms.util.LangUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * @author : Hamed Hatami , Javad Sarhadi , Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@FacesConverter(value = "persianConverter")
public class PersianConverter implements Converter {

    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
        if (value != null && !value.isEmpty() && value.contains("") && !value.equalsIgnoreCase("null") && !value.contains("*")) {
            value = value.replace("", "?");
        }
        value = LangUtil.getEnglishNumber(value);

        return value;
    }

    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object object) {
        if (object == null) return null;
        String value="";
        if (object instanceof Integer) {
            value = String.valueOf((Integer) object);
        } else {
            value = (String) object;
        }
        return LangUtil.getFarsiNumber(value);
    }
}