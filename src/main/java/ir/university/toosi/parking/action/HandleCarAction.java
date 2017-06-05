package ir.university.toosi.parking.action;


import ir.university.toosi.parking.entity.Car;
import ir.university.toosi.parking.service.CarServiceImpl;
import ir.university.toosi.tms.model.entity.BLookup;
import ir.university.toosi.tms.model.entity.personnel.Person;
import ir.university.toosi.wtms.web.action.UserManagementAction;
import ir.university.toosi.wtms.web.action.person.HandlePersonAction;
import ir.university.toosi.wtms.web.action.role.HandleRoleAction;
import org.primefaces.model.SortOrder;

import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author : Hamed Hatami , Arsham Sedaghatbin, Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Named(value = "handleCarAction")
@SessionScoped
public class HandleCarAction implements Serializable {

    @Inject
    private UserManagementAction me;
    @Inject
    private HandlePersonAction handlePersonAction;
    @EJB
    private CarServiceImpl carService;
    private List<Car> carList = null;
    private boolean editable;
    private boolean disableFields = true;
    private String carNumber;
    private Car currentCar = null;
    private String currentPage;
    private int page = 1;
    private boolean selected;
    private Set<Car> selectedCars = new HashSet<>();
    private boolean selectRow = false;
    private String carNameFilter;
    private String carLocationFilter;


    public void begin() {
//        todo:solve this comment
//        me.setActiveMenu(MenuType.USER);
        System.out.println(me.getDirection());
        refresh();
        me.redirect("/parking/car.xhtml");
    }

//    public void selectCars(ValueChangeEvent event) {
//        currentCar = carList.getRowData();
//        boolean temp = (Boolean) event.getNewValue();
//        if (temp) {
//            currentCar.setSelected(true);
//            selectedCars.add(currentCar);
//        } else {
//            currentCar.setSelected(false);
//            selectedCars.remove(currentCar);
//        }
//    }

    public void assignPerson(Person person) {
        handlePersonAction.setCurrentPerson(person);
        carList = carService.getAllCar();

    }

    public List<Car> getSelectionGrid() {
        List<Car> cars = new ArrayList<>();
        refresh();
        return carList;
    }

    private void refresh() {
        init();
        List<Car> cars = carService.getAllCar();
//        todo:must be uncommented
//            for (Car car : cars) {
//                car.getLocation().setTitleText(car.getName());
////                todo:the commented line is correct
////                car.getLocation().setTitleText(me.getValue(car.getLocation().getCode()));
//            }
        carList = new ArrayList<>(cars);
    }

    public void add() {
        init();
        setEditable(false);
        setDisableFields(false);
    }


    public void doDelete() {

//        currentCar.setEffectorUser(me.getUsername());
        carService.deleteCar(currentCar);
        refresh();
        me.addInfoMessage("operation.occurred");
        me.redirect("/parking/car.xhtml");
    }

    public void init() {
        page = 1;
        currentCar = null;
        carNameFilter = "";
        carLocationFilter = "";
        carNumber = "";
        setSelectRow(false);
        setDisableFields(true);
    }

    public void edit() {
        setEditable(true);
        setDisableFields(false);
        carNumber = currentCar.getNumber();
    }

    public void view() {
        disableFields = true;
        carNumber = currentCar.getNumber();
    }

    public void saveOrUpdate() {
        if (!editable) {
            doAdd();
        } else {
            doEdit();
        }
    }

    public void resetEditable() {
        setEditable(false);
    }

    private void doEdit() {
        currentCar.setNumber(carNumber);

        boolean condition = carService.editCar(currentCar);
        if (condition) {
            refresh();
            me.addInfoMessage("operation.occurred");
            me.redirect("/parking/car.xhtml");
        } else {
            me.addInfoMessage("operation.not.occurred");
            return;
        }

    }

    private void doAdd() {
        System.out.println(me.getDirection());
        Car newCar = new Car();
        newCar.setDeleted("0");
        newCar.setStatus("c");
//        newCar.setEffectorUser(me.getUsername());
        newCar.setNumber(carNumber);
        List<Car> cars = carService.findByNumber(carNumber);
        boolean condition = cars != null && cars.size() > 0;
        if (condition) {
            me.addInfoMessage("car.exist");
            return;
        }

        Car insertedCar = null;
        insertedCar = carService.createCar(newCar);
        if (insertedCar != null) {
            refresh();
            me.addInfoMessage("operation.occurred");
            me.redirect("/parking/car.xhtml");
        } else {
            me.addInfoMessage("operation.not.occurred");
        }

    }

//    public Filter<?> getCarIPFilterImpl() {
//        return new Filter<Car>() {
//            public boolean accept(Car car) {
//                return carIPFilter == null || carIPFilter.length() == 0 || car.getIp().startsWith(carIPFilter.toLowerCase());
//            }
//        };
//    }
//
//    public Filter<?> getPcNameFilterImpl() {
//        return new Filter<Car>() {
//            public boolean accept(Car car) {
//                return carNameFilter == null || carNameFilter.length() == 0 || car.getName().toLowerCase().contains(carNameFilter.toLowerCase());
//            }
//        };
//    }
//
//    public Filter<?> getPcLocationFilterImpl() {
//        return new Filter<Car>() {
//            public boolean accept(Car car) {
//                return carLocationFilter == null || carLocationFilter.length() == 0 || car.getLocation().getTitleText().toLowerCase().contains(carLocationFilter.toLowerCase());
//            }
//        };
//    }


//    public void selectForEdit() {
//        currentCar = carList.getRowData();
//        setSelectRow(true);
//    }


    public UserManagementAction getMe() {
        return me;
    }

    public void setMe(UserManagementAction me) {
        this.me = me;
    }

    public CarServiceImpl getCarService() {
        return carService;
    }

    public void setCarService(CarServiceImpl carService) {
        this.carService = carService;
    }

    public List<Car> getCarList() {
        return carList;
    }

    public void setCarList(List<Car> carList) {
        this.carList = carList;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public boolean isDisableFields() {
        return disableFields;
    }

    public void setDisableFields(boolean disableFields) {
        this.disableFields = disableFields;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public Car getCurrentCar() {
        return currentCar;
    }

    public void setCurrentCar(Car currentCar) {
        this.currentCar = currentCar;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Set<Car> getSelectedCars() {
        return selectedCars;
    }

    public void setSelectedCars(Set<Car> selectedCars) {
        this.selectedCars = selectedCars;
    }

    public boolean isSelectRow() {
        return selectRow;
    }

    public void setSelectRow(boolean selectRow) {
        this.selectRow = selectRow;
    }

    public String getCarNameFilter() {
        return carNameFilter;
    }

    public void setCarNameFilter(String carNameFilter) {
        this.carNameFilter = carNameFilter;
    }

    public String getCarLocationFilter() {
        return carLocationFilter;
    }

    public void setCarLocationFilter(String carLocationFilter) {
        this.carLocationFilter = carLocationFilter;
    }

}