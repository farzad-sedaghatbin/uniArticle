package ir.university.toosi.tms.model.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.tms.model.dao.MapDAOImpl;
import ir.university.toosi.tms.model.entity.EventLogType;
import ir.university.toosi.tms.model.entity.Map;
import ir.university.toosi.tms.model.entity.TrafficLog;
import ir.university.toosi.tms.model.entity.objectValue.alert.Element;
import ir.university.toosi.tms.model.entity.objectValue.alert.Number;
import ir.university.toosi.tms.model.entity.objectValue.init.*;
import ir.university.toosi.tms.model.entity.zone.Gateway;
import ir.university.toosi.tms.model.service.zone.GatewayServiceImpl;
import ir.university.toosi.tms.util.EventLogManager;
import ir.university.toosi.tms.util.JaxbUtil;
import ir.university.toosi.wtms.web.util.CalendarUtil;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Stateless
@LocalBean

public class MapServiceImpl<T extends Map> {

    @EJB
    private MapDAOImpl mapDAO;
    @EJB
    private TrafficLogServiceImpl trafficLogService;
    @EJB
    private GatewayServiceImpl gatewayService;

    @EJB
    private EventLogServiceImpl eventLogService;

    public T findById(String id) {
        try {
            return (T) mapDAO.findById(id);
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> findByCode(Map map) {
        try {
            EventLogManager.eventLog(eventLogService, null, Map.class.getSimpleName(), EventLogType.SEARCH, map.getEffectorUser());
            return (List<T>) mapDAO.findByCode(map.getCode());
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> getAllMap() {
        try {
            return (List<T>) mapDAO.findAll("Map.list", true);
        } catch (Exception e) {
            return null;
        }
    }

    public String deleteMap(T entity) {
        try {
            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), Map.class.getSimpleName(), EventLogType.DELETE, entity.getEffectorUser());
            mapDAO.delete(entity);
            return "operation.occurred";
        } catch (Exception e) {
            return "FALSE";
        }
    }


    public T createMap(T entity) {
        try {
            entity.setCode("MAP"+mapDAO.maximumId("Map.maximum", true));
            T t = (T) mapDAO.create(entity);
            EventLogManager.eventLog(eventLogService, String.valueOf(t.getId()), Map.class.getSimpleName(), EventLogType.ADD, entity.getEffectorUser());
            return t;
        } catch (Exception e) {
            return null;
        }
    }


    public boolean editMap(T entity) {
        try {
            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), Map.class.getSimpleName(), EventLogType.EDIT, entity.getEffectorUser());
            mapDAO.update(entity);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String alert() {
        long time = Long.valueOf(CalendarUtil.getTimeWithoutDot(new Date(), new Locale("fa")));
        List<TrafficLog> logs = trafficLogService.findInDuration(time - 60, time, CalendarUtil.getPersianDateWithoutSlash(new Locale("fa")));
        Element element = new Element();
        List<Number> numbers = new ArrayList<>();
        Number number = new Number();
        for (TrafficLog log : logs) {
            number = new Number();
            number.setAlertdatetime(CalendarUtil.getPersianDateWithSlash(new Locale("fa")));
            number.setAlertdiffusecolor("0 1 0");
            number.setAlertlastname(log.getPerson().getLastName());
            number.setAlertname(log.getPerson().getName());
            number.setAlertpersonnum(log.getPerson().getPersonnelNo());
            number.setNodeId(String.valueOf(log.getGateway().getId()));
            number.setAlertnotiftitle(log.getPerson().getName());
            number.setAlertnotifdescription(log.getPerson().getLastName() + " " + log.getPerson().getPersonnelNo());
//            number.setAlertimageurl(Base64.encodeBase64String(log.getPerson().getPicture()));
            numbers.add(number);
        }
        element.getNumber().addAll(numbers);
        String s = JaxbUtil.ObjectToXml(element, "ir.university.toosi.tms.model.entity.objectValue.alert");
        s=s.replaceAll("\"", "\'");
        s = s.substring(s.indexOf(">") + 1).replace("<element>", "<element xmlns=\"http://www.w3.org/1999/xhtml\" status=\"true\" operation=\"Get_Last\">");
//        String s=" <element xmlns=\"http://www.w3.org/1999/xhtml\" status=\"true\" operation=\"Get_Last\"><number node_id=\'7\' alertname=\'\' alertlastname=\'? 2\' alertpersonnum=\'123456\' alertdatetime=\'1393/08/05\' alertdiffusecolor=\'0 1 0\' alertnotiftitle=\'\' alertnotifdescription=\'? 2 123456\'/></element>";

        return s;
    }

    public String init() {
        List<Gateway> gateways = gatewayService.getAllGateway();
        Gate gate = new Gate();
        List<Aa> aaList = new ArrayList<>();
        Aa aa;
        Bb bb;
        Cc cc;
        Dd dd;
        Ee ee;
        Ff ff;
        Gg gg;
        bb = new Bb();
        bb.setNodeName("DefaultShape");
        bb.setType("Default");
        cc = new Cc();
        cc.setNodeName("Transform");
        cc.setDEF("foo");
        cc.setRotation("Auto");
        cc.setScale(".10 .6  .10");
        cc.setTranslation("Auto");
        dd = new Dd();
        dd.setNodeName("Shape");
        dd.setIsPickable("true");
        dd.setDEF("float");
        ee = new Ee();
        ee.setNodeName("Appearance");
        gg = new Gg();
        gg.setNodeName("Material");
        gg.setDiffuseColor("0 1 0");
        gg.setSpecularColor(".5 .5 .5");
        ff = new Ff();
        ff.setNodeName("Box");
        ff.setSize("20 20 20");
        dd.setFf(ff);
        ee.setGg(gg);
        dd.setEe(ee);
        cc.setDd(dd);
        bb.setCc(cc);
        for (Gateway gateway : gateways) {
            aa = new Aa();
            aa.setId(String.valueOf(gateway.getId()));
            aa.setName(gateway.getName());
            aa.setColor("#fff");
            aa.setAlertType("1");
            aa.setBb(bb);
            aaList.add(aa);
        }
        gate.getAa().addAll(aaList);

        InputStream inputStream = MapServiceImpl.class.getClassLoader().getResourceAsStream("textinit.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        StringBuilder out = new StringBuilder();
        try {
            while ((line = reader.readLine()) != null) {
                out.append(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String txt = JaxbUtil.ObjectToXml(gate, "ir.university.toosi.tms.model.entity.objectValue.init");

        txt = txt.replaceAll("Gate", "nodes");
        txt = txt.replaceAll("aa", "number");
        txt = txt.replaceAll("bb", "number");
        txt = txt.replaceAll("cc", "number");
        txt = txt.replaceAll("dd", "number");
        txt = txt.replaceAll("ee", "number");
        txt = txt.replaceAll("ff", "number");
        txt = txt.replaceAll("gg", "number");
        txt = txt.replaceAll("NodeName", "nodename");
        txt = txt.replaceAll("AlertType", "alerttype");
        txt = txt.replaceAll("Color", "color");
        txt = txt.replaceAll("DEF", "def");
        txt = out + txt.substring(txt.indexOf(">") + 1) + "\n" +
                "        </number>\n" +
                "    </collections>\n" +
                "</element>";
        System.out.println(txt);
        return txt;

    }


}