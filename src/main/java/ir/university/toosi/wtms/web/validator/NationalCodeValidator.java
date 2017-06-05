package ir.university.toosi.wtms.web.validator;

import ir.university.toosi.wtms.web.action.UserManagementAction;
import ir.university.toosi.wtms.web.util.ManagedBeanManager;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator(value = "nationalCodeValidator")
public class NationalCodeValidator implements Validator {

    public void validate(FacesContext facesContext, UIComponent uiComponent, Object object) throws ValidatorException {
        UserManagementAction me = ManagedBeanManager.lookup(UserManagementAction.class);
        if ((object instanceof String)) {
            String value = ((String) object).trim();
            if (!isValidNationalCode(value)) {
                FacesMessage message = new FacesMessage();
                message.setSeverity(FacesMessage.SEVERITY_ERROR);
                message.setSummary(me.getValue("national.code.exception"));
                throw new ValidatorException(message);
            }
        }
    }

    private boolean isValidNationalCode(String nationalCode) {

        long tempNumber = 0L;
        long sum = 0L;
        long mode = 0L;
        int checkDigit = 0;

        if (nationalCode.length() < 10) {
            return false;
        }

        checkDigit = Integer.valueOf(nationalCode.substring(9, 10)).intValue();

        for (int counter = 1; counter <= 9; counter++) {
            tempNumber = Long.valueOf(nationalCode.substring(counter - 1, counter)).longValue();
            sum += tempNumber * (11 - counter);
        }

        mode = sum % 11L;
        if (mode > 1L) {
            if (11L - mode == checkDigit) return true;
        } else if (((mode == 0L) || (mode == 1L)) &&
                (mode == checkDigit)) return true;

        return false;
    }
}

