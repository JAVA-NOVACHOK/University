package ua.com.nikiforov.models.persons;

import java.util.ArrayList;
import java.util.List;

import ua.com.nikiforov.models.Subject;

public class Teacher extends Person {

    private List<Subject> subjects;

    public Teacher() {
        subjects = new ArrayList<>();
    }

    public Teacher(long id, String firstName, String lastName) {
        super(id, firstName, lastName);
        subjects = new ArrayList<>();
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects.addAll(subjects);
    }
    
    public boolean addSubject(Subject subject) {
        return subjects.add(subject);
    }

    @Override
    public String toString() {
        return "[Teacher " + super.toString() + "subjects = " + subjects + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((subjects == null || subjects.isEmpty()) ? 0 : subjects.hashCode());
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
        if (subjects == null ) {
            if (other.subjects != null )
                return false;
        } else if (!subjects.equals(other.subjects))
            return false;
        return true;
    }
    
    
    

    
}
