package ua.com.nikiforov.models.person;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import ua.com.nikiforov.models.Group;
import ua.com.nikiforov.models.Lesson;
import ua.com.nikiforov.models.Subject;
import ua.com.nikiforov.models.Timetable;

public class Student extends Person {

    private Group group;
    private Timetable timetable;
    private List<Lesson> lessons;
    private Set<Subject> assignedSubjects;

    public Student(int id, String firstName, String lastName) {
        super(id, firstName, lastName);
        lessons = new ArrayList<>();
        assignedSubjects = new HashSet<>();
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Timetable getTimetable() {
        return timetable;
    }

    public void setTimetable(Timetable timetable) {
        this.timetable = timetable;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    public Set<Subject> getAssignedSubjects() {
        return assignedSubjects;
    }

    public void setAssignedSubjects(Set<Subject> assignedSubjects) {
        this.assignedSubjects = assignedSubjects;
    }

}
