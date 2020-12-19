package ua.com.nikiforov.models;

import java.util.ArrayList;
import java.util.List;
import ua.com.nikiforov.models.persons.Teacher;

import javax.persistence.*;

@Entity
@Table(name="subjects",uniqueConstraints = @UniqueConstraint(columnNames = {"subject_name"}))
public class Subject implements Comparable<Subject>{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "subject_id")
    private int id;
    @Column(name = "subject_name")
    private String name;

    @ManyToMany(mappedBy = "subjects",fetch = FetchType.EAGER)
    private List<Teacher> teachers;

    public Subject() {

    }

    public Subject(int id, String name) {
        this.id = id;
        this.name = name;

    }

    public Subject(int id, String name, List<Teacher> teachers) {
        this.id = id;
        this.name = name;
        this.teachers = new ArrayList<>(teachers);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public void addTeacher(Teacher teacher) {
        teachers.add(teacher);
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<Teacher> teachers) {
        this.teachers.addAll(teachers);
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((teachers == null || teachers.isEmpty()) ? 0 : teachers.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Subject other = (Subject) obj;
        if (id != other.id)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (teachers == null) {
            if (other.teachers != null)
                return false;
        } 
        return true;
    }

    @Override
    public String toString() {
        return "Subject [id=" + id + ", name=" + name + ", teachers=" + teachers + "]";
    }

    @Override
    public int compareTo(Subject o) {
        return this.getName().compareTo(o.getName()) ;
    }

}
