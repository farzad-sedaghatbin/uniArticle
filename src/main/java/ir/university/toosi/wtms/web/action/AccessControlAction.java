package ir.university.toosi.wtms.web.action;


import ir.university.toosi.tms.model.entity.WorkGroup;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Set;

/**
 * @author : Hamed Hatami , Arsham Sedaghatbin, Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Named(value = "accessControlAction")
@SessionScoped
public class AccessControlAction implements Serializable {

    private Set<WorkGroup> workGroup = null;



    public Set<WorkGroup> getWorkGroup() {
        return workGroup;
    }

    public void setWorkGroup(Set<WorkGroup> workGroup) {
        this.workGroup = workGroup;
    }
}
