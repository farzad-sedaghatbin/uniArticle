package ir.university.toosi.wtms.web.action;


import ir.ReaderWrapperService;
import ir.university.toosi.tms.model.entity.*;
import ir.university.toosi.tms.model.entity.calendar.Calendar;
import ir.university.toosi.tms.model.entity.personnel.Card;
import ir.university.toosi.tms.model.entity.zone.Zone;
import ir.university.toosi.tms.model.service.OperationServiceImpl;
import ir.university.toosi.tms.model.service.SystemConfigurationServiceImpl;
import ir.university.toosi.tms.model.service.UserServiceImpl;
import ir.university.toosi.tms.model.service.calendar.CalendarServiceImpl;
import ir.university.toosi.tms.model.service.personnel.PersonServiceImpl;
import ir.university.toosi.tms.model.service.zone.GatewayServiceImpl;
import ir.university.toosi.tms.model.service.zone.PDPServiceImpl;
import ir.university.toosi.tms.util.LangUtil;
import ir.university.toosi.wtms.web.action.monitoring.HandleMonitoringAction;
import ir.university.toosi.wtms.web.action.user.HandleUserAction;
import ir.university.toosi.wtms.web.helper.GeneralHelper;
import ir.university.toosi.wtms.web.util.CalendarUtil;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.*;

//import org.richfaces.component.Mode;
//import org.richfaces.component.Positioning;

/**
 * @author : Hamed Hatami , Arsham Sedaghatbin, Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */


@Named(value = "me")
@SessionScoped
public class UserManagementAction implements Serializable {

    @Inject
    private GeneralHelper generalHelper;
    @Inject
    private AccessControlAction accessControlAction;
    @Inject
    private HandleUserAction handleUserAction;
    @Inject
    private HandleMonitoringAction monitoringAction;

    @EJB
    private UserServiceImpl userService;
    @EJB
    private SystemConfigurationServiceImpl configurationService;
    @EJB
    private CalendarServiceImpl calendarService;
    @EJB
    private OperationServiceImpl operationService;
    @EJB
    private ReaderWrapperService readerWrapperService;

    @EJB
    GatewayServiceImpl gatewayService;

    @EJB
    PDPServiceImpl pdpService;
    @EJB
    PersonServiceImpl personService;

    public static String SENTRY_COUNT = "10";
    public static final String INVALID_TRY = "invalid_try";
    public static final String usernameInSession = "username";
    private static final String BUNDLE_NAME = "messages.Messages";

    //    private Mode componentMode = Mode.ajax;
//    private Positioning componentPosition = Positioning.autoLeft;
//    private Positioning mainJointPoint = Positioning.autoRight;
//    private Positioning subJointPoint = Positioning.autoLeft;
//    private Positioning horizontalJointPoint = Positioning.topLeft;
    private String username;
    private String direction = "rtl";
    private String appositeDirection = "ltr";
    private String align = "right";
    private String appositeAlign = "left";
    private String password = "";
    private String rePassword = "";
    private String oldPassword = "";
    private Languages languages;
    private String selectedLanguage = "fa";
    private User user = null;
    private String actorId;
    private String toUsername;
    private String message;
    private Locale locale = null;
    private String showMessage = "";
    private String changeCurrentUserPassword;
    private boolean newCMS = false;
    private String value;
    public static MenuType menuType = MenuType.CALENDAR;
    private MenuType activeMenu;
    private Hashtable<String, LanguageManagement> language;
    private Hashtable<SystemParameterType, String> systemParameter = new Hashtable<>();
    private int page = 1;
    public static Hashtable<String, Boolean> permissionHash = new Hashtable<>();
    public static List<Calendar> calendars = new ArrayList<>();
    public static SelectItem[] calendarItem;
    public static Hashtable<String, Calendar> calendarHashtable = new Hashtable<>();


