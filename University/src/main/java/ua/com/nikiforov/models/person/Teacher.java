package ua.com.nikiforov.models.person;

import java.util.ArrayList;
import java.util.List;
import ua.com.nikiforov.models.Group;
import ua.com.nikiforov.models.Subject;
import ua.com.nikiforov.models.Timetable;

public class Teacher {

    private Subject teachersSubject;
    private List<Group> assignedGroups;
    private Timetable timetable;

    public Teacher(Subject teachersSubject) {
        this.teachersSubject = teachersSubject;
        assignedGroups = new ArrayList<>();
        timetable = new Timetable();
    }

    public Subject getTeachersSubject() {
        return teachersSubject;
    }

    public void setTeachersSubject(Subject teachersSubject) {
        this.teachersSubject = teachersSubject;
    }

    public List<Group> getAssignedGroups() {
        return assignedGroups;
    }

    public void setAssignedGroups(List<Group> assignedGroups) {
        this.assignedGroups = assignedGroups;
    }

    public Timetable getTimetable() {
        return timetable;
    }

    public void setTimetable(Timetable timetable) {
        this.timetable = timetable;
    }
    
}
