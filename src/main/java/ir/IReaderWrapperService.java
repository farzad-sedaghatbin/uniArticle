package ir;

import ir.university.toosi.tms.readerwrapper.AccesEventHolder;
import ir.university.toosi.tms.readerwrapper.PersonHolder;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * Created by Rahman on 10/21/15.
 */
@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface IReaderWrapperService {

    @WebMethod
    public  void forceOpenDoor(int terminalId);
    @WebMethod
    public  void lockDoor(int terminalId);

    @WebMethod
    public  void unLockDoor(int terminalId);

    @WebMethod
    public  void lockTerminal(int terminalId);

    @WebMethod
    public  void unLockTerminal(int terminalId);

    @WebMethod
    public void getUserList(int terminalId);
    @WebMethod
    public void GetAccessEventData(int terminalId);

    @WebMethod
    public void setUserList(int terminalId, PersonHolder personHolder);
    @WebMethod
    public void setAccessEventList(int terminalId, AccesEventHolder accesEventHolder);

    @WebMethod
    public void addOnGetAccessEventData(int terminalId, ir.university.toosi.tms.readerwrapper.AccessEventData accessEventData);

    @WebMethod
    public boolean addOnGetOnlineVerifyAccessControl(int terminalId, ir.university.toosi.tms.readerwrapper.AccessEventData accessEventData) ;

    @WebMethod
    public void addUserInfo(int terminal, PersonHolder personHolder);
    @WebMethod
    public void sendParking(String pelak, byte[] pic);
}
