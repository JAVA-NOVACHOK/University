package ua.com.nikiforov.models;

import java.util.ArrayList;
import java.util.List;

public class Subject {

    private int id;
    private String name;
    private List<Long> teachersIds;

    public Subject() {
    }

    public Subject(int id, String name) {
        this.id = id;
        this.name = name;
        teachersIds = new ArrayList<>();
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

    public List<Long> getSubjectTeachersIds() {
        return teachersIds;
    }

    public void setSubjectTeacherIds(List<Long> subjectTeacherIds) {
        this.teachersIds = subjectTeacherIds;
    }

    

}
