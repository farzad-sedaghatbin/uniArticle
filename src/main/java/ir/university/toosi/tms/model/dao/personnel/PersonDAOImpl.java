package ir.university.toosi.tms.model.dao.personnel;


import ir.university.toosi.tms.model.dao.BaseDAOImpl;
import ir.university.toosi.tms.model.entity.personnel.Person;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Stateless
@LocalBean

public class PersonDAOImpl extends BaseDAOImpl<Person> {


    public Person findById(long id) {
        try {
            return (Person) em.createNamedQuery("Person.findById")
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Person> findByName(String name) {
        try {
            return (List<Person>) em.createNamedQuery("Person.findByName")
                    .setParameter("name", name)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Person> findByLastName(String lastName) {
        try {
            return (List<Person>) em.createNamedQuery("Person.findByLastName")
                    .setParameter("lastName", lastName)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Person> findByNationalCode(String nationalCode) {
        try {
            return (List<Person>) em.createNamedQuery("Person.findByNationalCode")
                    .setParameter("nationalCode", nationalCode)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public Person findByPersonnelNo(String personnelNo) {
        try {
            return (Person) em.createNamedQuery("Person.findByPersonnelNo")
                    .setParameter("personnelNo", personnelNo)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    public Person findByPersonOtherId(String personnelId) {
        try {
            return (Person) em.createNamedQuery("Person.findByPersonOtherId")
                    .setParameter("personOtherId", personnelId)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Object[]> findByPersonnelNumber() {
        try {
            return (List<Object[]>) em.createNamedQuery("Person.findByPersonnelNumber")
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Person> findByOrganAndRulePackage(String organId, String rulePackageId) {
        try {
            return (List<Person>) em.createNamedQuery("Person.findByOrganAndRulePackage")
                    .setParameter("organId", Long.valueOf(organId))
                    .setParameter("rulepackageId", Long.valueOf(rulePackageId))
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Person> findByRulePackage(Long rulePackageId) {
        try {
            return (List<Person>) em.createNamedQuery("Person.findByRulePackage")
                    .setParameter("rulepackageId", rulePackageId)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Person> findByOrgan(Long organId) {
        try {
            return Person.convert2Person((List<Object[]>) em.createNamedQuery("Person.findByOrgan")
                    .setParameter("id", organId)
                    .getResultList());
        } catch (Exception e) {
            return null;
        }
    }

    public List<Long> getAllPersonID() {
        try {
            return (List<Long>) em.createNamedQuery("Person.listID")
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    } public List<Person> allModel() {
        try {
            List<Object[]> list = (List<Object[]>) em.createNamedQuery("Person.allModel")
                    .getResultList();
            return Person.convert2Person(list);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean exist(String personNo, String nationalCode) {
        try {
            List<Person> persons = (List<Person>) em.createNamedQuery("Person.exist")
                    .setParameter("personnelNo", personNo)
                    .setParameter("nationalCode", nationalCode)
                    .getResultList();
            if (persons.size() != 0)
                return true;
            else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public List<Person> queryPerson(String query) {
        try {
            return (List<Person>) em.createQuery(query).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<Person> withCard() {
        try {
            return (List<Person>) em.createNamedQuery("Person.withCard")
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Person> withOutCard() {
        try {
            return (List<Person>) em.createNamedQuery("Person.withOutCard")
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public Long countOfAll() {
        try {
            return (Long) em.createNamedQuery("Person.countOfAll")
                    .getSingleResult();
        } catch (Exception e) {
            return 0l;
        }
    }

    public List<Person> deleted() {
        try {
            return (List<Person>) em.createNamedQuery("Person.deleted")
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }


    public List<Person> getAllPerson(int first, int pageIndex) {
        return em.createNamedQuery("Person.list").setFirstResult(first).setMaxResults(first + pageIndex).getResultList();
    }
}