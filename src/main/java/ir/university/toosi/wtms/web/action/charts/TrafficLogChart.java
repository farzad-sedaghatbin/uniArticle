package ir.university.toosi.wtms.web.action.charts;

import ir.university.toosi.tms.model.entity.zone.Gateway;
import ir.university.toosi.tms.model.service.TrafficLogServiceImpl;
import ir.university.toosi.tms.model.service.personnel.PersonServiceImpl;
import ir.university.toosi.tms.model.service.zone.GatewayServiceImpl;
import ir.university.toosi.wtms.web.action.UserManagementAction;
import org.primefaces.model.chart.*;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

/**
 * Created by O_Javaheri on 10/19/2015.
 */
@Named(value = "handleTrafficLogChartAction")
@SessionScoped
public class TrafficLogChart implements Serializable {

    @Inject
    private UserManagementAction me;

    @EJB
    private TrafficLogServiceImpl trafficLogService;
    @EJB
    private GatewayServiceImpl gatewayService;
    @EJB
    private PersonServiceImpl personService;

    private LineChartModel lineModel;
    private BarChartModel barChartModel;
    private String fromHour;
    private String fromMin;
    private String fromSec;
    private String toHour;
    private String toMin;
    private String toSec;
    private String fromDate;
    private String toDate;

    public void begin() {
        refineTimes();
        List<Object[]> activeTraffics = trafficLogService.searchForChart("000000", "235959", "13700101", "15000101", true);
        List<Object[]> deActiveTraffics = trafficLogService.searchForChart("000000", "235959", "13700101", "15000101", false);
        prepareChart(activeTraffics, deActiveTraffics);
        me.redirect("/charts/traffic-log-chart.xhtml");
    }

    public void search() {
        refineTimes();
        List<Object[]> activeTraffics = trafficLogService.searchForChart(fromHour + fromMin + fromSec, toHour + toMin + toSec, fromDate, toDate, true);
        List<Object[]> deActiveTraffics = trafficLogService.searchForChart(fromHour + fromMin + fromSec, toHour + toMin + toSec, fromDate, toDate, false);
        prepareChart(activeTraffics, deActiveTraffics);
    }

    private void prepareChart(List<Object[]> activeTraffics, List<Object[]> deActiveTraffics) {
        lineModel = new LineChartModel();
        barChartModel = new BarChartModel();
        ChartSeries barChartSeriesForActive = new ChartSeries();
        ChartSeries barChartSeriesForDeActive = new ChartSeries();
        LineChartSeries lineChartSeriesForActive = new LineChartSeries();
        LineChartSeries lineChartSeriesForDeActive = new LineChartSeries();
        barChartSeriesForActive.setLabel("Active Logs");
        lineChartSeriesForActive.setLabel("Active Logs");
        barChartSeriesForDeActive.setLabel("DeActive Logs");
        lineChartSeriesForDeActive.setLabel("DeActive Logs");
        for (Object[] param : activeTraffics) {
            Gateway gateway = gatewayService.findById((Long) param[0]);
            lineChartSeriesForActive.set(gateway.getName(), (Number) param[1]);
            barChartSeriesForActive.set(gateway.getName(), (Number) param[1]);
        }

        for (Object[] param : deActiveTraffics) {
            Gateway gateway = gatewayService.findById((Long) param[0]);
            lineChartSeriesForDeActive.set(gateway.getName(), (Number) param[1]);
            barChartSeriesForDeActive.set(gateway.getName(), (Number) param[1]);
        }

        lineModel.addSeries(lineChartSeriesForActive);
        lineModel.addSeries(lineChartSeriesForDeActive);
        barChartModel.addSeries(barChartSeriesForActive);
        barChartModel.addSeries(barChartSeriesForDeActive);

        lineModel.setTitle("Linear Chart");
        barChartModel.setTitle("Bar Chart");
        lineModel.setLegendPosition("se");
        barChartModel.setLegendPosition("ne");
        lineModel.setAnimate(true);
        barChartModel.setAnimate(true);
        lineModel.setShowPointLabels(true);
        lineModel.getAxes().put(AxisType.X, new CategoryAxis("Gateways"));
        Axis yAxis = lineModel.getAxis(AxisType.Y);
        yAxis.setLabel("Persons");
        yAxis.setMin(0);
        yAxis.setMax(personService.countOfAll());

        barChartModel.getAxes().put(AxisType.X, new CategoryAxis("Gateways"));
        yAxis = barChartModel.getAxis(AxisType.Y);
        yAxis.setLabel("Persons");
        yAxis.setMin(0);
        yAxis.setMax(personService.countOfAll());
    }

    private void refineTimes() {
        if (fromHour == null || fromHour.equals("")) {
            fromHour = "00";
        }
        if (fromHour.length() == 1) {
            fromHour = "0" + fromHour;
        }
        if (fromMin == null || fromMin.equals("")) {
            fromMin = "00";
        }
        if (fromMin.length() == 1) {
            fromMin = "0" + fromMin;
        }
        if (fromSec == null || fromSec.equals("")) {
            fromSec = "00";
        }
        if (fromSec.length() == 1) {
            fromSec = "0" + fromSec;
        }

        if (toHour == null || toHour.equals("")) {
            toHour = "00";
        }
        if (toHour.length() == 1) {
            toHour = "0" + toHour;
        }

        if (toMin == null || toMin.equals("")) {
            toMin = "00";
        }
        if (toMin.length() == 1) {
            toMin = "0" + toMin;
        }

        if (toSec == null || toSec.equals("")) {
            toSec = "00";
        }
        if (toSec.length() == 1) {
            toSec = "0" + toSec;
        }
        if (fromDate == null || fromDate.equals("")) {
            fromDate = "13700101";
        }

        if (toDate == null || toDate.equals("")) {
            toDate = "15000101";
        }

    }

    public LineChartModel getLineModel() {
        return lineModel;
    }

    public void setLineModel(LineChartModel lineModel) {
        this.lineModel = lineModel;
    }

    public String getFromHour() {
        return fromHour;
    }

    public void setFromHour(String fromHour) {
        this.fromHour = fromHour;
    }

    public String getFromMin() {
        return fromMin;
    }

    public void setFromMin(String fromMin) {
        this.fromMin = fromMin;
    }

    public String getFromSec() {
        return fromSec;
    }

    public void setFromSec(String fromSec) {
        this.fromSec = fromSec;
    }

    public String getToHour() {
        return toHour;
    }

    public void setToHour(String toHour) {
        this.toHour = toHour;
    }

    public String getToMin() {
        return toMin;
    }

    public void setToMin(String toMin) {
        this.toMin = toMin;
    }

    public String getToSec() {
        return toSec;
    }

    public void setToSec(String toSec) {
        this.toSec = toSec;
    }

    public BarChartModel getBarChartModel() {
        return barChartModel;
    }

    public void setBarChartModel(BarChartModel barChartModel) {
        this.barChartModel = barChartModel;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }
}
