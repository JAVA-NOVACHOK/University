package ua.com.nikiforov.models;

import java.util.ArrayList;
import java.util.List;
import ua.com.nikiforov.models.persons.Student;
import ua.com.nikiforov.models.persons.Teacher;

public class University {
    
    private List<Student> allStudents;
    private List<Teacher> allTeachers;
    private List<Subject> allSubjects;
    private List<Group> allGroups;
    
    public University() {
        allStudents = new ArrayList<>();
        allTeachers = new ArrayList<>();
        allSubjects = new ArrayList<>();
        allGroups = new ArrayList<>();
    }

    public List<Student> getAllStudents() {
        return allStudents;
    }

    public void setAllStudents(List<Student> allStudents) {
        this.allStudents = allStudents;
    }

    public List<Teacher> getAllTeachers() {
        return allTeachers;
    }

    public void setAllTeachers(List<Teacher> allTeachers) {
        this.allTeachers = allTeachers;
    }

    public List<Subject> getAllSubjects() {
        return allSubjects;
    }

    public void setAllSubjects(List<Subject> allSubjects) {
        this.allSubjects = allSubjects;
    }

    public List<Group> getAllGroups() {
        return allGroups;
    }

    public void setAllGroups(List<Group> allGroups) {
        this.allGroups = allGroups;
    }

}
