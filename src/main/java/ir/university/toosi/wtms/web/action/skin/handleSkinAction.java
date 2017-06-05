package ir.university.toosi.wtms.web.action.skin;

import ir.university.toosi.tms.model.service.UserServiceImpl;
import ir.university.toosi.wtms.web.action.UserManagementAction;
import org.apache.commons.lang.StringUtils;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.html.HtmlCommandLink;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Named(value = "handleSkinAction")
@SessionScoped
public class handleSkinAction implements Serializable {
    @Inject
    private UserManagementAction me;
    @EJB
    private UserServiceImpl userService;

    private String defaultSkin = "Gray";

    public String getSkin() {
        if (me.getUser() != null
                && !StringUtils.isEmpty(me.getUser().getSkin())
                && getSkinList().contains(me.getUser().getSkin())) {
            return me.getUser().getSkin();
        }
        return defaultSkin;
    }

    public void setSkin(String skin) {
        this.defaultSkin = skin;
    }

    public void changeSkin(ActionEvent event) {
        defaultSkin = (String) ((HtmlCommandLink) event.getSource()).getTitle();
        setCurrentUserSkin();

        FacesContext context = FacesContext.getCurrentInstance();
        String currentPage = context.getViewRoot().getViewId();

        me.redirect(currentPage);
    }

    private void setCurrentUserSkin() {
        me.getUser().setSkin(defaultSkin);

        me.getGeneralHelper().getWebServiceInfo().setServiceName("/editUser");

        userService.editUser(me.getUser());
    }

    public List<String> getSkinList() {
        List<String> skinList = new LinkedList<>();
        skinList.add("Gray");
        skinList.add("Blue");
        skinList.add("Indigo");
        skinList.add("Cyan");
        skinList.add("Red");
        return skinList;
    }

    public String getLoginImageUrl() {
        if (me.getDirection().equals("rtl")) {
            return "/images/" + getSkin() + "-persian.png";
        }
        return "/images/" + getSkin() + "-english.png";
    }
}
