package ua.com.nikiforov.models.person;

import ua.com.nikiforov.models.Timetable;

public class Student extends Person {

    private int groupId;
    private Timetable timetable;
  

    public Student(int id, String firstName, String lastName) {
        super(id, firstName, lastName);
    }

    public int getGroup() {
        return groupId;
    }

    public void setGroup(int groupId) {
        this.groupId = groupId;
    }

    public Timetable getTimetable() {
        return timetable;
    }

    public void setTimetable(Timetable timetable) {
        this.timetable = timetable;
    }

}
