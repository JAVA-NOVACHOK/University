package ua.com.nikiforov.models.persons;

import ua.com.nikiforov.models.timetables.TeacherTimtable;
import ua.com.nikiforov.models.timetables.Timetable;

public class Teacher extends Person {

    private int subjectId;
    private Timetable timetable;

    public Teacher(int id, String firstName, String lastName, int subjectId) {
        super(id, firstName, lastName);
        this.subjectId = subjectId;
        timetable = new TeacherTimtable(this);
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public Timetable getTimetable() {
        return timetable;
    }

    public void setTimetable(Timetable timetable) {
        this.timetable = timetable;
    }

   

}
