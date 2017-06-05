package ir.university.toosi.wtms.web.action.report;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.wtms.web.action.UserManagementAction;
import ir.university.toosi.tms.model.entity.*;
import ir.university.toosi.tms.model.entity.personnel.Card;
import ir.university.toosi.tms.model.entity.personnel.Person;
import ir.university.toosi.tms.model.entity.zone.Gateway;
import ir.university.toosi.tms.model.entity.zone.PDP;
import ir.university.toosi.wtms.web.util.RESTfulClientUtil;
import ir.university.toosi.wtms.web.util.ReportUtils;
import ir.university.toosi.wtms.web.util.Storage;

import javax.enterprise.context.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: behzad
 * Date: 1/2/14
 * Time: 9:05 PM
 * To change this template use File | Settings | File Templates.
 */

@Named(value = "handleCardReportAction")
@SessionScoped
public class HandleCardReportAction implements Serializable {


    @Inject
    private UserManagementAction me;
    private String type;
    private String kind;
    private String reportType;
    private String name;
    private String lastname;
    private String cardNo;
    private String haveCard;
    private String unsucessfullTry;
    private String organizationName;
    private String deleted;
    private String workStation;
    private String enabled;
    private String zone;
    private String allowed;
    private String haveDescription;
    private String gateway;
    private String pdp;
    private String startDate;
    private String endDate;
    private String startTime;
    private String endTime;
    private String entryType;
    private SelectItem[] items = new SelectItem[5];
    private SelectItem[] booleanItems = new SelectItem[2];
    private SelectItem[] reportTypeItems = new SelectItem[2];
    private SelectItem[] staticReportItems = new SelectItem[4];
    private SelectItem[] staticPersonReportItems = new SelectItem[5];
    private SelectItem[] staticCardReportItems = new SelectItem[7];
    private SelectItem[] staticGatewayReportItems = new SelectItem[2];
    private SelectItem[] operationItems = new SelectItem[3];
    private String startSecond;
    private String startMinutes;
    private String startHour;
    private String endSecond;
    private String endMinutes;
    private String endHour;
    private String staticKind;
    private String reportSaveName;
    private String nameOperation;
    private String lastnameOperation;
    private String cardNoOperation;
    private String haveCardOperation;
    private String unsucessfullTryOperation;
    private String organizationNameOperation;
    private String deletedOperation;
    private String workStationOperation;
    private String enabledOperation;
    private String zoneOperation;
    private String allowedOperation;
    private String haveDescriptionOperation;
    private String gatewayOperation;
    private String pdpOperation;
    private String startDateOperation;
    private String endDateOperation;
    private String startTimeOperation;
    private String endTimeOperation;
    private String entryTypeOperation;
    private String queryString = "";
    private List<BaseEntity> innerReportEntity = null;
    private List<Person> innerPerson = null;
    private List<Card> innerCard = null;
    private List<ReportEntity> innerReport = null;
    private List<String> titles = new ArrayList<>();
    private String gridObjectType = "";
    private DataModel<ReportEntity> reportEntityList = null;
    private DataModel<Person> personList = null;
    private DataModel<Card> cardList = null;
    private DataModel<SavedQuery> querieList = null;
    private int page1 = 1;
    private int page2 = 1;
    private int page3 = 1;
    private int page4 = 1;
    private SavedQuery currentSavedQuery;
    private boolean selectRow = false;

    private SelectItem[] gateItem;
    private SelectItem[] pdpItem;
    private Storage<String, Gateway> gatewayObject = new Storage<>(new Gateway());
    private WebServiceInfo webServiceInfo = new WebServiceInfo();

    public void init() {
        type = "pdf";
        kind = "0";
        reportSaveName = "";
        reportType = "0";
        name = "";
        lastname = "";
        cardNo = "";
        haveCard = "";
        unsucessfullTry = "0";
        organizationName = "";
        deleted = "";
        workStation = "";
        enabled = "";
        zone = "";
        allowed = "";
        haveDescription = "";
        gateway = "";
        pdp = "";
        startDate = "";
        endDate = "";
        startTime = "";
        endTime = "";
        entryType = "";
        nameOperation = "";
        lastnameOperation = "";
        cardNoOperation = "";
        haveCardOperation = "";
        unsucessfullTryOperation = "";
        organizationNameOperation = "";
        deletedOperation = "";
        workStationOperation = "";
        enabledOperation = "";
        zoneOperation = "";
        allowedOperation = "";
        haveDescriptionOperation = "";
        gatewayOperation = "";
        pdpOperation = "";
        startDateOperation = "";
        endDateOperation = "";
        startTimeOperation = "";
        endTimeOperation = "";
        entryTypeOperation = "";
        startSecond = "";
        startMinutes = "";
        startHour = "";
        endSecond = "";
        endMinutes = "";
        endHour = "";
        staticKind = "";
        queryString = "";
        gridObjectType = "";
        innerReportEntity = null;
        titles = new ArrayList<>();
        page1 = 1;
        page2 = 1;
        page3 = 1;
        page4 = 1;
    }

    public void selectKind(ValueChangeEvent event) {
        kind = (String) event.getNewValue();
        System.out.println(kind);
        staticKind = "0";
    }

    public void selectReportType(ValueChangeEvent event) {
        reportType = (String) event.getNewValue();
        staticKind = "";
        gridObjectType = "";
        queryString = "";
        reportSaveName = "";
        System.out.println(reportType);
    }

    public void begin() {
        me.setActiveMenu(MenuType.REPORT);
        init();
        me.redirect("/report/list-report.xhtml");
    }

    public void saveQueryBegin() {
        me.setActiveMenu(MenuType.REPORT);
        init();
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/getAllSavedQuery");
        List<SavedQuery> innerQueryList = null;
        try {
            innerQueryList = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName()), new TypeReference<List<SavedQuery>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        querieList = new ListDataModel<>(innerQueryList);
        me.redirect("/report/list-saved-query.xhtml");
    }


    public void creatReportByMe() {
        try {

            if (reportType.equalsIgnoreCase("dynamic")) {
                if (kind.equalsIgnoreCase("user") || kind.equalsIgnoreCase("card") || (kind.equalsIgnoreCase("time"))) {
                    kind = "person";
                }
                startTime = startHour + startMinutes + startSecond;
                endTime = endHour + endMinutes + endSecond;
                System.out.println("this section generate and download Reports    " + kind + "      " + type);

                String query = makeQuery();

                String countQuery = query.replaceFirst("select r", "select count(r)");

                me.getGeneralHelper().getWebServiceInfo().setServiceName("/queryCountView");
                Long count = 0L;
                try {
                    count = new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), countQuery), new TypeReference<Long>() {
                    });
                } catch (IOException e) {
                    reportEntityList = new ListDataModel<>();
                    e.printStackTrace();
                    me.addErrorMessage("data_not_have_report_result");
                    me.redirect("/report/list-report.htm");
                    return;
                }
                if (count == 0) {
                    reportEntityList = new ListDataModel<>();
                    me.addErrorMessage("data_not_have_report_result");
                    me.redirect("/report/list-report.htm");
                    return;
                } else if (count > 5000)// TODO : add in system configuration
                {
                    savedScheduleQuery(query, count, type, kind);
                    return;
                }

                System.out.println("the query is : " + query);
                me.getGeneralHelper().getWebServiceInfo().setServiceName("/queryView");

