package ua.com.nikiforov.models.persons;

import java.util.ArrayList;
import java.util.List;

import ua.com.nikiforov.models.Subject;

import javax.persistence.*;

@Entity

public class Teacher implements Comparable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "teacher_id")
    private long id;
    @Column(name="first_name")
    private String firstName;
    @Column(name="last_name")
    private String lastName;

    @ManyToMany
    @JoinTable(name="teachers_subjects",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id"))
    private List<Subject> subjects;

    public Teacher() {
        subjects = new ArrayList<>();
    }

    public Teacher(long id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        subjects = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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
        } 
        return true;
    }


    @Override
    public int compareTo(Object o) {
        Teacher teacher = (Teacher)o;
        return this.lastName.compareTo(teacher.getLastName());
    }
}
