package test;

import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;

/**
 * Created by farzad on 10/24/2015.
 */
@ManagedBean
@ApplicationScoped
public class CounterView implements Serializable {
    private volatile int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void increment() {
        count++;
        EventBus eventBus = EventBusFactory.getDefault().eventBus();
        eventBus.publish("/counter", String.valueOf(count));
    }

    public void beginCount() throws IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext.getExternalContext().getRequestParameterMap().get("cid") == null || facesContext.getExternalContext().getRequestParameterMap().get("cid").isEmpty()) {
            facesContext.getExternalContext().redirect(facesContext.getExternalContext().getRequestContextPath() + "/testPush.xhtml");
        } else {
            facesContext.getExternalContext().redirect(facesContext.getExternalContext().getRequestContextPath() + "/testPush.xhtml" + "?cid=" + facesContext.getExternalContext().getRequestParameterMap().get("cid"));
        }
    }
}

