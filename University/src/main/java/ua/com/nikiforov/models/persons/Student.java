package ua.com.nikiforov.models.persons;

import ua.com.nikiforov.models.timetables.StudentsTimtable;

public class Student extends Person {

    private int groupId;

    public Student(int id, String firstName, String lastName,int groupId) {
        super(id, firstName, lastName);
        this.groupId = groupId;
        timetable = new StudentsTimtable(this);
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroup(int groupId) {
        this.groupId = groupId;
    }

}
