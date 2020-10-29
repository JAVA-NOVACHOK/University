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

    
}
