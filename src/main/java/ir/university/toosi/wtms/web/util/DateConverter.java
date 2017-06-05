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

@FacesConverter(value = "dateConverter")
public class DateConverter implements Converter {

    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
        if (value != null && !value.isEmpty() && value.contains("/") && !value.equalsIgnoreCase("null") && !value.contains("*")) {
            value = value.replace("/", "");
        }
        value = LangUtil.getEnglishNumber(value);

        return value;
    }

    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object object) {
        String value = (String) object;
        if (value != null && !StringUtils.isEmpty(value) && !value.contains("/") && !value.equalsIgnoreCase("null")) {
            if (value.length() == 12)
                value = value.substring(0, 4) + "/" + value.substring(4, 6) + "/" + value.substring(6, 8) + " " + value.substring(8, 10) + ":" + value.substring(10);
            if (value.length() == 8)
                value = value.substring(0, 4) + "/" + value.substring(4, 6) + "/" + value.substring(6);
            if (value.length() == 6)
                value = value.substring(0, 2) + "/" + value.substring(2, 4) + "/" + value.substring(4, 6);
        }


        StringBuffer farsiNumberString = new StringBuffer();
        String number = "";
        if (object instanceof String) {
            number = (String) value;
            for (int i = 0; i < number.length(); ++i) {
                char c = number.charAt(i);
                switch (c) {
                    case '0':
                        farsiNumberString.append('۰');
                        break;

                    case '1':
                        farsiNumberString.append('۱');
                        break;

                    case '2':
                        farsiNumberString.append('۲');
                        break;

                    case '3':
                        farsiNumberString.append('۳');
                        break;

                    case '4':
                        farsiNumberString.append('۴');
                        break;

                    case '5':
                        farsiNumberString.append('۵');
                        break;

                    case '6':
                        farsiNumberString.append('۶');
                        break;

                    case '7':
                        farsiNumberString.append('۷');
                        break;

                    case '8':
                        farsiNumberString.append('۸');
                        break;

                    case '9':
                        farsiNumberString.append('۹');
                        break;
                    default:
                        farsiNumberString.append(c);
                }
            }
        }
        return farsiNumberString.toString();
    }
}