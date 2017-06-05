package ir.university.toosi.wtms.web.action.monitoring;

import org.primefaces.push.annotation.OnMessage;
import org.primefaces.push.annotation.PushEndpoint;
import org.primefaces.push.impl.JSONEncoder;

/**
 * Created by farzad on 10/24/2015.
 */
@PushEndpoint("/counter")
public class MonitorResource {
    @OnMessage
    public String onMessage(String count) {
        return count;
    }
}
