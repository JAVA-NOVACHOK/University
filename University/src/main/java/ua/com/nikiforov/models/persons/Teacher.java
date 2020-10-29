package ua.com.nikiforov.models.persons;

import java.util.ArrayList;
import java.util.List;

public class Teacher extends Person {

    private List<Integer> subjectIds;

    public Teacher() {
        subjectIds = new ArrayList<>();
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
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((subjectIds == null) ? 0 : subjectIds.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        Teacher other = (Teacher) obj;
        if (subjectIds == null) {
            if (other.subjectIds != null)
                return false;
        } else if (!subjectIds.equals(other.subjectIds))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "subjectIds=" + subjectIds + ", getId()=" + getId() + ", getFirstName()=" + getFirstName()
                + ", getLastName()=" + getLastName();
    }

}
