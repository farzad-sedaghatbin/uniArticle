package ir.university.toosi.tms.model.service;

import ir.university.toosi.tms.model.entity.TrafficLog;
import ir.university.toosi.tms.model.entity.personnel.Card;
import ir.university.toosi.tms.model.entity.personnel.Person;
import ir.university.toosi.tms.model.entity.zone.Gateway;
import ir.university.toosi.tms.model.entity.zone.PDP;
import ir.university.toosi.tms.model.entity.zone.Virdi;
import ir.university.toosi.tms.model.service.personnel.CardServiceImpl;
import ir.university.toosi.tms.model.service.personnel.PersonServiceImpl;
import ir.university.toosi.tms.model.service.zone.PDPServiceImpl;
import ir.university.toosi.tms.util.Initializer;
import ir.university.toosi.wtms.web.util.CalendarUtil;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.Date;
import java.util.Locale;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Stateless
@LocalBean
public class CrossingServiceImpl {

    @EJB
    private PhotoServiceImpl photoService;
    @EJB
    private PDPServiceImpl pdpService;
    @EJB
    private PersonServiceImpl personService;
    @EJB
    private CardServiceImpl cardService;
    @EJB
    private AuthenticateServiceImpl authenticateService;
    @EJB
    private TrafficLogServiceImpl trafficLogService;


    public TrafficLog authorize(String pdpIP, String personNo, String cardType, long start) throws Exception {
        long end = 0;
        PDP pdp = pdpService.findByIp(pdpIP);
        Object[] person = Initializer.persons.get(personNo);

        if (person == null || pdp == null)
            return null;
        String photos = null;
        Card card = null;
        boolean finger = false;
        if (cardType.equalsIgnoreCase("0")) {
            finger = true;
            photos = "/" + pdp.getId() + "finger" + new Date().getTime();
        } else{
            card = cardService.findByCode(cardType);
            if (card != null) {
                photos = "/" + pdp.getId() + card.getCode() + new Date().getTime();
            } else {
                photos = "/" + pdp.getId() + "noAuth" + new Date().getTime();
            }

        }
        TrafficLog trafficLog = new TrafficLog();
        if (authenticateService.authenticate(pdp.getGateway(), person, !pdp.isEntrance(), card, finger)) {
            trafficLog.setStatus("OK");
            trafficLog.setValid(true);
        } else {
            trafficLog.setStatus("fail");
            trafficLog.setValid(false);
        }
        if (pdp.isEntrance() != true)
            trafficLog.setExit(true);
        else
            trafficLog.setExit(false);
        trafficLog.setPictures(photos);
        trafficLog.setFinger(finger);
        trafficLog.setCard(card);
        trafficLog.setOffline(false);
        trafficLog.setGateway(pdp.getGateway());
        trafficLog.setPdp(pdp);
        trafficLog.setZone(pdp.getGateway().getZone());
        trafficLog.setTraffic_date(CalendarUtil.getPersianDateWithoutSlash(new Locale("fa")));
        trafficLog.setTraffic_time(CalendarUtil.getTime(new Date(), new Locale("fa")));
        return trafficLog;

    }
    public TrafficLog authorize(Virdi virdi,String personNo, String cardType) throws Exception {
        long end = 0;
        Object[] person = Initializer.persons.get(personNo);

        if (person == null || virdi == null)
            return null;
        String photos = null;
        Card card = null;
        boolean finger = false;
        if (cardType.equalsIgnoreCase("0")) {
            finger = true;
            photos = "/" + virdi.getId() + "finger" + new Date().getTime();
        } else{
            card = cardService.findByCode(cardType);
            if (card != null) {
                photos = "/" + virdi.getId() + card.getCode() + new Date().getTime();
            } else {
                photos = "/" + virdi.getId() + "noAuth" + new Date().getTime();
            }

        }
        TrafficLog trafficLog = new TrafficLog();
        if (authenticateService.authenticate(virdi.getGateway(), person, !virdi.isEntrance(), card, finger)) {
            trafficLog.setStatus("OK");
            trafficLog.setValid(true);
        } else {
            trafficLog.setStatus("fail");
            trafficLog.setValid(false);
        }
        if (virdi.isEntrance() != true)
            trafficLog.setExit(true);
        else
            trafficLog.setExit(false);
        trafficLog.setPictures(photos);
        trafficLog.setFinger(finger);
        trafficLog.setCard(card);
        trafficLog.setOffline(false);
        trafficLog.setGateway(virdi.getGateway());
        trafficLog.setVirdi(virdi);
        trafficLog.setZone(virdi.getGateway().getZone());
        trafficLog.setTraffic_date(CalendarUtil.getPersianDateWithoutSlash(new Locale("fa")));
        trafficLog.setTraffic_time(CalendarUtil.getTime(new Date(), new Locale("fa")));
        return trafficLog;

    }

    private boolean authenticate(Gateway gateway, Person person) {
        return true;
    }


}
