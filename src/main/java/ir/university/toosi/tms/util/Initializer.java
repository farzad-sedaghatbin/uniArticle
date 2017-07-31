package ir.university.toosi.tms.util;

import ir.university.toosi.tms.model.service.SystemConfigurationServiceImpl;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.net.Socket;
import java.util.Hashtable;
import java.util.List;

@Singleton
@Startup
public class Initializer {

    @EJB
    private SystemConfigurationServiceImpl systemConfigurationService;
//    @Inject
//    private GeneralHelper generalHelper;




    @PostConstruct
    public void initial() {

        SystemParameterManager.fillSystemConfiguration(systemConfigurationService);

    }

}
