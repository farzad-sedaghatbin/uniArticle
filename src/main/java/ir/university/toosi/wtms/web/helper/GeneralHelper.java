package ir.university.toosi.wtms.web.helper;


import ir.university.toosi.tms.model.entity.SystemParameterType;
import ir.university.toosi.tms.model.entity.WebServiceInfo;
import ir.university.toosi.tms.model.service.UserServiceImpl;
import ir.university.toosi.wtms.web.action.UserManagementAction;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;


/**
 * @author : Farzad Sedaghatbin
 * @version : 0.8
 */

@Named(value = "generalHelper")
@ApplicationScoped
public class GeneralHelper implements Serializable {

    @Inject
    private UserManagementAction me;

    @EJB
    private UserServiceImpl userService;
    private String userExtraField1;
    private String userExtraField2;
    private String userExtraField3;
    private String userExtraField4;
    private String personExtraField1;
    private String personExtraField2;
    private String personExtraField3;
    private String personExtraField4;

    private WebServiceInfo webServiceInfo = new WebServiceInfo();
    private byte[] anonymous;


    public GeneralHelper() {
        System.out.println("constractor");
//        loadLanguage();
        anonymous = getImage2Base64();
    }

    @PostConstruct
    public void dd(){

        System.out.println("postcons");
        System.out.println(me.getDirection());
    }

    public byte[] getImage2Base64() {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("no-photo-icon.png");
        BufferedImage image = null;
        try {
            image = ImageIO.read(inputStream);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            return new byte[0];
        }
    }





    public void initme() {
//        me.setActiveMenu(MenuType.HARDWARE);
//        if (lastLanguages == null) {
//            loadLanguage();
//        }
//        me.setLanguages(lastLanguages);
//        me.setSelectedLanguage(lastLanguages.getName());
//        try {
//            me.setLanguage();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        fillExtraField();
    }

    private void fillExtraField() {
        userExtraField1 = me.getValue(SystemParameterType.USER_EXTRA_FIELD_1.getDescription());
        userExtraField2 = me.getValue(SystemParameterType.USER_EXTRA_FIELD_2.getDescription());
        userExtraField3 = me.getValue(SystemParameterType.USER_EXTRA_FIELD_3.getDescription());
        userExtraField4 = me.getValue(SystemParameterType.USER_EXTRA_FIELD_4.getDescription());
        personExtraField1 = me.getValue(SystemParameterType.PERSON_EXTRA_FIELD_1.getDescription());
        personExtraField2 = me.getValue(SystemParameterType.PERSON_EXTRA_FIELD_2.getDescription());
        personExtraField3 = me.getValue(SystemParameterType.PERSON_EXTRA_FIELD_3.getDescription());
        personExtraField4 = me.getValue(SystemParameterType.PERSON_EXTRA_FIELD_4.getDescription());
        personExtraField4 = me.getValue(SystemParameterType.PERSON_EXTRA_FIELD_4.getDescription());
    }

    public UserManagementAction getMe() {
        return me;
    }

    public void setMe(UserManagementAction me) {
        this.me = me;
    }

    public WebServiceInfo getWebServiceInfo() {
        return webServiceInfo;
    }

    public void setWebServiceInfo(WebServiceInfo webServiceInfo) {
        this.webServiceInfo = webServiceInfo;
    }

    public String getUserExtraField1() {
        return userExtraField1;
    }

    public void setUserExtraField1(String userExtraField1) {
        this.userExtraField1 = userExtraField1;
    }

    public String getUserExtraField2() {
        return userExtraField2;
    }

    public void setUserExtraField2(String userExtraField2) {
        this.userExtraField2 = userExtraField2;
    }

    public String getUserExtraField3() {
        return userExtraField3;
    }

    public void setUserExtraField3(String userExtraField3) {
        this.userExtraField3 = userExtraField3;
    }

    public String getUserExtraField4() {
        return userExtraField4;
    }

    public void setUserExtraField4(String userExtraField4) {
        this.userExtraField4 = userExtraField4;
    }

    public String getPersonExtraField1() {
        return personExtraField1;
    }

    public void setPersonExtraField1(String personExtraField1) {
        this.personExtraField1 = personExtraField1;
    }

    public String getPersonExtraField2() {
        return personExtraField2;
    }

    public void setPersonExtraField2(String personExtraField2) {
        this.personExtraField2 = personExtraField2;
    }

    public String getPersonExtraField3() {
        return personExtraField3;
    }

    public void setPersonExtraField3(String personExtraField3) {
        this.personExtraField3 = personExtraField3;
    }

    public String getPersonExtraField4() {
        return personExtraField4;
    }

    public void setPersonExtraField4(String personExtraField4) {
        this.personExtraField4 = personExtraField4;
    }



    public byte[] getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(byte[] anonymous) {
        this.anonymous = anonymous;
    }

    public UserServiceImpl getUserService() {
        return userService;
    }

    public void setUserService(UserServiceImpl userService) {
        this.userService = userService;
    }


}