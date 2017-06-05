package ir.university.toosi.tms.model.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.guest.entity.Guest;
import ir.university.toosi.guest.service.GuestServiceImpl;
import ir.university.toosi.tms.model.entity.Map;
import ir.university.toosi.tms.model.service.calendar.CalendarServiceImpl;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import java.io.UnsupportedEncodingException;


/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@ApplicationPath("restful")
@Path("/TMSService")
public class TMSService extends Application {
    @EJB
    private CalendarServiceImpl calendarService;
    @EJB
    private MapServiceImpl mapService;
    @EJB
    private GuestServiceImpl guestService;


    @POST
    @Path("/newGuest")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String newGuest(Guest guest) {
        try {
            return guestService.create(guest) == null ? "false" : "true";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "false";
    }

    @GET
    @Path("/todayList")
    @Produces(MediaType.TEXT_PLAIN)
    public String todayList() {
        try {
            return new ObjectMapper().writeValueAsString(guestService.todayGuest());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "false";
    }

    @POST
    @Path("/updateGuest")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String update(Guest guest) {
        return guestService.update(guest) == null ? "false" : "true";
    }


    @POST
    @Path("/newCalendar")
    @Produces(MediaType.TEXT_PLAIN)
    public String getCalendar() {
        return calendarService.newCalendar();
    }

    @POST
    @Path("/getEditCalendar")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String getEditCalendar(String id) {
        try {
            System.out.println("8888>>>>>>>>>>>>>>>>>>>>>" + id);
            String content = new String(calendarService.findById(id).getContent(), "utf-8");
            System.out.println(content);
            return content;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "NULL";

    }

    @POST
    @Path("/getEditCalendarByYear")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String getEditCalendarByYear(String year) {
        return calendarService.newCalendar().replace("2014", year);
    }


    @POST
    @Path("/alert")
    @Produces(MediaType.TEXT_XML)
    public String alert() {
        try {
            return mapService.alert();
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/initMap")
    @Produces(MediaType.TEXT_XML)
    public String initMap() {
        try {
            String s = mapService.init();
            System.out.println(s);
            return s;
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/viewMap")
    @Produces(MediaType.TEXT_PLAIN)
    public String viewMap() {
        try {
            return new String(((Map) mapService.getAllMap().get(0)).getContent(), "utf-8");
        } catch (Exception e) {
            return "NULL";
        }
    }

}
