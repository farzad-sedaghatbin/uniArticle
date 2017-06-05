package ir.university.toosi.tms.model.service.personnel;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.tms.model.dao.personnel.PersonDAOImpl;
import ir.university.toosi.tms.model.entity.EventLogType;
import ir.university.toosi.tms.model.entity.GatewayPerson;
import ir.university.toosi.tms.model.entity.User;
import ir.university.toosi.tms.model.entity.personnel.Job;
import ir.university.toosi.tms.model.entity.personnel.Person;
import ir.university.toosi.tms.model.entity.rule.RulePackage;
import ir.university.toosi.tms.model.service.EventLogServiceImpl;
import ir.university.toosi.tms.model.service.GatewayPersonServiceImpl;
import ir.university.toosi.tms.model.service.UserServiceImpl;
import ir.university.toosi.tms.util.EventLogManager;

import javax.ejb.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class PersonServiceImpl<T extends Person> {

    @EJB
    private PersonDAOImpl personDAO;
    @EJB
    private EventLogServiceImpl eventLogService;
    @EJB
    private UserServiceImpl userService;
    @EJB
    private JobServiceImpl jobService;
    @EJB
    private CardServiceImpl cardService;
    @EJB
    private GatewayPersonServiceImpl gatewayPersonService;

    public T findById(long id) {
        try {
            return (T) personDAO.findById(id);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean exist(String personNo,String nationalCode) {
        try {
            return personDAO.exist(personNo,nationalCode);
        } catch (Exception e) {
            return false;
        }
    }

    public long getMaximumId() {
        try {
            return personDAO.maximumId("Person.maximumId", true);
        } catch (Exception e) {
            return 1;
        }
    }

    public List<T> getAllPerson() {
        try {
            return (List<T>) personDAO.findAll("Person.list", true);
        } catch (Exception e) {
            return null;
        }
    } public List<Long> getAllPersonID() {
        try {
            return  personDAO.getAllPersonID();
        } catch (Exception e) {
            return null;
        }
    }    public List<T> getAllPersonModel() {
        try {
            return (List<T>) personDAO.allModel();
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> getDeleted() {
        try {
            return (List<T>) personDAO.deleted();
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> getPersonByName(String name) {
        try {
            return (List<T>) personDAO.findByName(name);
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> getPersonByLastName(String lastName) {
        try {
            return (List<T>) personDAO.findByLastName(lastName);
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> getPersonByNationalCode(String nationalCode) {
        try {
            return (List<T>) personDAO.findByNationalCode(nationalCode);
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> findByOrganAndRulePackage(String organId, String rulePackageId) {
        try {
            return (List<T>) personDAO.findByOrganAndRulePackage(organId, rulePackageId);
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> findByRulePackage(Long rulePackageId) {
        try {
            return (List<T>) personDAO.findByRulePackage(rulePackageId);
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> findWithRulePackage() {
        try {
            List<T> persons = new ArrayList<>();
            for (T person : getAllPerson()) {
                if (person.getRulePackage() != null || (person.getOrganRef() != null) && person.getOrganRef().getRulePackage() != null) {
                    persons.add(person);
                }
            }
            return persons;
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> findByOrgan(Long organId) {
        try {
            return (List<T>) personDAO.findByOrgan(organId);
        } catch (Exception e) {
            return null;
        }
    }

    public T getPersonByPersonnelNo(String personnelNo) {
        try {
            return (T) personDAO.findByPersonnelNo(personnelNo);
        } catch (Exception e) {
            return null;
        }
    }  public T getPersonByPersonOtherId(String personOtherId) {
        try {
            return (T) personDAO.findByPersonOtherId(personOtherId);
        } catch (Exception e) {
            return null;
        }
    }

    public List<Object[]> getPersonByPersonnelNumber() {
        try {
            return (List<Object[]>) personDAO.findByPersonnelNumber();
        } catch (Exception e) {
            return null;
        }
    }

    private Person getPerson(Object[] obj) {
        Person person = new Person();
        person.setId(((BigInteger)obj[0]).longValue());
        RulePackage rulePackage = new RulePackage();
        rulePackage.setId(((BigInteger)obj[1]).longValue());
        person.setRulePackage(rulePackage);
        return person;
    }


    public String deletePerson(T entity) {
        try {
            List<User> users = userService.findByPerson(entity.getId());
            if (users != null && users.size() != 0)
                return new ObjectMapper().writeValueAsString("REL_USER_PERSON");

            Job job = jobService.findByPersonId(entity.getId());
            jobService.deleteJob(job);

            List<GatewayPerson> gatewayPersons = gatewayPersonService.findByPersonId(entity.getId());
            for (GatewayPerson gatewayPerson : gatewayPersons) {
                gatewayPersonService.deleteGatewayPerson(gatewayPerson);
            }
            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), Person.class.getSimpleName(), EventLogType.DELETE, entity.getEffectorUser());
            personDAO.delete(findById(entity.getId()));
            return "operation.occurred";
        } catch (Exception e) {
            e.printStackTrace();
            return "FALSE";
        }
    }


    public T createPerson(T entity) {
        try {
            T t = (T) personDAO.create(entity);
            EventLogManager.eventLog(eventLogService, String.valueOf(t.getId()), Person.class.getSimpleName(), EventLogType.ADD, entity.getEffectorUser());
            return t;
        } catch (Exception e) {
            return null;
        }
    }


    public boolean editPerson(T entity) {
        try {
            Person old = findById(entity.getId());
            Person newPerson = new Person();
            newPerson.setFinger(old.getFinger());
            newPerson.setRulePackage(old.getRulePackage());
            newPerson.setName(old.getName());
            newPerson.setAddress(old.getAddress());
            newPerson.setEmail(old.getEmail());
            newPerson.setLastName(old.getLastName());
            newPerson.setMobile(old.getMobile());
            newPerson.setWorkStation(old.getWorkStation());
            newPerson.setPin(old.getPin());
            newPerson.setPicture(old.getPicture());
            newPerson.setPhone(old.getPhone());
            newPerson.setPersonStatus(old.getPersonStatus());
            newPerson.setPersonnelNo(old.getPersonnelNo());
            newPerson.setPersonOtherId(old.getPersonOtherId());
            newPerson.setPassword(old.getPassword());
            newPerson.setOrganRef(old.getOrganRef());
            newPerson.setNationalCode(old.getNationalCode());
            newPerson.setStatus("o," + entity.getEffectorUser());
            personDAO.createOld(newPerson);


            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), Person.class.getSimpleName(), EventLogType.EDIT, entity.getEffectorUser());
            personDAO.update(entity);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<Person> query(String query) {
        return personDAO.queryPerson(query);
    }

    public List<Person> withCard() {
        return personDAO.withCard();
    }

    public List<Person> withOutCard() {
        return personDAO.withOutCard();
    }

    public List<Person> getAllPerson(int first,int pageIndex){
        return personDAO.getAllPerson(first,pageIndex);
    }

    public Long countOfAll(){
        return personDAO.countOfAll();
    }

}