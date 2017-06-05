package ir.university.toosi.tms.model.service;

import ir.university.toosi.tms.model.entity.GatewaySpecialState;
import ir.university.toosi.wtms.web.util.CalendarUtil;

import javax.annotation.Resource;
import javax.ejb.*;
import java.util.List;
import java.util.Locale;

/**
 * @author : Hamed Hatami , Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */
@Stateless
@LocalBean

public class GatewaySpecialStateScheduler {

    @Resource
    private TimerService timerService;

    @EJB
    private GatewaySpecialStateServiceImpl gatewaySpecialStateService;

    public void stopService() {
        for (Timer timer : timerService.getTimers()) {
            timer.cancel();
        }
    }

    public void startService() {

        List<GatewaySpecialState> gatewaySpecialStates = gatewaySpecialStateService.getAllGatewaySpecialState();

        for (GatewaySpecialState gatewaySpecialState : gatewaySpecialStates) {
            String time = gatewaySpecialState.getTime();
            String date = gatewaySpecialState.getDate();
            String gDate = CalendarUtil.getDate(CalendarUtil.getDate(date, new Locale("fa")), new Locale("en"));
            String[] dateStrings = gDate.split(" ")[0].split("/");
            String day = dateStrings[0];
            String month = dateStrings[1];
            String year = dateStrings[2];
            String[] strings = time.split(":");
            String hour = strings[0];
            String min = strings[1];
            String sec = strings[2];
            ScheduleExpression expression = new ScheduleExpression().second(sec).minute(min).hour(hour).year(year).month(month).dayOfMonth(day);
            timerService.createCalendarTimer(expression, new TimerConfig("here", false));
        }
    }

    @Timeout
    public void runService(Timer timer) {
        //TODO

        System.out.println("IN TIMERRRRRRRRRR ************* : " + timer.getInfo());
    }
}