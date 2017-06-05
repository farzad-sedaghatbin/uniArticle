package ir.university.toosi.wtms.web.action.report;

import ir.university.toosi.wtms.web.action.UserManagementAction;
import ir.university.toosi.wtms.web.action.operation.HandleOperationAction;
import ir.university.toosi.wtms.web.action.workgroup.HandleWorkGroupAction;
import ir.university.toosi.tms.model.entity.personnel.Card;
import ir.university.toosi.tms.model.entity.Role;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.collections.map.HashedMap;
import org.primefaces.model.SortOrder;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Created with IntelliJ IDEA.
 * User: behzad
 * Date: 1/2/14
 * Time: 9:05 PM
 * To change this template use File | Settings | File Templates.
 */

@Named(value = "handlePersonReportAction")
@SessionScoped
public class HandlePersonReportAction implements Serializable{




        @Inject
        private UserManagementAction me;
        @Inject
        private HandleOperationAction handleOperationAction;
        @Inject
        private HandleWorkGroupAction handleWorkGroupAction;
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
        Card card=new Card();
        List<Card> cards=new ArrayList<>();

        for (int i=0;i<3;i++){
            card.setName("a"+1);
            card.setId(i);
            cards.add(card);
        }
        JRBeanCollectionDataSource beanCollectionDataSource=new JRBeanCollectionDataSource(cards);
        String  reportPath=  FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/report2.jasper");

                    JasperPrint jasperPrint= JasperFillManager.fillReport(reportPath, new HashedMap(), beanCollectionDataSource);
    }

    public String begin() {

            return "list-person-report";
        }

    public void creatReport(ValueChangeEvent event) throws JRException, IOException{
        init();
        HttpServletResponse httpServletResponse=(HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        httpServletResponse.addHeader("Content-disposition", "attachment; filename=report2.pdf");
        ServletOutputStream servletOutputStream=httpServletResponse.getOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
        FacesContext.getCurrentInstance().responseComplete();
    }



}
