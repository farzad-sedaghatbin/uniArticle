
package ir.university.toosi.tms.readerwrapper;

import java.io.Serializable;

public class PersonHolder implements Serializable{


    ir.university.toosi.tms.readerwrapper.Person[] persons;

    public PersonHolder() {
    }

    public Person[] getPersons() {
        return persons;
    }

    public void setPersons(Person[] persons) {
        this.persons = persons;
    }
}
