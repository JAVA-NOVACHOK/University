package ua.com.nikiforov.services.timetables;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ua.com.nikiforov.dao.timetables.TimetableDAO;
import ua.com.nikiforov.models.timetable.Timetable;

@Service
public class TeachersTimetableService extends PersonalTimetable {

    private TimetableDAO teachersTimetable;

    @Autowired
    public TeachersTimetableService(@Qualifier("teachersTimetable") TimetableDAO teachersTimetableDAO) {
        this.teachersTimetable = teachersTimetableDAO;
    }

    @Override
    public List<DayTimetable>  getDayTimetable(String date, long teacherId) {
        List<Timetable> allTimetablesList = teachersTimetable.getDayTimetable(date, teacherId);
        return createMonthTimetable(allTimetablesList);
    }

    @Override
    public List<DayTimetable> getMonthTimetable(String date, long teacherId) {
        List<Timetable> allTimetablesList = teachersTimetable.getMonthTimetable(date, teacherId);
        return createMonthTimetable(allTimetablesList);
    }

}
