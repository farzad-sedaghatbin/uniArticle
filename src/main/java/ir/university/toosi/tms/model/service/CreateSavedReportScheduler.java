package ir.university.toosi.tms.model.service;


import ir.university.toosi.tms.model.entity.BaseEntity;
import ir.university.toosi.tms.model.entity.ReportEntity;
import ir.university.toosi.tms.model.entity.SavedQuery;
import ir.university.toosi.tms.util.Configuration;
import ir.university.toosi.tms.util.Initializer;
import ir.university.toosi.tms.util.ReportUtils;

import javax.annotation.Resource;
import javax.ejb.*;
import java.util.ArrayList;
import java.util.List;


@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class CreateSavedReportScheduler {

    @Resource
    private TimerService timerService;

    @EJB
    private SavedQueryServiceImpl savedQueryService;

    @EJB
    private TrafficLogServiceImpl trafficLogService;

    @EJB
    private LanguageServiceImpl languageService;

    public void stopService() {
        for (Timer timer : timerService.getTimers()) {
            if (timer.getInfo().equals(Configuration.getProperty("createSavedReport_Scheduler_Name"))) {
                timer.cancel();
            }
        }
    }

    public void startService() {

        ScheduleExpression expression = new ScheduleExpression().second(0).minute(0).hour(1).dayOfWeek("*");
        timerService.createCalendarTimer(expression, new TimerConfig(Configuration.getProperty("createSavedReport_Scheduler_Name"), false));
    }

    @Timeout
    public void runService(Timer timer) {
        try {

            List<SavedQuery> savedQueries = savedQueryService.findScheduled();
            for (SavedQuery savedQuery : savedQueries) {

                List<BaseEntity> innerReportEntity = new ArrayList<>();
                List<ReportEntity> reportEntities = trafficLogService.queryView(savedQuery.getQuery());
                innerReportEntity.addAll(reportEntities);
                if (savedQuery.getType().equalsIgnoreCase("staticEnteredPersonGateway") || savedQuery.getType().equalsIgnoreCase("staticUnsuccessfullEnteredGateway"))
                    export(savedQuery.getExportType(), "staticGateway", innerReportEntity, addTitles(savedQuery.getType()));
                else
                    export(savedQuery.getExportType(), savedQuery.getType(), innerReportEntity, addTitles(savedQuery.getType()));

                savedQuery.setDeleted("1");
                savedQueryService.editSavedQuery(savedQuery);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void export(String type, String kind, List<BaseEntity> innerReportEntity, List<String> titles) {
        //TODO : save file  FARZAD
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

    private List<String> addTitles(String kind) {
        List<String> titles = new ArrayList<>();
        if (kind.equalsIgnoreCase("person")) {
            titles.add(getValue("name"));
            titles.add(getValue("organ"));
            titles.add(getValue("lastname"));
            titles.add(getValue("gateway"));
            titles.add(getValue("date"));
            titles.add(getValue("time"));
            titles.add(getValue("entry_type"));
            titles.add(getValue("success"));
        } else if (kind.equalsIgnoreCase("staticEnteredPersonGateway")) {
            titles = new ArrayList<>();
            titles.add(getValue("name"));
            titles.add(getValue("lastname"));
            titles.add(getValue("organ"));
            titles.add(getValue("gateway"));
            titles.add(getValue("date"));
            titles.add(getValue("time"));
            titles.add(getValue("entry_type"));
            titles.add(getValue("success"));
        } else if (kind.equalsIgnoreCase("staticUnsuccessfullEnteredGateway")) {
            titles = new ArrayList<>();
            titles.add(getValue("name"));
            titles.add(getValue("lastname"));
            titles.add(getValue("gateway"));
        }
        return titles;
    }

    private String getValue(String key) {
        if (Initializer.lang == null) {
            //TODO : systemConfiguration
            Initializer.lang = languageService.loadLanguage("fa");
        }
        if (Initializer.lang.containsKey(key))
            return Initializer.lang.get(key).getTitle();
        return key + "_NOT_DEF";

    }

}