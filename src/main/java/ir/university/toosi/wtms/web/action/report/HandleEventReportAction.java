package ir.university.toosi.wtms.web.action.report;

import ir.university.toosi.tms.model.entity.Role;
import ir.university.toosi.wtms.web.action.UserManagementAction;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import org.primefaces.model.SortOrder;

import javax.enterprise.context.SessionScoped;
import javax.faces.model.DataModel;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


/**
 * Created with IntelliJ IDEA.
 * User: behzad
 * Date: 1/2/14
 * Time: 9:05 PM
 * To change this template use File | Settings | File Templates.
 */

@Named(value = "handleEvent" +
        "ReportAction")
@SessionScoped
public class HandleEventReportAction implements Serializable{




        @Inject
        private UserManagementAction me;
        private String type;
        private  String kind;

        private DataModel<Role> roleList = null;
        private String editable = "false";
        private boolean roleEnabled;
        private String description;
        private String descText;
        private Role currentRole = null;
        private String currentPage;
        private int page = 1;
        private SortOrder roleDescriptionOrder = SortOrder.UNSORTED;
        private String roleDescriptionFilter;
        private boolean selected;
        private boolean selectAll = false;
        private Set<Role> selectedRoles = new HashSet<>();
        private JasperPrint jasperPrint;

    public void init() throws JRException{


        ////

//        JRBeanCollectionDataSource beanCollectionDataSource=new JRBeanCollectionDataSource(cards);
//        ClassLoader classLoader = getClass().getClassLoader();
//
//        InputStream url = null;
//        InputStream jasperIS = getClass().getResourceAsStream("/report/reportInvestment.jasper");
//
//        url = classLoader.getResourceAsStream("/reports/report2.jasper");
//        String  reportPath=  FacesContext.getCurrentInstance().getExternalContext().getRealPath(jasperIS.toString());
//
//                    JasperPrint jasperPrint= JasperFillManager.fillReport(reportPath, new HashedMap(), beanCollectionDataSource);
    }

    public String begin() {

            return "list-card-report";
        }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }
}
