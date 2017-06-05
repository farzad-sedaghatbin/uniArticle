package ir.university.toosi.tms.model.service.personnel;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.tms.model.dao.personnel.OrganDAOImpl;
import ir.university.toosi.tms.model.entity.EventLogType;
import ir.university.toosi.tms.model.entity.Permission;
import ir.university.toosi.tms.model.entity.PermissionType;
import ir.university.toosi.tms.model.entity.personnel.Organ;
import ir.university.toosi.tms.model.entity.personnel.Person;
import ir.university.toosi.tms.model.entity.rule.RulePackage;
import ir.university.toosi.tms.model.service.EventLogServiceImpl;
import ir.university.toosi.tms.model.service.PermissionServiceImpl;
import ir.university.toosi.tms.util.EventLogManager;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Stateless
@LocalBean

public class OrganServiceImpl<T extends Organ> {

    @EJB
    private OrganDAOImpl organDAO;
    @EJB
    private PersonServiceImpl personService;
    @EJB
    private EventLogServiceImpl eventLogService;
    @EJB
    private PermissionServiceImpl permissionService;


    public T findById(long id) {
        try {
            return (T) organDAO.findById(id);
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> findByRulePackageId(Long id) {
        try {
            return (List<T>) organDAO.findByRulePackageId(id);
        } catch (Exception e) {
            return null;
        }
    }


    public List<T> getAllOrgan() {
        try {
            return (List<T>) organDAO.findAll("Organ.list", true);
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> getAllActiveOrgan() {
        try {
            return (List<T>) organDAO.findAll("Organ.active.list", true);
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> getAllActiveOrganByParent(long id) {
        try {
            return (List<T>) organDAO.findAllActiveOrganByParent(id);
        } catch (Exception e) {
            return null;
        }
    }


    public boolean existOrgan(T entity) {
        try {
            return organDAO.existOrgan(entity.getCode());
        } catch (Exception e) {
            return false;
        }
    }

    public String deleteOrgan(T entity) {
        try {
            List<Person> persons = personService.findByOrgan(entity.getId());
            if (persons != null && persons.size() != 0)
                return new ObjectMapper().writeValueAsString("REL_ORGAN_PERSON");
            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), Organ.class.getSimpleName(), EventLogType.DELETE, entity.getEffectorUser());
            organDAO.delete(entity);
            return "operation.occurred";
        } catch (Exception e) {
            return "FALSE";
        }
    }


    public T createOrgan(T entity) {
        try {
            Organ organ = organDAO.create(entity);
            Permission permission = new Permission(String.valueOf(organ.getId()), organ.getName(), PermissionType.ORGAN);
            permissionService.createPermission(permission);
            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), Organ.class.getSimpleName(), EventLogType.ADD, entity.getEffectorUser());
            return (T) entity;
        } catch (Exception e) {
            return null;
        }
    }


    public boolean editOrgan(T entity) {
        try {
            Organ oldOrgan = findById(entity.getId());
            if (entity.isInheritance()) {
                if (entity.getRulePackage() != null) {
                    List<Person> persons = personService.findByOrgan(oldOrgan.getId());
                    for (Person person : persons) {
                        person.setRulePackage(entity.getRulePackage());
                        personService.editPerson(person);
                    }
                    List<Organ> childOrgans = findChildOrgans(oldOrgan, oldOrgan.getRulePackage());
                    if (childOrgans != null && childOrgans.size() != 0) {
                        for (Organ childOrgan : childOrgans) {

                            childOrgan.setRulePackage(entity.getRulePackage());
                            organDAO.update(childOrgan);
                             persons = personService.findByOrgan(childOrgan.getId());
                            for (Person person : persons) {
                                person.setRulePackage(entity.getRulePackage());
                                personService.editPerson(person);
                            }
                            if (childOrgan.getChildOrgans() != null && childOrgan.getChildOrgans().size() != 0) {
                                for (Organ organ : childOrgan.getChildOrgans()) {
                                    organ.setRulePackage(entity.getRulePackage());
                                    organDAO.update(organ);
                                    persons = personService.findByOrgan(organ.getId());
                                    for (Person person : persons) {
                                        person.setRulePackage(entity.getRulePackage());
                                        personService.editPerson(person);
                                    }
                                }
                            }
                        }
                    } else {
                      persons = personService.findByOrgan(oldOrgan.getId());
                        for (Person person : persons) {
                            person.setRulePackage(entity.getRulePackage());
                            personService.editPerson(person);
                        }
                    }
                }
            }

            Organ newOrgan = new Organ();
            newOrgan.setTitle(oldOrgan.getTitle());
            newOrgan.setName(oldOrgan.getName());
            newOrgan.setCode(oldOrgan.getCode());
            newOrgan.setInheritance(oldOrgan.isInheritance());
            newOrgan.setOrganType(oldOrgan.getOrganType());
            newOrgan.setParentOrgan(oldOrgan.getParentOrgan());
            newOrgan.setRulePackage(oldOrgan.getRulePackage());
            newOrgan.setStatus("o," + entity.getEffectorUser());
            organDAO.createOld(newOrgan);


            organDAO.update(entity);

            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), Organ.class.getSimpleName(), EventLogType.EDIT, entity.getEffectorUser());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private List<Organ> findChildOrgans(Organ organ, RulePackage rulePackage) {

        Stack<Organ> organStack = new Stack<>();
        List<Organ> addedList = new ArrayList<>();
        List<Organ> organList = (List<Organ>) getAllActiveOrganByParent(organ.getId());
        for (Organ organ1 : organList) {
            if ((rulePackage == null && organ1.getRulePackage() == null) || (rulePackage != null && organ1.getRulePackage() != null && organ1.getRulePackage().getId() == rulePackage.getId())) {
                organStack.push(organ1);
            }
        }

        while (!organStack.isEmpty()) {
            Organ removeOrgan = organStack.pop();
            addedList.add(removeOrgan);
            if (!removeOrgan.isInheritance())
                continue;
            organList = (List<Organ>) getAllActiveOrganByParent(removeOrgan.getId());
            for (Organ organ1 : organList) {
                if ((rulePackage == null && organ1.getRulePackage() == null) || (organ1.getRulePackage() != null && organ1.getRulePackage().getId() == rulePackage.getId())) {
                    organStack.push(organ1);
                }
            }
        }

        return addedList;
    }
}