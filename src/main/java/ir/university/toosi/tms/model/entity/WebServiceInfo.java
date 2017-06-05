package ir.university.toosi.tms.model.entity;

import ir.university.toosi.tms.util.Configuration;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi, Mostafa Rastgar
 * @version : 0.8
 */
public class WebServiceInfo {


    private final String serverUrl = "http://" + Configuration.getProperty("server.ip") + ":"+ Configuration.getProperty("kernel.port") +"/kernel/restful/TMSService";
//    private final String serverUrl = "http://192.168.40.65:8080/kernel/restful/TMSService";
    private String serviceName;

    public WebServiceInfo() {
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
