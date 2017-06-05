package ir.university.toosi.tms.util;

import ir.university.toosi.tms.model.entity.EventLog;
import ir.university.toosi.tms.model.entity.EventLogType;
import ir.university.toosi.tms.model.service.EventLogServiceImpl;
import ir.university.toosi.wtms.web.util.CalendarUtil;

import java.util.Date;
import java.util.Locale;

/**
 * @author : Hamed Hatami , Javad Sarhadi , Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */
public class EventLogManager {

    public synchronized static void eventLog(EventLogServiceImpl eventLogService, String objectId, String tableName, EventLogType operation, String userName) {

        eventLogService.createEventLog(new EventLog(operation, objectId, tableName, userName, LangUtil.getEnglishNumber(CalendarUtil.getDateWithoutSlash(new Date(), new Locale("fa"), "yyyyMMdd")), LangUtil.getEnglishNumber(CalendarUtil.getTime(new Date(), new Locale("fa")))));
    }
}
