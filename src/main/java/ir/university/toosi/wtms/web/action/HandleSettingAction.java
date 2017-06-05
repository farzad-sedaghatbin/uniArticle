package ir.university.toosi.wtms.web.action;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.tms.model.service.SystemConfigurationServiceImpl;
import ir.university.toosi.wtms.web.helper.GeneralHelper;
import ir.university.toosi.tms.model.entity.MenuType;
import ir.university.toosi.tms.model.entity.SystemConfiguration;
import ir.university.toosi.wtms.web.util.RESTfulClientUtil;
import org.primefaces.model.SortOrder;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author : Hamed Hatami , Arsham Sedaghatbin, Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Named(value = "handleSettingAction")
@SessionScoped
public class HandleSettingAction implements Serializable {

    @Inject
    private UserManagementAction me;
    @Inject
    private GeneralHelper generalHelper;

    @EJB
    private SystemConfigurationServiceImpl configurationService;
    private int page = 1;
    private List<SystemConfiguration> systemConfigurationDataModel = new ArrayList<>();
    private List<SystemConfiguration> systemConfigurationList = new ArrayList<>();
    private SortOrder descriptionOrder = SortOrder.UNSORTED;

    public void begin() {
//        me.setActiveMenu(MenuType.SETTING);
        refresh();
        me.redirect("/setting/system-setting.xhtml");
    }

    public void changeSetting(ValueChangeEvent event) {
        SystemConfiguration systemConfiguration = null /*systemConfigurationDataModel.getRowData()*/;
        boolean temp = (Boolean) event.getNewValue();
        if (temp) {
            systemConfiguration.setValue("True");
        } else {
            systemConfiguration.setValue("False");
        }
        systemConfigurationList.add(systemConfiguration);
    }


    private void refresh() {
        systemConfigurationDataModel = configurationService.getAllConfiguration();
        systemConfigurationList = new ArrayList<>();

    }

    public void save() {
        Iterator iterator = systemConfigurationDataModel.iterator();
        while (iterator.hasNext()) {
            SystemConfiguration systemConfiguration = (SystemConfiguration) iterator.next();
            if (systemConfiguration.getType().equalsIgnoreCase("boolean"))
                continue;
            boolean condition = configurationService.editConfiguration(systemConfiguration);
            if (!condition) {
                me.addInfoMessage("operation.not.occurred");
                return;
            }
        }
        for (SystemConfiguration systemConfiguration : systemConfigurationList) {
            boolean condition = configurationService.editConfiguration(systemConfiguration);
            if (!condition) {
                me.addInfoMessage("operation.not.occurred");
                return;
            }
            me.fillSystemConfiguration();
        }

        me.initParameter();

    }

    public void sortByDescription() {
        if (descriptionOrder.equals(SortOrder.ASCENDING)) {
            setDescriptionOrder(SortOrder.DESCENDING);
        } else {
            setDescriptionOrder(SortOrder.ASCENDING);
        }
    }

    public UserManagementAction getMe() {
        return me;
    }

    public void setMe(UserManagementAction me) {
        this.me = me;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<SystemConfiguration> getSystemConfigurationList() {
        return systemConfigurationList;
    }

    public void setSystemConfigurationList(List<SystemConfiguration> systemConfigurationList) {
        this.systemConfigurationList = systemConfigurationList;
    }

    public List<SystemConfiguration> getSystemConfigurationDataModel() {
        return systemConfigurationDataModel;
    }

    public void setSystemConfigurationDataModel(List<SystemConfiguration> systemConfigurationDataModel) {
        this.systemConfigurationDataModel = systemConfigurationDataModel;
    }

    public SortOrder getDescriptionOrder() {
        return descriptionOrder;
    }

    public void setDescriptionOrder(SortOrder descriptionOrder) {
        this.descriptionOrder = descriptionOrder;
    }
}