package ir.university.toosi.wtms.web.action;


import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

/**
 * @author : Hamed Hatami , Arsham Sedaghatbin, Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Named(value = "modifyPasswordAction")
@SessionScoped
public class ModifyPasswordAction implements Serializable {

    @Inject
    private UserManagementAction me;

    @Inject
    private LogoutAction logoutAction;

    private String oldPassword;
    private String newPassword;
    private String confirmPassword;


    public String begin() {
        return "change-password";
    }

    public String end() {

        if (!newPassword.equals(confirmPassword)) {
            me.addErrorMessage("invalid.newPassword.confirmPassword");
            return "change-password";
        }

        if (!oldPassword.equals(me.getPassword())) {
            me.addErrorMessage("invalid.oldPassword");
            return "change-password";
        }

/*
        if (me.getGeneralHelper().getUserService().modifyPassword(new User(me.getUsername(), newPassword))) {
            conversation.end();
            return logoutAction.logout();
        }
*/

        return "change-password";
    }

    public String goBack() {
        return "home";
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}