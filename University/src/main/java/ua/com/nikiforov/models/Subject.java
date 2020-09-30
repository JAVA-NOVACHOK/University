package ua.com.nikiforov.models;

import java.util.ArrayList;
import java.util.List;
import ua.com.nikiforov.models.person.Teacher;

public class Subject {

    private String name;
    private String description;
    private List<Teacher> teachers;

    public Subject(String name, String description) {
        this.name = name;
        this.description = description;
        teachers = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<Teacher> teachers) {
        this.teachers = teachers;
    }
    
}
