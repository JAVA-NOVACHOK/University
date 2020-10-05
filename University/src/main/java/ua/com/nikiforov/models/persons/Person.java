package ua.com.nikiforov.models.persons;

import java.util.List;
import ua.com.nikiforov.models.Lesson;
import ua.com.nikiforov.models.timetables.Timetable;

public abstract class Person {

    protected int id;
    protected Timetable timetable;
    private String firstName;
    private String lastName;

    public Person(int id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Lesson> getDayTimetable() {
        return timetable.getDayTimetable();
    }

    public List<Lesson> getMonthTimetable() {
        return timetable.getMonthTimetable();
    }

}