                try {
                    innerReport = new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), query), new TypeReference<List<ReportEntity>>() {
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    reportEntityList = new ListDataModel<>();
                    me.addErrorMessage("data_not_have_report_result");
                    me.redirect("/report/list-report.htm");
                    return;
                }
                if (innerReport.size() == 0) {
//                    init();
                    reportEntityList = new ListDataModel<>();
                    me.addErrorMessage("data_not_have_report_result");
                    me.redirect("/report/list-report.htm");
                    return;
                } else {
                    gridObjectType = "ReportEntity";
                    innerReportEntity = new ArrayList<>();
                    innerReportEntity.addAll(innerReport);
                    reportEntityList = new ListDataModel<>(innerReport);
                }

            } else if (reportType.equalsIgnoreCase("static")) {
                System.out.println("this section generate and download static Reports    ");
                String query = "";
                if (kind.equalsIgnoreCase("staticPerson")) {
                    if (staticKind.equalsIgnoreCase("staticAllPerson")) {
                        me.getGeneralHelper().getWebServiceInfo().setServiceName("/getAllPersonDataModel");
                        try {
                            innerPerson = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName()), new TypeReference<List<Person>>() {
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                            reportEntityList = new ListDataModel<>();
                            me.addErrorMessage("data_not_have_report_result");
                            me.redirect("/report/list-report.htm");
                            return;
                        }
                        if (innerPerson.size() == 0) {
//                            init();
                            reportEntityList = new ListDataModel<>();
                            me.addErrorMessage("data_not_have_report_result");
                            me.redirect("/report/list-report.htm");
                            return;
                        } else {
                            innerReportEntity = new ArrayList<>();
                            innerReportEntity.addAll(innerPerson);
                            gridObjectType = "Person";
                            personList = new ListDataModel<>(innerPerson);
                        }
                    } else if (staticKind.equalsIgnoreCase("staticUnsuccessEntryPerson")) {
                        query = "select r from ReportEntity r where r.success = 'true'";
                        //me.getGeneralHelper().getWebServiceInfo().setServiceName("/getAllPerson");
                        me.getGeneralHelper().getWebServiceInfo().setServiceName("/queryView");
                        try {
                            innerReport = new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), query), new TypeReference<List<ReportEntity>>() {
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                            reportEntityList = new ListDataModel<>();
                            me.addErrorMessage("data_not_have_report_result");
                            me.redirect("/report/list-report.htm");
                            return;
                        }
                        if (innerReport.size() == 0) {
//                            init();
                            reportEntityList = new ListDataModel<>();
                            me.addErrorMessage("data_not_have_report_result");
                            me.redirect("/report/list-report.htm");
                            return;
                        } else {
                            gridObjectType = "ReportEntity";
                            innerReportEntity = new ArrayList<>();
                            innerReportEntity.addAll(innerReport);
                            reportEntityList = new ListDataModel<>(innerReport);
                        }
                    } else if (staticKind.equalsIgnoreCase("staticHaveCardPerson")) {
                        me.getGeneralHelper().getWebServiceInfo().setServiceName("/personWithCard");
                        try {
                            innerPerson = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName()), new TypeReference<List<Person>>() {
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                            reportEntityList = new ListDataModel<>();
                            me.addErrorMessage("data_not_have_report_result");
                            me.redirect("/report/list-report.htm");
                            return;
                        }
                        if (innerPerson.size() == 0) {
//                            init();
                            reportEntityList = new ListDataModel<>();
                            me.addErrorMessage("data_not_have_report_result");
                            me.redirect("/report/list-report.htm");
                            return;
                        } else {
                            innerReportEntity = new ArrayList<>();
                            innerReportEntity.addAll(innerPerson);
                            gridObjectType = "Person";
                            personList = new ListDataModel<>(innerPerson);
                        }

                    } else if (staticKind.equalsIgnoreCase("staticHaveNoCardPerson")) {
                        me.getGeneralHelper().getWebServiceInfo().setServiceName("/personWithOutCard");
                        try {
                            innerPerson = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName()), new TypeReference<List<Person>>() {
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                            reportEntityList = new ListDataModel<>();
                            me.addErrorMessage("data_not_have_report_result");
                            me.redirect("/report/list-report.htm");
                            return;
                        }
                        if (innerPerson.size() == 0) {
//                            init();
                            reportEntityList = new ListDataModel<>();
                            me.addErrorMessage("data_not_have_report_result");
                            me.redirect("/report/list-report.htm");
                            return;
                        } else {
                            innerReportEntity = new ArrayList<>();
                            innerReportEntity.addAll(innerPerson);
                            gridObjectType = "Person";
                            personList = new ListDataModel<>(innerPerson);
                        }
                    } else if (staticKind.equalsIgnoreCase("staticDeletedPerson")) {
                        me.getGeneralHelper().getWebServiceInfo().setServiceName("/getDeletedPerson");
                        try {
                            innerPerson = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName()), new TypeReference<List<Person>>() {
                            });
                            gridObjectType = "Person";
                        } catch (IOException e) {
                            e.printStackTrace();
                            reportEntityList = new ListDataModel<>();
                            me.addErrorMessage("data_not_have_report_result");
                            me.redirect("/report/list-report.htm");
                            return;
                        }
                        if (innerPerson.size() == 0) {
//                            init();
                            reportEntityList = new ListDataModel<>();
                            me.addErrorMessage("data_not_have_report_result");
                            me.redirect("/report/list-report.htm");
                            return;
                        } else {
                            innerReportEntity = new ArrayList<>();
                            innerReportEntity.addAll(innerPerson);
                            gridObjectType = "Person";
                            personList = new ListDataModel<>(innerPerson);
                        }
                    }
                    titles = new ArrayList<>();
                    titles.add(me.getValue("name"));
                    titles.add(me.getValue("lastname"));
                } else if (kind.equalsIgnoreCase("staticCard")) {
                    if (staticKind.equalsIgnoreCase("staticAssignCard")) {
                        me.getGeneralHelper().getWebServiceInfo().setServiceName("/assignCard");
                        try {
                            innerCard = new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), startDate + "#" + endDate), new TypeReference<List<Card>>() {
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                            reportEntityList = new ListDataModel<>();
                            me.addErrorMessage("data_not_have_report_result");
                            me.redirect("/report/list-report.htm");
                            return;
                        }
                        if (innerCard.size() == 0) {
//                            init();
                            reportEntityList = new ListDataModel<>();
                            me.addErrorMessage("data_not_have_report_result");
                            me.redirect("/report/list-report.htm");
                            return;
                        } else {
                            innerReportEntity = new ArrayList<>();
                            innerReportEntity.addAll(innerCard);
                            gridObjectType = "Card";
                            cardList = new ListDataModel<>(innerCard);
                        }

                    } else if (staticKind.equalsIgnoreCase("staticLostCard")) {
                        me.getGeneralHelper().getWebServiceInfo().setServiceName("/lostCard");
                        try {
                            innerCard = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName()), new TypeReference<List<Card>>() {
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                            reportEntityList = new ListDataModel<>();
                            me.addErrorMessage("data_not_have_report_result");
                            me.redirect("/report/list-report.htm");
                            return;
                        }
                        if (innerCard.size() == 0) {
//                            init();
                            reportEntityList = new ListDataModel<>();
                            me.addErrorMessage("data_not_have_report_result");
                            me.redirect("/report/list-report.htm");
                            return;
                        } else {
                            innerReportEntity = new ArrayList<>();
                            innerReportEntity.addAll(innerCard);
                            gridObjectType = "Card";
                            cardList = new ListDataModel<>(innerCard);
                        }
                    } else if (staticKind.equalsIgnoreCase("staticClosedCard")) {
                        me.getGeneralHelper().getWebServiceInfo().setServiceName("/closedCard");
                        try {
                            innerCard = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName()), new TypeReference<List<Card>>() {
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                            reportEntityList = new ListDataModel<>();
                            me.addErrorMessage("data_not_have_report_result");
                            me.redirect("/report/list-report.htm");
                            return;
                        }
                        if (innerCard.size() == 0) {
//                            init();
                            reportEntityList = new ListDataModel<>();
                            me.addErrorMessage("data_not_have_report_result");
                            me.redirect("/report/list-report.htm");
                            return;
                        } else {
                            innerReportEntity = new ArrayList<>();
                            innerReportEntity.addAll(innerCard);
                            gridObjectType = "Card";
                            cardList = new ListDataModel<>(innerCard);
                        }
                    } else if (staticKind.equalsIgnoreCase("staticOpenCard")) {
                        me.getGeneralHelper().getWebServiceInfo().setServiceName("/openCard");
                        try {
                            innerCard = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName()), new TypeReference<List<Card>>() {
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                            reportEntityList = new ListDataModel<>();
                            me.addErrorMessage("data_not_have_report_result");
                            me.redirect("/report/list-report.htm");
                            return;
                        }
                        if (innerCard.size() == 0) {
//                            init();
                            reportEntityList = new ListDataModel<>();
                            me.addErrorMessage("data_not_have_report_result");
                            me.redirect("/report/list-report.htm");
                            return;
                        } else {
                            innerReportEntity = new ArrayList<>();
                            innerReportEntity.addAll(innerCard);
                            gridObjectType = "Card";
                            cardList = new ListDataModel<>(innerCard);
                        }
                    } else if (staticKind.equalsIgnoreCase("staticStolenCard")) {
                        me.getGeneralHelper().getWebServiceInfo().setServiceName("/stolenCard");
                        try {
                            innerCard = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName()), new TypeReference<List<Card>>() {
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                            reportEntityList = new ListDataModel<>();
                            me.addErrorMessage("data_not_have_report_result");
                            me.redirect("/report/list-report.htm");
                            return;
                        }
                        if (innerCard.size() == 0) {
//                            init();
                            reportEntityList = new ListDataModel<>();
                            me.addErrorMessage("data_not_have_report_result");
                            me.redirect("/report/list-report.htm");
                            return;
                        } else {
                            innerReportEntity = new ArrayList<>();
                            innerReportEntity.addAll(innerCard);
                            gridObjectType = "Card";
                            cardList = new ListDataModel<>(innerCard);
                        }

                    } else if (staticKind.equalsIgnoreCase("staticEntryInGateWayWithTimeCard")) {
                        query = "select r from ReportEntity r where r.trafficDate >= '" + startDate + "' and r.trafficDate <= '" + endDate + "'";
                        //me.getGeneralHelper().getWebServiceInfo().setServiceName("/getAllPerson");
                        me.getGeneralHelper().getWebServiceInfo().setServiceName("/queryView");
                        try {
                            innerReport = new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), query), new TypeReference<List<ReportEntity>>() {
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                            reportEntityList = new ListDataModel<>();
                            me.addErrorMessage("data_not_have_report_result");
                            me.redirect("/report/list-report.htm");
                            return;
                        }
                        if (innerReport.size() == 0) {
//                            init();
                            reportEntityList = new ListDataModel<>();
                            me.addErrorMessage("data_not_have_report_result");
                            me.redirect("/report/list-report.htm");
                            return;
                        } else {
                            gridObjectType = "ReportEntity";
                            innerReportEntity.addAll(innerReport);
                            reportEntityList = new ListDataModel<>(innerReport);
                        }

                    } else if (staticKind.equalsIgnoreCase("staticUnsuccessfullEntryCard")) {
                        query = "select r from ReportEntity r where r.success = 0";
                        //me.getGeneralHelper().getWebServiceInfo().setServiceName("/getAllPerson");
                        me.getGeneralHelper().getWebServiceInfo().setServiceName("/queryView");
                        try {
                            innerReport = new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), query), new TypeReference<List<ReportEntity>>() {
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                            reportEntityList = new ListDataModel<>();
                            me.addErrorMessage("data_not_have_report_result");
                            me.redirect("/report/list-report.htm");
                            return;
                        }
                        if (innerReport.size() == 0) {
//                            init();
                            reportEntityList = new ListDataModel<>();
                            me.addErrorMessage("data_not_have_report_result");
                            me.redirect("/report/list-report.htm");
                            return;
                        } else {
                            gridObjectType = "ReportEntity";
                            innerReportEntity.addAll(innerReport);
                            reportEntityList = new ListDataModel<>(innerReport);
                        }

                    }
                    titles = new ArrayList<>();
                    titles.add(me.getValue("name"));
                    titles.add(me.getValue("lastname"));
                    titles.add(me.getValue("code"));
                } else if (kind.equalsIgnoreCase("staticGateway")) {
                    if (staticKind.equalsIgnoreCase("staticEnteredPersonGateway")) {
                        query = makeQuery();
                        System.out.println("the query is: " + query);

                        String countQuery = query.replaceFirst("select r", "select count(r)");

                        me.getGeneralHelper().getWebServiceInfo().setServiceName("/queryCountView");
                        Long count = 0L;
                        try {
                            count = new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), countQuery), new TypeReference<Long>() {
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                            reportEntityList = new ListDataModel<>();
                            me.addErrorMessage("data_not_have_report_result");
                            me.redirect("/report/list-report.htm");
                            return;
                        }
                        if (count == 0) {
                            reportEntityList = new ListDataModel<>();
                            me.addErrorMessage("data_not_have_report_result");
                            me.redirect("/report/list-report.htm");
                            return;
                        } else if (count > 5000)// TODO : add in system configuration
                        {
                            savedScheduleQuery(query, count, type, staticKind);
                            return;
                        }
                        //me.getGeneralHelper().getWebServiceInfo().setServiceName("/getAllPerson");
                        me.getGeneralHelper().getWebServiceInfo().setServiceName("/queryView");
                        try {
                            innerReport = new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), query), new TypeReference<List<ReportEntity>>() {
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                            reportEntityList = new ListDataModel<>();
                            me.addErrorMessage("data_not_have_report_result");
                            me.redirect("/report/list-report.htm");
                            return;
                        }
                        if (innerReport.size() == 0) {
//                            init();
                            reportEntityList = new ListDataModel<>();
                            me.addErrorMessage("data_not_have_report_result");
                            me.redirect("/report/list-report.htm");
                            return;
                        } else {
                            gridObjectType = "ReportEntity";
                            innerReportEntity.addAll(innerReport);
                            reportEntityList = new ListDataModel<>(innerReport);
                        }

                    } else if (staticKind.equalsIgnoreCase("staticUnsuccessfullEnteredGateway")) {
                        query = "select r from ReportEntity r where r.success = 0";
                        //me.getGeneralHelper().getWebServiceInfo().setServiceName("/getAllPerson");

                        String countQuery = query.replaceFirst("select r", "select count(r)");

                        me.getGeneralHelper().getWebServiceInfo().setServiceName("/queryCountView");
                        Long count = 0L;
                        try {
                            count = new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), countQuery), new TypeReference<Long>() {
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                            reportEntityList = new ListDataModel<>();
                            me.addErrorMessage("data_not_have_report_result");
                            me.redirect("/report/list-report.htm");
                            return;
                        }
                        if (count == 0) {
                            reportEntityList = new ListDataModel<>();
                            me.addErrorMessage("data_not_have_report_result");
                            me.redirect("/report/list-report.htm");
                            return;
                        } else if (count > 5000)// TODO : add in system configuration
                        {
                            savedScheduleQuery(query, count, type, staticKind);
                            return;
                        }

                        me.getGeneralHelper().getWebServiceInfo().setServiceName("/queryView");
                        try {
                            innerReport = new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), query), new TypeReference<List<ReportEntity>>() {
                            });
                            gridObjectType = "ReportEntity";
                        } catch (IOException e) {
                            e.printStackTrace();
                            reportEntityList = new ListDataModel<>();
                            me.addErrorMessage("data_not_have_report_result");
                            me.redirect("/report/list-report.htm");
                            return;
                        }
                        if (innerReport.size() == 0) {
//                            init();
                            reportEntityList = new ListDataModel<>();
                            me.addErrorMessage("data_not_have_report_result");
                            me.redirect("/report/list-report.htm");
                            return;
                        } else {
                            gridObjectType = "ReportEntity";
                            innerReportEntity.addAll(innerReport);
                            reportEntityList = new ListDataModel<>(innerReport);
                        }
                    }
                    titles = new ArrayList<>();
                    titles.add(me.getValue("name"));
                    titles.add(me.getValue("lastname"));
                    titles.add(me.getValue("gateway"));
                }
