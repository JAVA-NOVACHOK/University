package ua.com.nikiforov.models;

import java.util.ArrayList;
import java.util.List;

public class Subject {

    private int id;
    private String name;
    private List<Integer> subjectTeacherIds;

    public Subject(int id, String name) {
        this.id = id;
        this.name = name;
        subjectTeacherIds = new ArrayList<>();
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

    public List<Integer> getSubjectTeachersIds() {
        return subjectTeacherIds;
    }

    public void addSubjectTeacherId(int subjectTeacherId) {
        subjectTeacherIds.add(subjectTeacherId);
    }

}
