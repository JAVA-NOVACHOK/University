package ua.com.nikiforov.models.person;

import java.util.ArrayList;
import java.util.List;
import ua.com.nikiforov.models.Group;
import ua.com.nikiforov.models.Lesson;
import ua.com.nikiforov.models.Subject;
import ua.com.nikiforov.models.Timetable;

public class Teacher extends Person {

    private int subjectId;
    private List<Group> assignedGroups;
    private Timetable timetable;

    public Teacher(int id, String firstName, String lastName, int subjectId) {
        super(id, firstName, lastName);
        this.subjectId = subjectId;
        assignedGroups = new ArrayList<>();
        timetable = new Timetable();
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
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

    @Override
    public List<Lesson> getDayTimetable() {
        return timetable.getTeachersDayTimetable(id);
    }

    @Override
    public List<Lesson> getMonthTimetables() {
        return timetable.getTeachersMonthTimetable(id);
    }

}