//                switch (type) {
//                    case "pdf":
//                        new ReportUtils<>().exportPDF(innerReportEntity, titles, kind);
//                        break;
//                    case "vnd.ms-excel":
//                        new ReportUtils<>().exportExcel(innerReportEntity, titles, kind);
//                        break;
//                    case "txt":
//                        new ReportUtils<>().exportCsv(innerReportEntity, titles, kind);
//                        break;
//                    default:
//                }
            }


        } catch (
                Exception e
                )

        {
            e.printStackTrace();
        }

      /*
       downloadReport();
        init();
        begin();*/
        //  me.redirect("/report/list-report.htm");
    }

    private String makeQuery() {
        queryString = "";
        if (kind.equalsIgnoreCase("person")) {
            if (name != null && !"".equals(name)) {
                queryString += "select r from ReportEntity r where r.name = '" + name + "' ";
            }
            if (lastname != null && !"".equals(lastname)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.lastName = '" + lastname + "' ";
                else if (!lastnameOperation.equalsIgnoreCase("not")) {
                    queryString += lastnameOperation + " r.lastName = '" + lastname + "' ";
                } else {
                    queryString += " and r.lastName != '" + lastname + "' ";
                }
            }
            if (cardNo != null && !"".equals(cardNo)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.cardCode = '" + cardNo + "' ";
                else if (!cardNoOperation.equalsIgnoreCase("not")) {
                    queryString += cardNoOperation + " r.cardCode = '" + cardNo + "' ";
                } else {
                    queryString += "not r.cardCode != '" + cardNo + "' ";
                }
            }
            if (!"0".equalsIgnoreCase(unsucessfullTry)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.success = " + unsucessfullTry + " ";
                else if (!unsucessfullTryOperation.equalsIgnoreCase("not")) {
                    queryString += unsucessfullTryOperation + " r.success = " + unsucessfullTry + " ";
                } else {
                    queryString += " and r.success != " + unsucessfullTry + " ";
                }
            }
       /* if (!"0".equalsIgnoreCase(haveCard)) {
            if ("".equals(queryString))
                queryString += "select t from TrafficLog t where t.person.lastName = '" + lastname + "' ";
            else
                queryString += " and t.person.lastName = '" + lastname + "' ";
        }*/
           /* if (!"0".equalsIgnoreCase(deleted)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.deleted = '" + deleted + "' ";
                else
                    queryString += " and r.deleted = '" + deleted + "' ";
            }*/
         /*   if (!"0".equalsIgnoreCase(enabled)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.person.lastName = '" + lastname + "' ";
                else
                    queryString += " and t.person.lastName = '" + enabled + "' ";
            }*/
       /* if (!"0".equalsIgnoreCase(allowed)) {
            if ("".equals(queryString))
                queryString += "select t from TrafficLog t where t.person.lastName = '" + allowed + "' ";
            else
                queryString += " and t.person.lastName = '" + allowed + "' ";
        }*/
        /*if (!"0".equalsIgnoreCase(haveDescription)) {
            if ("".equals(queryString))
                queryString += "select t from TrafficLog t where t.person.lastName = '" + haveDescription + "' ";
            else
                queryString += " and t.person.lastName = '" + haveDescription + "' ";
        }*/
            if (startDate != null && !"".equals(startDate)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.trafficDate >= '" + startDate + "' ";
                else if (!startDateOperation.equalsIgnoreCase("not")) {
                    queryString += startDateOperation + " r.trafficDate >= '" + startDate + "' ";
                } else {
                    queryString += " and r.trafficDate != '" + startDate + "' ";
                }
            }
            if (endDate != null && !"".equals(endDate)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.trafficDate <= '" + endDate + "' ";
                else if (!endDateOperation.equalsIgnoreCase("not")) {
                    queryString += endDateOperation + " r.trafficDate <= '" + endDate + "' ";
                } else {
                    queryString += " and r.trafficDate != '" + endDate + "' ";
                }
            }
            if (startTime != null && !"000".equals(startTime)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.trafficTime >= '" + startTime + "' ";
                else if (!startTimeOperation.equalsIgnoreCase("not")) {
                    queryString += startTimeOperation + " r.trafficTime >= '" + startTime + "' ";
                } else {
                    queryString += " and r.trafficTime != '" + startTime + "' ";
                }
            }
            if (endTime != null && !"000".equals(endTime)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.trafficTime <= '" + endTime + "' ";
                else if (!endTimeOperation.equalsIgnoreCase("not")) {
                    queryString += endTimeOperation + " r.trafficTime <= '" + endTime + "' ";
                } else {
                    queryString += " and r.trafficTime != '" + endTime + "' ";
                }
            }
            if (gateway != null && !"0".equals(gateway)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.gateName = '" + gateway + "' ";
                else if (!gatewayOperation.equalsIgnoreCase("not")) {
                    queryString += gatewayOperation + " r.gateName = '" + gateway + "' ";
                } else {
                    queryString += " and r.gateName != '" + gateway + "' ";
                }
            }
            if (pdp != null && !"0".equals(pdp)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.pdpName = '" + pdp + "' ";
                else if (!pdpOperation.equalsIgnoreCase("not")) {
                    queryString += pdpOperation + " r.pdpName = '" + pdp + "' ";
                } else {
                    queryString += " and r.pdpName != '" + pdp + "' ";
                }
            }
            if (zone != null && !"0".equals(zone)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.zoneName = '" + zone + "' ";
                else if (!zoneOperation.equalsIgnoreCase("not")) {
                    queryString += zoneOperation + " r.zoneName = '" + zone + "' ";
                } else {
                    queryString += " and  r.zoneName != '" + zone + "' ";
                }
            }
            if (organizationName != null && !"".equals(organizationName)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.organName = '" + organizationName + "' ";
                else if (!organizationNameOperation.equalsIgnoreCase("not")) {
                    queryString += organizationNameOperation + " r.organName = '" + organizationName + "' ";
                } else {
                    queryString += " and r.organName != '" + organizationName + "' ";
                }
            }
            if (entryType != null && !"".equals(entryType)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.entryType = '" + entryType + "' ";
                else if (!entryTypeOperation.equalsIgnoreCase("not")) {
                    queryString += entryTypeOperation + " r.entryType = '" + entryType + "' ";
                } else {
                    queryString += " and r.entryType != '" + entryType + "' ";
                }
            }
            if ("".equals(queryString)) {
                queryString += "select r from ReportEntity r where r.name is not null";
            }
        } else if (kind.equalsIgnoreCase("gateway")) {
            if (name != null && !"".equals(name)) {
                queryString += "select r from ReportEntity r where r.name = '" + name + "'";
            }
            if (lastname != null && !"".equals(lastname)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.lastName = '" + lastname + "'";
                else if (!lastnameOperation.equalsIgnoreCase("not")) {
                    queryString += lastnameOperation + " r.lastName = '" + lastname + "' ";
                } else {
                    queryString += " and r.lastName != '" + lastname + "' ";
                }
            }
            if (cardNo != null && !"".equals(cardNo)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.cardCode = '" + cardNo + "'";
                else if (!cardNoOperation.equalsIgnoreCase("not")) {
                    queryString += cardNoOperation + " r.cardCode = '" + cardNo + "' ";
                } else {
                    queryString += "not r.cardCode != '" + cardNo + "' ";
                }
            }
            if (!"0".equalsIgnoreCase(unsucessfullTry)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.success = " + unsucessfullTry + "";
                else if (!unsucessfullTryOperation.equalsIgnoreCase("not")) {
                    queryString += unsucessfullTryOperation + " r.success = " + unsucessfullTry + " ";
                } else {
                    queryString += " and r.success != " + unsucessfullTry + " ";
                }
            }
       /* if (!"0".equalsIgnoreCase(haveCard)) {
            if ("".equals(queryString))
                queryString += "select t from TrafficLog t where t.person.lastName = '" + lastname + "'";
            else
                queryString += " and t.person.lastName = '" + lastname + "'";
        }*/
          /*  if (!"0".equalsIgnoreCase(deleted)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.deleted = '" + deleted + "'";
                else
                    queryString += " and r.deleted = '" + deleted + "'";
            }*/
         /*   if (!"0".equalsIgnoreCase(enabled)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.person.lastName = '" + lastname + "'";
                else
                    queryString += " and t.person.lastName = '" + enabled + "'";
            }*/
       /* if (!"0".equalsIgnoreCase(allowed)) {
            if ("".equals(queryString))
                queryString += "select t from TrafficLog t where t.person.lastName = '" + allowed + "'";
            else
                queryString += " and t.person.lastName = '" + allowed + "'";
        }*/
        /*if (!"0".equalsIgnoreCase(haveDescription)) {
            if ("".equals(queryString))
                queryString += "select t from TrafficLog t where t.person.lastName = '" + haveDescription + "'";
            else
                queryString += " and t.person.lastName = '" + haveDescription + "'";
        }*/
            if (startDate != null && !"".equals(startDate)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.trafficDate >= '" + startDate + "' ";
                else if (!startDateOperation.equalsIgnoreCase("not")) {
                    queryString += startDateOperation + " r.trafficDate >= '" + startDate + "' ";
                } else {
                    queryString += " and r.trafficDate != '" + startDate + "' ";
                }
            }
            if (endDate != null && !"".equals(endDate)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.trafficDate <= '" + endDate + "' ";
                else if (!endDateOperation.equalsIgnoreCase("not")) {
                    queryString += endDateOperation + " r.trafficDate <= '" + endDate + "' ";
                } else {
                    queryString += " and r.trafficDate != '" + endDate + "' ";
                }
            }
            if (startTime != null && !"000".equals(startTime)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.trafficTime >= '" + startTime + "' ";
                else if (!startTimeOperation.equalsIgnoreCase("not")) {
                    queryString += startTimeOperation + " r.trafficTime >= '" + startTime + "' ";
                } else {
                    queryString += " and r.trafficTime != '" + startTime + "' ";
                }
            }
            if (endTime != null && !"000".equals(endTime)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.trafficTime <= '" + endTime + "' ";
                else if (!endTimeOperation.equalsIgnoreCase("not")) {
                    queryString += endTimeOperation + " r.trafficTime <= '" + endTime + "' ";
                } else {
                    queryString += " and r.trafficTime != '" + endTime + "' ";
                }
            }
            if (gateway != null && !"0".equals(gateway)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.gateName = '" + gateway + "' ";
                else if (!gatewayOperation.equalsIgnoreCase("not")) {
                    queryString += gatewayOperation + " r.gateName = '" + gateway + "' ";
                } else {
                    queryString += " and r.gateName != '" + gateway + "' ";
                }
            }
            if (pdp != null && !"0".equals(pdp)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.pdpName = '" + pdp + "' ";
                else if (!pdpOperation.equalsIgnoreCase("not")) {
                    queryString += pdpOperation + " r.pdpName = '" + pdp + "' ";
                } else {
                    queryString += " and r.pdpName !='" + pdp + "' ";
                }
            }
            /*if (zone != null && !"0".equals(zone)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.zoneName = '" + zone + "' ";
                else if (!zoneOperation.equalsIgnoreCase("not")) {
                    queryString += zoneOperation + " r.zoneName = '" + zone + "' ";
                } else {
                    queryString += " and  r.zoneName != '" + zone + "' ";
                }
            }*/
            if (organizationName != null && !"".equals(organizationName)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.organName = '" + organizationName + "' ";
                else if (!organizationNameOperation.equalsIgnoreCase("not")) {
                    queryString += organizationNameOperation + " r.organName = '" + organizationName + "' ";
                } else {
                    queryString += " and r.organName != '" + organizationName + "' ";
                }
            }
            if (entryType != null && !"".equals(entryType)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.entryType = '" + entryType + "' ";
                else if (!entryTypeOperation.equalsIgnoreCase("not")) {
                    queryString += entryTypeOperation + " r.entryType = '" + entryType + "' ";
                } else {
                    queryString += " and r.entryType != '" + entryType + "' ";
                }
            }
            if ("".equals(queryString)) {
                queryString += "select r from ReportEntity r where r.gateName is not null";
            }
        } else if (kind.equalsIgnoreCase("zone")) {
            if (name != null && !"".equals(name)) {
                queryString += "select r from ReportEntity r where r.name = '" + name + "' ";
            }
            if (lastname != null && !"".equals(lastname)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.lastName = '" + lastname + "' ";
                else if (!lastnameOperation.equalsIgnoreCase("not")) {
                    queryString += lastnameOperation + " r.lastName = '" + lastname + "' ";
                } else {
                    queryString += " and r.lastName != '" + lastname + "' ";
                }
            }
            if (cardNo != null && !"".equals(cardNo)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.cardCode = '" + cardNo + "' ";
                else if (!cardNoOperation.equalsIgnoreCase("not")) {
                    queryString += cardNoOperation + " r.cardCode = '" + cardNo + "' ";
                } else {
                    queryString += "not r.cardCode != '" + cardNo + "' ";
                }
            }
            if (!"0".equalsIgnoreCase(unsucessfullTry)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.success = " + unsucessfullTry + "";
                else if (!unsucessfullTryOperation.equalsIgnoreCase("not")) {
                    queryString += unsucessfullTryOperation + " r.success = " + unsucessfullTry + " ";
                } else {
                    queryString += " and r.success != " + unsucessfullTry + " ";
                }
            }
       /* if (!"0".equalsIgnoreCase(haveCard)) {
            if ("".equals(queryString))
                queryString += "select t from TrafficLog t where t.person.lastName = '" + lastname + "' ";
            else
                queryString += " and t.person.lastName = '" + lastname + "' ";
        }*/
          /*  if (!"0".equalsIgnoreCase(deleted)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.deleted = '" + deleted + "' ";
                else
                    queryString += " and r.deleted = '" + deleted + "' ";
            }*/
         /*   if (!"0".equalsIgnoreCase(enabled)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.person.lastName = '" + lastname + "' ";
                else
                    queryString += " and t.person.lastName = '" + enabled + "' ";
            }*/
       /* if (!"0".equalsIgnoreCase(allowed)) {
            if ("".equals(queryString))
                queryString += "select t from TrafficLog t where t.person.lastName = '" + allowed + "' ";
            else
                queryString += " and t.person.lastName = '" + allowed + "' ";
        }*/
        /*if (!"0".equalsIgnoreCase(haveDescription)) {
            if ("".equals(queryString))
                queryString += "select t from TrafficLog t where t.person.lastName = '" + haveDescription + "' ";
            else
                queryString += " and t.person.lastName = '" + haveDescription + "' ";
        }*/
            if (startDate != null && !"".equals(startDate)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.trafficDate >= '" + startDate + "' ";
                else {
                    queryString += " and r.trafficDate >= '" + startDate + "' ";
                }
            }
            if (endDate != null && !"".equals(endDate)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.trafficDate <= '" + endDate + "' ";
                else {
                    queryString += " and r.trafficDate <= '" + endDate + "' ";
                }
            }
            if (startTime != null && !"000".equals(startTime) && !startTime.equalsIgnoreCase("")) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.trafficTime >= '" + startTime + "' ";
                else {
                    queryString += " and r.trafficTime >= '" + startTime + "' ";
                }
            }
            if (endTime != null && !"000".equals(endTime) && !endTime.equalsIgnoreCase("")) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.trafficTime <= '" + endTime + "' ";
                else {
                    queryString += " and r.trafficTime <= '" + endTime + "' ";
                }
            }
           /* if (gateway != null && !"0".equals(gateway) && !gateway.equalsIgnoreCase("")) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.gateName = '" + gateway + "' ";
                else if (!gatewayOperation.equalsIgnoreCase("not")) {
                    queryString += gatewayOperation + " r.gateName = '" + gateway + "' ";
                } else {
                    queryString += " and r.gateName != '" + gateway + "' ";
                }
            }
            if (pdp != null && !"0".equals(pdp) && !pdp.equalsIgnoreCase("")) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.pdpName = '" + pdp + "' ";
                else if (!pdpOperation.equalsIgnoreCase("not")) {
                    queryString += pdpOperation + " r.pdpName = '" + pdp + "' ";
                } else {
                    queryString += " and r.pdpName !='" + pdp + "' ";
                }
            }*/
            if (zone != null && !"0".equals(zone) && !zone.equalsIgnoreCase("")) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.zoneName = '" + zone + "' ";
                else if (!zoneOperation.equalsIgnoreCase("not")) {
                    queryString += zoneOperation + " r.zoneName = '" + zone + "' ";
                } else {
                    queryString += " and  r.zoneName != '" + zone + "' ";
                }
            }
            if (organizationName != null && !"".equals(organizationName)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.organName = '" + organizationName + "' ";
                else if (!organizationNameOperation.equalsIgnoreCase("not")) {
                    queryString += organizationNameOperation + " r.organName = '" + organizationName + "' ";
                } else {
                    queryString += " and r.organName != '" + organizationName + "' ";
                }
            }
            if (entryType != null && !"".equals(entryType)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.entryType = '" + entryType + "' ";
                else if (!entryTypeOperation.equalsIgnoreCase("not")) {
                    queryString += entryTypeOperation + " r.entryType = '" + entryType + "' ";
                } else {
                    queryString += " and r.entryType != '" + entryType + "' ";
                }
            }
            if ("".equals(queryString)) {
                queryString += "select r from ReportEntity r where r.zoneName is not null";
            }
        } /*else if (kind.equalsIgnoreCase("card")) {
            if (name != null && !"".equals(name)) {
                queryString += "select r from ReportEntity r where r.name = '" + name + "' ";
            }
            if (lastname != null && !"".equals(lastname)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.lastName = '" + lastname + "' ";
               else if(!lastnameOperation.equalsIgnoreCase("not")){
                    queryString += lastnameOperation + " r.lastName = '" + lastname + "' ";
                }else {
                    queryString += " and r.lastName != '" + lastname + "' ";
                }
            }
            if (cardNo != null && !"".equals(cardNo)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.cardCode = '" + cardNo + "' ";
                 else if (!cardNoOperation.equalsIgnoreCase("not")) {
                    queryString += cardNoOperation + " r.cardCode = '" + cardNo + "' ";
                } else {
                    queryString += "not r.cardCode != '" + cardNo + "' ";
                }
            }
            if (!"0".equalsIgnoreCase(unsucessfullTry)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.success = " + unsucessfullTry + "";
                else
                    queryString += unsucessfullTryOperation + " r.success = " + unsucessfullTry + " ";
            }
       *//* if (!"0".equalsIgnoreCase(haveCard)) {
            if ("".equals(queryString))
                queryString += "select t from TrafficLog t where t.person.lastName = '" + lastname + "' ";
            else
                queryString += " and t.person.lastName = '" + lastname + "' ";
        }*//*
          *//*  if (!"0".equalsIgnoreCase(deleted)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.deleted = '" + deleted + "' ";
                else
                    queryString += " and r.deleted = '" + deleted + "' ";
            }*//*
         *//*   if (!"0".equalsIgnoreCase(enabled)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.person.lastName = '" + lastname + "' ";
                else
                    queryString += " and t.person.lastName = '" + enabled + "' ";
            }*//*
       *//* if (!"0".equalsIgnoreCase(allowed)) {
            if ("".equals(queryString))
                queryString += "select t from TrafficLog t where t.person.lastName = '" + allowed + "' ";
            else
                queryString += " and t.person.lastName = '" + allowed + "' ";
        }*//*
        *//*if (!"0".equalsIgnoreCase(haveDescription)) {
            if ("".equals(queryString))
                queryString += "select t from TrafficLog t where t.person.lastName = '" + haveDescription + "' ";
            else
                queryString += " and t.person.lastName = '" + haveDescription + "' ";
        }*//*
            if (startDate != null && !"".equals(startDate)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.trafficDate >= '" + startDate + "' ";
                else
                    queryString += " and r.trafficDate >= '" + startDate + "' ";
            }
            if (endDate != null && !"".equals(endDate)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.trafficDate <= '" + endDate + "' ";
                else
                    queryString += " and r.trafficDate <= '" + endDate + "' ";
            }
            if (startTime != null && !"000".equals(startTime)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.trafficTime >= '" + startTime + "' ";
                else
                    queryString += " and r.trafficTime >= '" + startTime + "' ";
            }
            if (endTime != null && !"000".equals(endTime)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.trafficTime <= '" + endTime + "' ";
                else
                     queryString += endTimeOperation + " r.trafficTime <= '" + endTime + "' ";
            }
            if (gateway != null && !"0".equals(gateway)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.gateName = '" + gateway + "' ";
                else
                                        queryString += gatewayOperation + " r.gateName = '" + gateway + "' ";
            }
            if (pdp != null && !"0".equals(pdp)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.pdpName = '" + pdp + "' ";
                else
                   queryString += pdpOperation + " r.pdpName = '" + pdp + "' ";
            }
            if (zone != null && !"0".equals(zone)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.zoneName = '" + zone + "' ";
              else if (!zoneOperation.equalsIgnoreCase("not")) {
                    queryString += zoneOperation + " r.zoneName = '" + zone + "' ";
                } else {
                    queryString += " and  r.zoneName != '" + zone + "' ";
                }
            }
            if (organizationName != null && !"".equals(organizationName)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.organName = '" + organizationName + "' ";
                else
                   queryString += organizationNameOperation + " r.organName = '" + organizationName + "' ";
            }
            if (entryType!= null && !"".equals(entryType)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.entryType = '" + entryType + "' ";
                else if (!entryTypeOperation.equalsIgnoreCase("not")) {
                    queryString += entryTypeOperation + " r.entryType = '" + entryType + "' ";
                } else {
                    queryString += " and r.entryType != '" + entryType + "' ";
                }
            }
            if ("".equals(queryString)) {
                queryString += "select r from ReportEntity r where r.cardCode is not null";
            }
        }*/ else if (kind.equalsIgnoreCase("pdp")) {
            if (name != null && !"".equals(name)) {
                queryString += "select r from ReportEntity r where r.name = '" + name + "' ";
            }
            if (lastname != null && !"".equals(lastname)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.lastName = '" + lastname + "' ";
                else if (!lastnameOperation.equalsIgnoreCase("not")) {
                    queryString += lastnameOperation + " r.lastName = '" + lastname + "' ";
                } else {
                    queryString += " and r.lastName != '" + lastname + "' ";
                }
            }
            if (cardNo != null && !"".equals(cardNo)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.cardCode = '" + cardNo + "' ";
                else if (!cardNoOperation.equalsIgnoreCase("not")) {
                    queryString += cardNoOperation + " r.cardCode = '" + cardNo + "' ";
                } else {
                    queryString += "not r.cardCode != '" + cardNo + "' ";
                }
            }
            if (!"0".equalsIgnoreCase(unsucessfullTry)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.success = " + unsucessfullTry + "";
                else if (!unsucessfullTryOperation.equalsIgnoreCase("not")) {
                    queryString += unsucessfullTryOperation + " r.success = " + unsucessfullTry + " ";
                } else {
                    queryString += " and r.success != " + unsucessfullTry + " ";
                }
            }
       /* if (!"0".equalsIgnoreCase(haveCard)) {
            if ("".equals(queryString))
                queryString += "select t from TrafficLog t where t.person.lastName = '" + lastname + "' ";
            else
                queryString += " and t.person.lastName = '" + lastname + "' ";
        }*/
          /*  if (!"0".equalsIgnoreCase(deleted)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.deleted = '" + deleted + "' ";
                else
                    queryString += " and r.deleted = '" + deleted + "' ";
            }*/
         /*   if (!"0".equalsIgnoreCase(enabled)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.person.lastName = '" + lastname + "' ";
                else
                    queryString += " and t.person.lastName = '" + enabled + "' ";
            }*/
       /* if (!"0".equalsIgnoreCase(allowed)) {
            if ("".equals(queryString))
                queryString += "select t from TrafficLog t where t.person.lastName = '" + allowed + "' ";
            else
                queryString += " and t.person.lastName = '" + allowed + "' ";
        }*/
        /*if (!"0".equalsIgnoreCase(haveDescription)) {
            if ("".equals(queryString))
                queryString += "select t from TrafficLog t where t.person.lastName = '" + haveDescription + "' ";
            else
                queryString += " and t.person.lastName = '" + haveDescription + "' ";
        }*/
            if (startDate != null && !"".equals(startDate)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.trafficDate >= '" + startDate + "'";
                else
                    queryString += " and r.trafficDate >= '" + startDate + "'";
            }
            if (endDate != null && !"".equals(endDate)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.trafficDate <= '" + endDate + "'";
                else
                    queryString += " and r.trafficDate <= '" + endDate + "'";
            }
            if (startTime != null && !"000".equals(startTime)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.trafficTime >= '" + startTime + "'";
                else
                    queryString += " and r.trafficTime >= '" + startTime + "'";
            }
            if (endTime != null && !"000".equals(endTime)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.trafficTime <= '" + endTime + "' ";
                else if (!endTimeOperation.equalsIgnoreCase("not")) {
                    queryString += endTimeOperation + " r.trafficTime <= '" + endTime + "' ";
                } else {
                    queryString += " and r.trafficTime != '" + endTime + "' ";
                }
            }
           /* if (gateway != null && !"0".equals(gateway)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.gateName = '" + gateway + "' ";
                else if (!gatewayOperation.equalsIgnoreCase("not")) {
                    queryString += gatewayOperation + " r.gateName = '" + gateway + "' ";
                } else {
                    queryString += " and r.gateName != '" + gateway + "' ";
                }
            }*/
            if (pdp != null && !"0".equals(pdp)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.pdpName = '" + pdp + "' ";
                else if (!pdpOperation.equalsIgnoreCase("not")) {
                    queryString += pdpOperation + " r.pdpName = '" + pdp + "' ";
                } else {
                    queryString += " and r.pdpName !='" + pdp + "' ";
                }
            }
           /* if (zone != null && !"0".equals(zone)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.zoneName = '" + zone + "' ";
                else if (!zoneOperation.equalsIgnoreCase("not")) {
                    queryString += zoneOperation + " r.zoneName = '" + zone + "' ";
                } else {
                    queryString += " and  r.zoneName != '" + zone + "' ";
                }
            }*/
            if (organizationName != null && !"".equals(organizationName)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.organName = '" + organizationName + "' ";
                else if (!organizationNameOperation.equalsIgnoreCase("not")) {
                    queryString += organizationNameOperation + " r.organName = '" + organizationName + "' ";
                } else {
                    queryString += " and r.organName != '" + organizationName + "' ";
                }
            }
            if (entryType != null && !"".equals(entryType)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.entryType = '" + entryType + "' ";
                else if (!entryTypeOperation.equalsIgnoreCase("not")) {
                    queryString += entryTypeOperation + " r.entryType = '" + entryType + "' ";
                } else {
                    queryString += " and r.entryType != '" + entryType + "' ";
                }
            }
            if ("".equals(queryString)) {
                queryString += "select r from ReportEntity r where r.pdpName is not null";
            }
        } else if (kind.equalsIgnoreCase("staticGateway")) {
            if (name != null && !"".equals(name)) {
                queryString += "select r from ReportEntity r where r.name = '" + name + "'";
            }
            if (lastname != null && !"".equals(lastname)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.lastName = '" + lastname + "'";
                else
                    queryString += " and r.lastName = '" + lastname + "'";
            }
            if (cardNo != null && !"".equals(cardNo)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.cardCode = '" + cardNo + "'";
                else if (!cardNoOperation.equalsIgnoreCase("not")) {
                    queryString += cardNoOperation + " r.cardCode = '" + cardNo + "' ";
                } else {
                    queryString += "not r.cardCode != '" + cardNo + "' ";
                }
            }
            if (unsucessfullTry != null && !"0".equalsIgnoreCase(unsucessfullTry)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.success = " + unsucessfullTry + "";
                else
                    queryString += " and r.success = " + unsucessfullTry + "";
            }
       /* if (!"0".equalsIgnoreCase(haveCard)) {
            if ("".equals(queryString))
                queryString += "select t from TrafficLog t where t.person.lastName = '" + lastname + "'";
            else
                queryString += " and t.person.lastName = '" + lastname + "'";
        }*/
          /*  if (!"0".equalsIgnoreCase(deleted)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.deleted = '" + deleted + "'";
                else
                    queryString += " and r.deleted = '" + deleted + "'";
            }*/
         /*   if (!"0".equalsIgnoreCase(enabled)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.person.lastName = '" + lastname + "'";
                else
                    queryString += " and t.person.lastName = '" + enabled + "'";
            }*/
       /* if (!"0".equalsIgnoreCase(allowed)) {
            if ("".equals(queryString))
                queryString += "select t from TrafficLog t where t.person.lastName = '" + allowed + "'";
            else
                queryString += " and t.person.lastName = '" + allowed + "'";
        }*/
        /*if (!"0".equalsIgnoreCase(haveDescription)) {
            if ("".equals(queryString))
                queryString += "select t from TrafficLog t where t.person.lastName = '" + haveDescription + "'";
            else
                queryString += " and t.person.lastName = '" + haveDescription + "'";
        }*/
            if (startDate != null && !"".equals(startDate)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.trafficDate >= '" + startDate + "'";
                else
                    queryString += " and r.trafficDate >= '" + startDate + "'";
            }
            if (endDate != null && !"".equals(endDate)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.trafficDate <= '" + endDate + "'";
                else
                    queryString += " and r.trafficDate <= '" + endDate + "'";
            }
            if (startTime != null && !"000".equals(startTime)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.trafficTime >= '" + startTime + "'";
                else
                    queryString += " and r.trafficTime >= '" + startTime + "'";
            }
            if (endTime != null && !"000".equals(endTime)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.trafficTime <= '" + endTime + "'";
                else
                    queryString += " and r.trafficTime <= '" + endTime + "'";
            }
            if (gateway != null && !"0".equals(gateway)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.gateName = '" + gateway + "'";
                else
                    queryString += " and r.gateName != '" + gateway + "'";
            }
            if (pdp != null && !"0".equals(pdp)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.pdpName = '" + pdp + "'";
                else
                    queryString += " and r.pdpName !='" + pdp + "'";
            }
            if (zone != null && !"0".equals(zone)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.zoneName = '" + zone + "'";
                else
                    queryString += " and r.zoneName = '" + zone + "'";
            }
            if (organizationName != null && !"".equals(organizationName)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.organName = '" + organizationName + "'";
                else
                    queryString += " and r.organName = '" + organizationName + "'";
            }
            if (entryType != null && !"".equals(entryType)) {
                if ("".equals(queryString))
                    queryString += "select r from ReportEntity r where r.entryType = '" + entryType + "'";
                else
                    queryString += " and r.entryType = '" + entryType + "'";
            }
            if ("".equals(queryString)) {
                queryString += "select r from ReportEntity r";
            }
        }
        return queryString;
    }

    public void downloadReport() {
        if (reportType.equalsIgnoreCase("dynamic")) {
            addTitles();
        }
        switch (type) {
            case "pdf":
                new ReportUtils<>().exportPDF(innerReportEntity, titles, kind);
                break;
            case "vnd.ms-excel":
                new ReportUtils<>().exportExcel(innerReportEntity, titles, kind);
                break;
            case "txt":
                new ReportUtils<>().exportCsv(innerReportEntity, titles, kind);
                break;
            default:
        }
    }

    private void addTitles() {
        if (kind.equalsIgnoreCase("person")) {
            titles = new ArrayList<>();
            titles.add(me.getValue("name"));
            titles.add(me.getValue("lastname"));
            titles.add(me.getValue("organ"));
            //titles.add(me.getValue("post"));
            titles.add(me.getValue("gateway"));
            //titles.add(me.getValue("zone"));
            //titles.add(me.getValue("pdp"));
            titles.add(me.getValue("date"));
            titles.add(me.getValue("time"));
            titles.add(me.getValue("entry_type"));
            titles.add(me.getValue("success"));
        } else if (kind.equalsIgnoreCase("user")) {
            titles = new ArrayList<>();
            titles.add(me.getValue("name"));
            titles.add(me.getValue("lastname"));
            titles.add(me.getValue("organ"));
            //titles.add(me.getValue("post"));
            titles.add(me.getValue("gateway"));
            //titles.add(me.getValue("zone"));
            //titles.add(me.getValue("pdp"));
            titles.add(me.getValue("date"));
            titles.add(me.getValue("time"));
            titles.add(me.getValue("entry_type"));
            titles.add(me.getValue("success"));
        } else if (kind.equalsIgnoreCase("gateway")) {
            titles = new ArrayList<>();
            titles.add(me.getValue("name"));
            titles.add(me.getValue("lastname"));
            titles.add(me.getValue("organ"));
            //titles.add(me.getValue("post"));
            titles.add(me.getValue("gateway"));
            //titles.add(me.getValue("zone"));
            //titles.add(me.getValue("pdp"));
            titles.add(me.getValue("date"));
            titles.add(me.getValue("time"));
            titles.add(me.getValue("entry_type"));
            titles.add(me.getValue("success"));
        } else if (kind.equalsIgnoreCase("zone")) {
            titles = new ArrayList<>();
            titles.add(me.getValue("name"));
            titles.add(me.getValue("lastname"));
            titles.add(me.getValue("organ"));
            //titles.add(me.getValue("post"));
            //titles.add(me.getValue("gateway"));
            titles.add(me.getValue("zone"));
            //titles.add(me.getValue("pdp"));
            titles.add(me.getValue("date"));
            titles.add(me.getValue("time"));
            titles.add(me.getValue("entry_type"));
            titles.add(me.getValue("success"));
        } else if (kind.equalsIgnoreCase("card")) {
            titles = new ArrayList<>();
            titles.add(me.getValue("name"));
            titles.add(me.getValue("lastname"));
            titles.add(me.getValue("organ"));
            //titles.add(me.getValue("post"));
            titles.add(me.getValue("gateway"));
            //titles.add(me.getValue("zone"));
            //titles.add(me.getValue("pdp"));
            titles.add(me.getValue("date"));
            titles.add(me.getValue("time"));
            titles.add(me.getValue("entry_type"));
            titles.add(me.getValue("success"));
        } else if (kind.equalsIgnoreCase("pdp")) {
            titles = new ArrayList<>();
            titles.add(me.getValue("name"));
            titles.add(me.getValue("lastname"));
            titles.add(me.getValue("organ"));
            //titles.add(me.getValue("post"));
            //titles.add(me.getValue("gateway"));
            //titles.add(me.getValue("zone"));
            titles.add(me.getValue("pdp"));
            titles.add(me.getValue("date"));
            titles.add(me.getValue("time"));
            titles.add(me.getValue("entry_type"));
            titles.add(me.getValue("success"));
        }
    }

    public void save() {

        System.out.println("ready to save:   " + queryString + "  and the name is: " + reportSaveName);
        SavedQuery savedQuery = new SavedQuery();
        savedQuery.setQuery(queryString);
        savedQuery.setTitle(reportSaveName);
        savedQuery.setType(kind);
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/createSavedQuery");
        SavedQuery savedQuery1 = null;
        try {
            savedQuery1 = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(savedQuery)), SavedQuery.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        init();
        me.redirect("/report/list-report.htm");
    }

    public void execute() {
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/queryView");
        try {
            innerReport = new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), currentSavedQuery.getQuery()), new TypeReference<List<ReportEntity>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
            me.addErrorMessage("data_not_have_report_result");
            me.redirect("/report/list-saved-query.htm");
            return;
        }
        if (innerReport.size() == 0) {
//            init();
            me.addErrorMessage("data_not_have_report_result");
            me.redirect("/report/list-saved-query.htm");
            return;
        } else {
            gridObjectType = "ReportEntity";
            innerReportEntity = new ArrayList<>();
            innerReportEntity.addAll(innerReport);
            reportEntityList = new ListDataModel<>(innerReport);
        }
        setReportType("dynamic");
        kind = currentSavedQuery.getType();
        downloadReport();
        setSelectRow(false);
        saveQueryBegin();
    }

    public void doDelete() {
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/deleteSavedQuery");
        try {
            String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(currentSavedQuery)), String.class);
            me.addInfoMessage(condition);
            saveQueryBegin();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setSelectRow(false);
        saveQueryBegin();
        me.redirect("/report/list-report.htm");
    }

    private void savedScheduleQuery(String query, Long count, String type, String kind) {

        me.getGeneralHelper().getWebServiceInfo().setServiceName("/getScheduledQuery");
        List<SavedQuery> savedQueries = null;
        try {
            savedQueries = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName()), new TypeReference<List<SavedQuery>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        Long upToNowCount = 0L;
        for (SavedQuery savedQuery : savedQueries) {
            upToNowCount += savedQuery.getCount();
        }
        //TODO : Add system configuration
        if (upToNowCount > 10000) {
            me.addErrorMessage("result_list_is_too_large");
            me.addErrorMessage("count_of_savedQuery_is_too_large");
            me.redirect("/report/list-report.htm");
            return;
        }
//        SavedQuery savedQuery = new SavedQuery(query, "1", count, type, kind);
//        me.getGeneralHelper().getWebServiceInfo().setServiceName("/createSavedQuery");
        SavedQuery savedQuery1 = null;
//        try {
//            savedQuery1 = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(savedQuery)), SavedQuery.class);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        me.addErrorMessage("result_list_is_too_large");
        me.redirect("/report/list-report.htm");
        return;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public SelectItem[] getItems() {
        items[0] = new SelectItem("0", "");
        items[1] = new SelectItem("person", "");
        items[2] = new SelectItem("gateway", "ѐ");
        items[3] = new SelectItem("zone", "?");
        items[4] = new SelectItem("pdp", "? ? ?");
//        items[5] = new SelectItem("card", "");
//        items[6] = new SelectItem("user", " ");
//        items[7] = new SelectItem("time", "");
        return items;
    }

    public SelectItem[] getBooleanItems() {
        booleanItems[0] = new SelectItem("true", "?");
        booleanItems[1] = new SelectItem("false", "?");
        return booleanItems;
    }

    public SelectItem[] getReportTypeItems() {
        reportTypeItems[0] = new SelectItem("static", "");
        reportTypeItems[1] = new SelectItem("dynamic", "?");
        return reportTypeItems;
    }

    public SelectItem[] getStaticReportItems() {
        staticReportItems[0] = new SelectItem("0", "");
        staticReportItems[1] = new SelectItem("staticPerson", "");
        staticReportItems[2] = new SelectItem("staticCard", "");
        staticReportItems[3] = new SelectItem("staticGateway", "?");
        return staticReportItems;
    }

    public SelectItem[] getStaticPersonReportItems() {
        staticPersonReportItems[0] = new SelectItem("staticAllPerson", " ");
        staticPersonReportItems[1] = new SelectItem("staticUnsuccessEntryPerson", "   ");
        staticPersonReportItems[2] = new SelectItem("staticHaveCardPerson", "? ");
        staticPersonReportItems[3] = new SelectItem("staticHaveNoCardPerson", " ");
        staticPersonReportItems[4] = new SelectItem("staticDeletedPerson", "  ");
        return staticPersonReportItems;
    }

    public SelectItem[] getStaticCardReportItems() {
        staticCardReportItems[0] = new SelectItem("staticLostCard", " ?  ");
        staticCardReportItems[1] = new SelectItem("staticAssignCard", " ?  ");
        staticCardReportItems[2] = new SelectItem("staticClosedCard", " ?  ");
        staticCardReportItems[3] = new SelectItem("staticOpenCard", " ? ? ");
        staticCardReportItems[4] = new SelectItem("staticStolenCard", " ?  ");
        staticCardReportItems[5] = new SelectItem("staticEntryInGateWayWithTimeCard", " ?     ");
        staticCardReportItems[6] = new SelectItem("staticUnsuccessfullEntryCard", " ?  ");
        return staticCardReportItems;
    }

    public SelectItem[] getStaticGatewayReportItems() {
        staticGatewayReportItems[0] = new SelectItem("staticUnsuccessfullEnteredGateway", "? ");
        staticGatewayReportItems[1] = new SelectItem("staticEnteredPersonGateway", "  ");
        return staticGatewayReportItems;
    }

    public SelectItem[] getOperationItems() {
        operationItems[0] = new SelectItem("and", "");
        operationItems[1] = new SelectItem("or", "?");
        operationItems[2] = new SelectItem("not", "? ");
        return operationItems;
    }

    public void selectForEdit() {
        currentSavedQuery = querieList.getRowData();
        setSelectRow(true);

    }

    public void setStaticReportItems(SelectItem[] staticReportItems) {
        this.staticReportItems = staticReportItems;
    }

    public void setReportTypeItems(SelectItem[] reportTypeItems) {
        this.reportTypeItems = reportTypeItems;
    }

    public void setItems(SelectItem[] items) {
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getHaveCard() {
        return haveCard;
    }

    public void setHaveCard(String haveCard) {
        this.haveCard = haveCard;
    }

    public String getUnsucessfullTry() {
        return unsucessfullTry;
    }

    public void setUnsucessfullTry(String unsucessfullTry) {
        this.unsucessfullTry = unsucessfullTry;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getWorkStation() {
        return workStation;
    }

    public void setWorkStation(String workStation) {
        this.workStation = workStation;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getAllowed() {
        return allowed;
    }

    public void setAllowed(String allowed) {
        this.allowed = allowed;
    }

    public String getHaveDescription() {
        return haveDescription;
    }

    public void setHaveDescription(String haveDescription) {
        this.haveDescription = haveDescription;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public String getPdp() {
        return pdp;
    }

    public void setPdp(String pdp) {
        this.pdp = pdp;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setBooleanItems(SelectItem[] booleanItems) {
        this.booleanItems = booleanItems;
    }

    public String getEntryType() {
        return entryType;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getStartSecond() {
        return startSecond;
    }

    public void setStartSecond(String startSecond) {
        this.startSecond = startSecond;
    }

    public String getStartMinutes() {
        return startMinutes;
    }

    public void setStartMinutes(String startMinutes) {
        this.startMinutes = startMinutes;
    }

    public String getStartHour() {
        return startHour;
    }

    public void setStartHour(String startHour) {
        this.startHour = startHour;
    }

    public String getEndSecond() {
        return endSecond;
    }

    public void setEndSecond(String endSecond) {
        this.endSecond = endSecond;
    }

    public String getEndMinutes() {
        return endMinutes;
    }

    public void setEndMinutes(String endMinutes) {
        this.endMinutes = endMinutes;
    }

    public String getEndHour() {
        return endHour;
    }

    public void setEndHour(String endHour) {
        this.endHour = endHour;
    }

    public String getStaticKind() {
        return staticKind;
    }

    public void setStaticKind(String staticKind) {
        this.staticKind = staticKind;
    }

    public String getNameOperation() {
        return nameOperation;
    }

    public void setNameOperation(String nameOperation) {
        this.nameOperation = nameOperation;
    }

    public String getLastnameOperation() {
        return lastnameOperation;
    }

    public void setLastnameOperation(String lastnameOperation) {
        this.lastnameOperation = lastnameOperation;
    }

    public String getCardNoOperation() {
        return cardNoOperation;
    }

    public void setCardNoOperation(String cardNoOperation) {
        this.cardNoOperation = cardNoOperation;
    }

    public String getHaveCardOperation() {
        return haveCardOperation;
    }

    public void setHaveCardOperation(String haveCardOperation) {
        this.haveCardOperation = haveCardOperation;
    }

    public String getUnsucessfullTryOperation() {
        return unsucessfullTryOperation;
    }

    public void setUnsucessfullTryOperation(String unsucessfullTryOperation) {
        this.unsucessfullTryOperation = unsucessfullTryOperation;
    }

    public String getOrganizationNameOperation() {
        return organizationNameOperation;
    }

    public void setOrganizationNameOperation(String organizationNameOperation) {
        this.organizationNameOperation = organizationNameOperation;
    }

    public String getDeletedOperation() {
        return deletedOperation;
    }

    public void setDeletedOperation(String deletedOperation) {
        this.deletedOperation = deletedOperation;
    }

    public String getWorkStationOperation() {
        return workStationOperation;
    }

    public void setWorkStationOperation(String workStationOperation) {
        this.workStationOperation = workStationOperation;
    }

    public String getEnabledOperation() {
        return enabledOperation;
    }

    public void setEnabledOperation(String enabledOperation) {
        this.enabledOperation = enabledOperation;
    }

    public String getZoneOperation() {
        return zoneOperation;
    }

    public void setZoneOperation(String zoneOperation) {
        this.zoneOperation = zoneOperation;
    }

    public String getAllowedOperation() {
        return allowedOperation;
    }

    public void setAllowedOperation(String allowedOperation) {
        this.allowedOperation = allowedOperation;
    }

    public String getHaveDescriptionOperation() {
        return haveDescriptionOperation;
    }

    public void setHaveDescriptionOperation(String haveDescriptionOperation) {
        this.haveDescriptionOperation = haveDescriptionOperation;
    }

    public String getGatewayOperation() {
        return gatewayOperation;
    }

    public void setGatewayOperation(String gatewayOperation) {
        this.gatewayOperation = gatewayOperation;
    }

    public String getPdpOperation() {
        return pdpOperation;
    }

    public void setPdpOperation(String pdpOperation) {
        this.pdpOperation = pdpOperation;
    }

    public String getStartDateOperation() {
        return startDateOperation;
    }

    public void setStartDateOperation(String startDateOperation) {
        this.startDateOperation = startDateOperation;
    }

    public String getEndDateOperation() {
        return endDateOperation;
    }

    public void setEndDateOperation(String endDateOperation) {
        this.endDateOperation = endDateOperation;
    }

    public String getStartTimeOperation() {
        return startTimeOperation;
    }

    public void setStartTimeOperation(String startTimeOperation) {
        this.startTimeOperation = startTimeOperation;
    }

    public String getEndTimeOperation() {
        return endTimeOperation;
    }

    public void setEndTimeOperation(String endTimeOperation) {
        this.endTimeOperation = endTimeOperation;
    }

    public String getEntryTypeOperation() {
        return entryTypeOperation;
    }

    public void setEntryTypeOperation(String entryTypeOperation) {
        this.entryTypeOperation = entryTypeOperation;
    }

    public String getGridObjectType() {
        return gridObjectType;
    }

    public void setGridObjectType(String gridObjectType) {
        this.gridObjectType = gridObjectType;
    }

    public DataModel<ReportEntity> getReportEntityList() {
        return reportEntityList;
    }

    public void setReportEntityList(DataModel<ReportEntity> reportEntityList) {
        this.reportEntityList = reportEntityList;
    }

    public DataModel<Person> getPersonList() {
        return personList;
    }

    public void setPersonList(DataModel<Person> personList) {
        this.personList = personList;
    }

    public DataModel<Card> getCardList() {
        return cardList;
    }

    public void setCardList(DataModel<Card> cardList) {
        this.cardList = cardList;
    }

    public int getPage1() {
        return page1;
    }

    public void setPage1(int page1) {
        this.page1 = page1;
    }

    public int getPage2() {
        return page2;
    }

    public void setPage2(int page2) {
        this.page2 = page2;
    }

    public int getPage3() {
        return page3;
    }

    public void setPage3(int page3) {
        this.page3 = page3;
    }

    public String getReportSaveName() {
        return reportSaveName;
    }

    public void setReportSaveName(String reportSaveName) {
        this.reportSaveName = reportSaveName;
    }

    public DataModel<SavedQuery> getQuerieList() {
        return querieList;
    }

    public void setQuerieList(DataModel<SavedQuery> querieList) {
        this.querieList = querieList;
    }

    public int getPage4() {
        setCurrentSavedQuery(null);
        setSelectRow(false);
        return page4;
    }

    public void setPage4(int page4) {
        this.page4 = page4;
    }

    public boolean isSelectRow() {
        return selectRow;
    }

    public void setSelectRow(boolean selectRow) {
        this.selectRow = selectRow;
    }

    public SavedQuery getCurrentSavedQuery() {
        return currentSavedQuery;
    }

    public void setCurrentSavedQuery(SavedQuery currentSavedQuery) {
        this.currentSavedQuery = currentSavedQuery;
    }

    public SelectItem[] getGateItem() {
        try {
            if (kind.equalsIgnoreCase("gateway")) {
                webServiceInfo.setServiceName("/getAllGateway");
                List<Gateway> gates = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(webServiceInfo.getServerUrl(), webServiceInfo.getServiceName()), new TypeReference<List<Gateway>>() {
                });

                gateItem = new SelectItem[gates.size()];
                for (int i = 0; i < gates.size(); i++) {
                    gateItem[i] = new SelectItem(gates.get(i).getName(), gates.get(i).getName());
                    gatewayObject.put(gates.get(i).getName(), gates.get(i));
                }
                return gateItem;
            }
            if (zone == null || "".equals(zone) || "0".equals(zone))
                return new SelectItem[0];
            webServiceInfo.setServiceName("/findGatewayByZone");

            List<Gateway> gates = new ObjectMapper().readValue(
                    new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(),
                            webServiceInfo.getServiceName(),
                            new ObjectMapper().writeValueAsString(me.getGeneralHelper().getZoneObject().get(zone))
                    ), new TypeReference<List<Gateway>>() {
            }
            );
            gateItem = new SelectItem[gates.size()];
            for (int i = 0; i < gates.size(); i++) {
                gateItem[i] = new SelectItem(gates.get(i).getName(), gates.get(i).getName());
                gatewayObject.put(gates.get(i).getName(), gates.get(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return gateItem;
    }

    public void setGateItem(SelectItem[] gateItem) {
        this.gateItem = gateItem;
    }

    public SelectItem[] getPdpItem() {
        try {
            if (kind.equalsIgnoreCase("pdp")) {
                webServiceInfo.setServiceName("/getAllPdp");
                List<PDP> pdps = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(webServiceInfo.getServerUrl(), webServiceInfo.getServiceName()), new TypeReference<List<PDP>>() {
                });

                pdpItem = new SelectItem[pdps.size()];
                for (int i = 0; i < pdps.size(); i++) {
                    pdpItem[i] = new SelectItem(pdps.get(i).getName(), pdps.get(i).getName());
                }
                return pdpItem;
            }
//            if (zone == null || "".equals(zone) || "0".equals(zone))
//                return new SelectItem[0];
            if (gateway == null || "".equals(gateway) || "0".equals(gateway))
                return new SelectItem[0];
            webServiceInfo.setServiceName("/findPdpByGatewayId");

            List<PDP> pdps = new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), webServiceInfo.getServiceName(), String.valueOf(gatewayObject.get(gateway).getId())), new TypeReference<List<PDP>>() {
            });
            pdpItem = new SelectItem[pdps.size()];
            for (int i = 0; i < pdps.size(); i++) {
                pdpItem[i] = new SelectItem(pdps.get(i).getName(), pdps.get(i).getName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pdpItem;
    }

    public void setPdpItem(SelectItem[] pdpItem) {
        this.pdpItem = pdpItem;
    }

    public void resetZone(ValueChangeEvent event) {
        if (event.getNewValue() == null || "".equals(event.getNewValue()) || "0".equals(event.getNewValue())) {
            gateway = "0";
            pdp = "0";
        }
    }

    public void resetGateway(ValueChangeEvent event) {
        if (event.getNewValue() == null || "".equals(event.getNewValue()) || "0".equals(event.getNewValue())) {
            pdp = "0";
        }
    }

    public boolean rendered(String s) {
        if (s.equalsIgnoreCase("ReportEntity")) {
            if (reportEntityList == null)
                return false;
            return reportEntityList.getRowCount() != 0;
        } else if (s.equalsIgnoreCase("Person")) {
            if (personList == null)
                return false;
            return personList.getRowCount() != 0;
        } else if (s.equalsIgnoreCase("Card")) {
            if (cardList == null)
                return false;
            return cardList.getRowCount() != 0;
        }
        return false;
    }
}
