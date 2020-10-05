package ua.com.nikiforov.models.timetables;

import ua.com.nikiforov.models.persons.Student;
import ua.com.nikiforov.services.TimetableService;

public class StudentsTimtable extends Timetable {

    private Student student;

    public StudentsTimtable(Student student) {
        timetableService = new TimetableService();
        this.student = student;
    }

    @Override
    public void setDayTimtable() {
        dayTimetable = timetableService.getStudentsDayTimetable(student);

    }

    @Override
    public void setMonthTimetable() {
        monthTimetable = timetableService.getStudentsMonthTimetable(student);
    }

}
