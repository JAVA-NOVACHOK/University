package ua.com.nikiforov.services;

import java.util.ArrayList;
import java.util.List;
import ua.com.nikiforov.models.Lesson;
import ua.com.nikiforov.models.persons.Student;
import ua.com.nikiforov.models.persons.Teacher;

public class TimetableService {

    public List<Lesson> getStudentsDayTimetable(Student student) {
        return new ArrayList<>();
    }

    public List<Lesson> getStudentsMonthTimetable(Student student) {
        return new ArrayList<>();
    }

    public List<Lesson> getTeachersDayTimetable(Teacher teacher) {
        return new ArrayList<>();
    }

    public List<Lesson> getTeachersMonthTimetable(Teacher teacher) {
        return new ArrayList<>();
    }

}
