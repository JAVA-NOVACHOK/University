package ua.com.nikiforov.models;

import java.util.ArrayList;
import java.util.List;
import ua.com.nikiforov.models.person.Teacher;

public class Subject {

    private int id;
    private String name;
    private List<Teacher> teachers;

    public Subject(int id, String name) {
        this.id = id;
        this.name = name;
        teachers = new ArrayList<>();
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

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<Teacher> teachers) {
        this.teachers = teachers;
    }

}
