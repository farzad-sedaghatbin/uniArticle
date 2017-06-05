package ir.university.toosi.tms.model.service;

import javax.jws.WebService;

/**
 * Created by farzad on 10/21/2015.
 */
@WebService
public interface EJB3RemoteInterface {
    public boolean cardVerify();
    public boolean fingerVerify();
}
