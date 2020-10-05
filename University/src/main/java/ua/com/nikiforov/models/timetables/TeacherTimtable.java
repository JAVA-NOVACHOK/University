package ua.com.nikiforov.models.timetables;

import ua.com.nikiforov.models.persons.Teacher;

public class TeacherTimtable extends Timetable {

    private Teacher teacher;

    public TeacherTimtable(Teacher teacher) {
        this.teacher = teacher;
    }

    @Override
    public void setDayTimtable() {
        timetableService.getTeachersDayTimetable(teacher);
    }

    @Override
    public void setMonthTimetable() {
        timetableService.getTeachersMonthTimetable(teacher);
    }

}