    public void redirect(String pageName) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            if (facesContext.getExternalContext().getRequestParameterMap().get("cid") == null || facesContext.getExternalContext().getRequestParameterMap().get("cid").isEmpty()) {
                facesContext.getExternalContext().redirect(facesContext.getExternalContext().getRequestContextPath() + pageName);
            } else {
                facesContext.getExternalContext().redirect(facesContext.getExternalContext().getRequestContextPath() + pageName + "?cid=" + facesContext.getExternalContext().getRequestParameterMap().get("cid"));
            }
        } catch (Exception e) {
            String message = e.getMessage();
            if (e instanceof NullPointerException) {
                message = "Null Pointer Exception";
            }
        }
    }

    public void initParameter() {
        SENTRY_COUNT = configurationService.findByParameter(SystemParameterType.SENTRY_COUNT).getValue();

    }

    public boolean hasPermission(String checkedOperation) {
        Boolean hasPermission = permissionHash.get(checkedOperation);
        return hasPermission == null ? false : hasPermission;
    }

    public ResourceBundle getBundle() {
        return ResourceBundle.getBundle(BUNDLE_NAME, getLocale());
    }

    public String getBundleMessage(String bundleKey, String... arguments) {
        if (bundleKey == null || bundleKey.isEmpty())
            return "?";

        try {
            return MessageFormat.format(getBundle().getString(bundleKey), arguments);
        } catch (MissingResourceException e) {
//todo: don't log here please!!
//            log.error(createExceptionCause(e));
            return bundleKey;
        }
    }

    public void changeLocale() {
        FacesContext context = FacesContext.getCurrentInstance();
        String language = context.getViewRoot().getLocale().getLanguage();
        String currentPage = context.getViewRoot().getViewId().replaceAll(".xhtml", ".htm");

        if (language == null) {
            this.setLocale(Locale.ENGLISH);
        }

        if (language.equalsIgnoreCase("fa")) {
            this.setLocale(Locale.ENGLISH);
        } else {
            this.setLocale(LangUtil.LOCALE_FARSI);
        }

        redirect(currentPage);
    }

    public void addErrorMessage(String bundleKey) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getBundleMessage(bundleKey, null), getBundleMessage(bundleKey, null)));
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
    }

    public void addErrorMessageString(String messageContent) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, messageContent, messageContent));
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
    }

    public void addErrorMessage(String bundleKey, String... arguments) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getBundleMessage(bundleKey, arguments), getBundleMessage(bundleKey, arguments)));
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
    }

    public void addWarnMessage(String bundleKey) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, getBundleMessage(bundleKey, null), getBundleMessage(bundleKey, null)));
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
    }

    public void addInfoMessage(String bundleKey) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, getBundleMessage(bundleKey, null), getBundleMessage(bundleKey, null)));
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
    }

    public void addInfoMessage(String bundleKey, String... arguments) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, getBundleMessage(bundleKey, arguments), getBundleMessage(bundleKey, arguments)));
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
    }

    public Locale getLocale() {
        if (this.locale == null) {
            this.locale = LangUtil.LOCALE_FARSI;
        }
        return this.locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }


    public void authenticate() {
        permissionHash = new Hashtable<>();
        try {
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            if ((username == null) || (username.isEmpty()) || (password == null) || (password.isEmpty())) {
                addErrorMessage("invalid.login.username.password");
//                return "login";
            }
            user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user = userService.authenticate(username, password);
//            if (!generalHelper.getLastLanguages().isRtl()) {
//                direction = "ltr";
//                align = "left";
//                appositeAlign = "right";
//                appositeDirection = "rtl";
//            }


            //getGeneralHelper().getUserService().authenticate(username, generalHelper.getEncryptUtil().encrypt(password));
            if ((user == null) || user.getUsername().equalsIgnoreCase("null")/*&& !(accessControlAction.isSystemManager())*/ || user.getEnable().equalsIgnoreCase("false")) {
                Integer invalidTry = (Integer) session.getAttribute(INVALID_TRY);
                if (invalidTry == null) {
                    invalidTry = 0;
                }
                invalidTry++;
                if (invalidTry > 2) {
//                    user = null;//getGeneralHelper().getUserService().findByUsername(username);
//                    user.setEnable("false");
                    //getGeneralHelper().getUserService().editUser(user);
                }
                session.setAttribute(INVALID_TRY, invalidTry);
                addErrorMessage("invalid.login");
                redirect("/login.xhtml");
                return;
            }
            /*else if ((user.getEnable().equalsIgnoreCase("false")) || (user.getWorkGroups().getEnabled().equalsIgnoreCase("false"))) {
                addErrorMessage("invalid.login.userDisable");
                return "login";
            }*/

//            if (user.isOnline()) {
//            addErrorMessage("user_can_not_login_now");
//            return "login";
//            } else {
//                user.setOnline(true);
//                getGeneralHelper().getUserService().editUser(user);
//            }
            boolean allowed = true;
//            for (PC pc : user.getPcs()) {
//                if (pc.getIp().equals(getRemoteIpAddress())) {
//                    allowed = true;
//                    break;
//                }
//            }

            if (!allowed) {
                addErrorMessage("invalid.pc");
//                return "login";
            }
            if (user.getEnable().equalsIgnoreCase("false")) {
                addErrorMessage("user.disabled");
//                return "login";
            }
            accessControlAction.setWorkGroup(user.getWorkGroups());
//            getGeneralHelper().getWebServiceInfo().setServiceName("/getAllOperation");
//            List<Operation> operationList = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(getGeneralHelper().getWebServiceInfo().getServerUrl(), getGeneralHelper().getWebServiceInfo().getServiceName()), new TypeReference<List<Operation>>() {
//            });

            List<Operation> operationList = operationService.getAllOperation();
            if (username.equalsIgnoreCase("admin")) {
                for (Operation operation : operationList) {
                    permissionHash.put(operation.getName(), Boolean.TRUE);

                }
            }
            for (Operation operation : operationList) {
                for (WorkGroup workGroup : user.getWorkGroups()) {
                    for (Role role : workGroup.getRoles()) {
                        for (Operation innerOperation : role.getOperations()) {
                            if (innerOperation.getName().equalsIgnoreCase(operation.getName())) {
                                permissionHash.put(operation.getName(), Boolean.TRUE);
                            }
                        }
                    }
                }
            }
//                if (!permissionHash.containsKey(operation.getDescription()))
//                    permissionHash.put(operation.getDescription(), Boolean.FALSE);
//            }

            /************/
//            fillCalendar();
            /*************/
            session.setAttribute(usernameInSession, this.username);
        } catch (Exception e) {
            String message = e.getMessage();
            if (e instanceof NullPointerException) {
                message = "Null Pointer Exception";
            }
            e.printStackTrace();
        }

//        fillSystemConfiguration();
//        initParameter();
        //generalHelper.initial();
        handleUserAction.refresh();
        redirect("/user/users.xhtml");
//        return "home";

    }


    public void fillSystemConfiguration() {
        List<SystemConfiguration> systemConfigurations = configurationService.getAllConfiguration();
        for (SystemConfiguration systemConfiguration : systemConfigurations) {
            systemParameter.put(systemConfiguration.getParameter(), systemConfiguration.getValue());
        }
    }

    public String goHome() {
        return "home";
    }

    public void fillCalendar() {
        /************/
        calendars = calendarService.getAllCalendar();

        calendarItem = new SelectItem[calendars.size()];
        int i = 0;
        for (Calendar calendar : calendars) {
            calendarHashtable.put(String.valueOf(calendar.getId()), calendar);
            calendarItem[i++] = new SelectItem(calendar.getId(), calendar.getName());
        }
        /*************/
    }

    public String resetPassword() {
        return "password-reset";
    }

    public String getRemoteIpAddress() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        return request.getRemoteAddr();
    }

    public String changePassword() {
        changeCurrentUserPassword = "true";
        password = "";
        rePassword = "";
        oldPassword = "";
        return "password-change";

    }

    public String revert() {
        changeCurrentUserPassword = "false";
        return "home";

    }


    public Languages getLanguages() {
        return languages;
    }

    public void setLanguages(Languages languages) {
        this.languages = languages;
    }

    public String getValue(String key) {
        if (language == null) {
            getGeneralHelper().initme();
        }
//        if (language.containsKey(key))
//            return language.get(key).getTitle();
        return key;

    }

