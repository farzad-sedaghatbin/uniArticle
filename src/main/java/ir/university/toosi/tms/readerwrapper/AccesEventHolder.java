package ir.university.toosi.tms.readerwrapper;

import java.io.Serializable;

/**
 * Created by Rahman on 11/2/15.
 */
public class AccesEventHolder implements Serializable {

    AccessEventData[] accessEventDatas;

    public AccesEventHolder() {
    }

    public AccessEventData[] getAccessEventDatas() {
        return accessEventDatas;
    }

    public void setAccessEventDatas(AccessEventData[] accessEventDatas) {
        this.accessEventDatas = accessEventDatas;
    }
}
