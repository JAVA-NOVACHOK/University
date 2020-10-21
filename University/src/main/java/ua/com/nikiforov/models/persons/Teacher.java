package ua.com.nikiforov.models.persons;

import java.util.ArrayList;
import java.util.List;

public class Teacher extends Person {

    private List<Integer> subjectIds;

    public Teacher() {
    }

    public Teacher(long id, String firstName, String lastName) {
        super(id, firstName, lastName);
        subjectIds = new ArrayList<>();
    }

    public List<Integer> getSubjectIds() {
        return subjectIds;
    }

    public void setSubjectIds(List<Integer> subjectIds) {
        this.subjectIds = subjectIds;
    }

    @Override
    public String toString() {
        return "subjectIds=" + subjectIds + ", getId()=" + getId() + ", getFirstName()=" + getFirstName()
                + ", getLastName()=" + getLastName();
    }

}