//    public Mode getComponentMode() {
//        return componentMode;
//    }
//
//    public void setComponentMode(Mode componentMode) {
//        this.componentMode = componentMode;
//    }
//
//    public Positioning getComponentPosition() {
//        return componentPosition;
//    }
//
//    public void setComponentPosition(Positioning componentPosition) {
//        this.componentPosition = componentPosition;
//    }
//
//    public Positioning getMainJointPoint() {
//        return mainJointPoint;
//    }
//
//    public void setMainJointPoint(Positioning mainJointPoint) {
//        this.mainJointPoint = mainJointPoint;
//    }
//
//    public Positioning getSubJointPoint() {
//        return subJointPoint;
//    }
//
//    public void setSubJointPoint(Positioning subJointPoint) {
//        this.subJointPoint = subJointPoint;
//    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public GeneralHelper getGeneralHelper() {
        return generalHelper;
    }

    public void setGeneralHelper(GeneralHelper generalHelper) {
        this.generalHelper = generalHelper;
    }

//    public Positioning getHorizontalJointPoint() {
//        return horizontalJointPoint;
//    }
//
//    public void setHorizontalJointPoint(Positioning horizontalJointPoint) {
//        this.horizontalJointPoint = horizontalJointPoint;
//    }


    public String getActorId() {
        return actorId;
    }

    public void setActorId(String actorId) {
        this.actorId = actorId;
    }

    public String getEncryptedUsername() {
        try {
            return username;
        } catch (Exception e) {
            String message = e.getMessage();
            if (e instanceof NullPointerException) {
                message = "Null Pointer Exception";
            }
            return null;
        }
    }

    public String getEncryptedPassword() {
        try {
            return password;
        } catch (Exception e) {
            String message = e.getMessage();
            if (e instanceof NullPointerException) {
                message = "Null Pointer Exception";
            }
            return null;
        }
    }

    public String getEncryptedUsernamePassword() {
        try {
            return username + "," + password + "#";
        } catch (Exception e) {
            String message = e.getMessage();
            if (e instanceof NullPointerException) {
                message = "Null Pointer Exception";
            }
            return null;
        }
    }

    public String getToUsername() {
        return toUsername;
    }

    public void setToUsername(String toUsername) {
        this.toUsername = toUsername;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getShowMessage() {
        return showMessage;
    }

    public void setShowMessage(String showMessage) {
        this.showMessage = showMessage;
    }

    public void processUserName(ValueChangeEvent event) {
        toUsername = (String) event.getNewValue();
    }

    public String getRePassword() {
        return rePassword;
    }

    public void setRePassword(String rePassword) {
        this.rePassword = rePassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        oldPassword = oldPassword;
    }

    public String getChangeCurrentUserPassword() {
        return changeCurrentUserPassword;
    }

    public void setChangeCurrentUserPassword(String changeCurrentUserPassword) {
        this.changeCurrentUserPassword = changeCurrentUserPassword;
    }

    public boolean isNewCMS() {
        return newCMS;
    }

    public void setNewCMS(boolean newCMS) {
        this.newCMS = newCMS;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public Hashtable<String, LanguageManagement> getLanguage() {
        return language;
    }


    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getAlign() {
        return align;
    }

    public void setAlign(String align) {
        this.align = align;
    }

    public String getSelectedLanguage() {
        return selectedLanguage;
    }

    public void setSelectedLanguage(String selectedLanguage) {
        this.selectedLanguage = selectedLanguage;
    }

    public String getAppositeDirection() {
        return appositeDirection;
    }

    public void setAppositeDirection(String appositeDirection) {
        this.appositeDirection = appositeDirection;
    }

    public String getAppositeAlign() {
        return appositeAlign;
    }

    public void setAppositeAlign(String appositeAlign) {
        this.appositeAlign = appositeAlign;
    }

    public String getCurrentTime() {
        return LangUtil.getEnglishNumber(CalendarUtil.getTime(new Date(), new Locale("fa")));
    }

    public static SelectItem[] getCalendarItem() {
        return calendarItem;
    }

    public static void setCalendarItem(SelectItem[] calendarItem) {
        UserManagementAction.calendarItem = calendarItem;
    }

    public Hashtable<SystemParameterType, String> getSystemParameter() {
        return systemParameter;
    }

    public void setSystemParameter(Hashtable<SystemParameterType, String> systemParameter) {
        this.systemParameter = systemParameter;
    }

    public static String getSENTRY_COUNT() {
        return SENTRY_COUNT;
    }

    public static void setSENTRY_COUNT(String SENTRY_COUNT) {
        UserManagementAction.SENTRY_COUNT = SENTRY_COUNT;
    }

    public static String getInvalidTry() {
        return INVALID_TRY;
    }

    public static String getUsernameInSession() {
        return usernameInSession;
    }

    public void setLanguage(Hashtable<String, LanguageManagement> language) {
        this.language = language;
    }

    public AccessControlAction getAccessControlAction() {
        return accessControlAction;
    }

    public void setAccessControlAction(AccessControlAction accessControlAction) {
        this.accessControlAction = accessControlAction;
    }

    public static Hashtable<String, Boolean> getPermissionHash() {
        return permissionHash;
    }

    public static void setPermissionHash(Hashtable<String, Boolean> permissionHash) {
        UserManagementAction.permissionHash = permissionHash;
    }

    public static List<Calendar> getCalendars() {
        return calendars;
    }

    public static void setCalendars(List<Calendar> calendars) {
        UserManagementAction.calendars = calendars;
    }

    public static Hashtable<String, Calendar> getCalendarHashtable() {
        return calendarHashtable;
    }

    public static void setCalendarHashtable(Hashtable<String, Calendar> calendarHashtable) {
        UserManagementAction.calendarHashtable = calendarHashtable;
    }

    public boolean wasExpanded(MenuType menu) {
        if (menuType.equals(menu)) {
            return true;
        } else {
            return false;
        }
    }

    public MenuType getMenuType() {
        return menuType;
    }

    public void setMenuType(MenuType menuType) {
        UserManagementAction.menuType = menuType;
    }

    public String getActiveMenu() {
        return activeMenu.name().toLowerCase();
    }

    public void setActiveMenu(MenuType activeMenu) {
        this.activeMenu = activeMenu;
    }

    public void test() {
        TrafficLog trafficLog = new TrafficLog();
        trafficLog.setId(1);
        trafficLog.setTime(String.valueOf(new Date().getTime()));
        trafficLog.setDate(new Date().toString());
        trafficLog.setExit(true);
        trafficLog.setValid(true);
        trafficLog.setGateway(gatewayService.findById(7));
        trafficLog.setPdp(pdpService.findById(8));
        trafficLog.setPerson(personService.findById(5));
        trafficLog.setCard(new Card());
        trafficLog.setFinger(true);
        trafficLog.setLast(true);
        trafficLog.setOffline(false);
        trafficLog.setPictures("");
        trafficLog.setDeleted("0");
        trafficLog.setStatus("c");
        trafficLog.setEffectorUser("admin");
        trafficLog.setZone(new Zone());
        trafficLog.setVideo("");
        monitoringAction.sendMessage(trafficLog);

    }
}