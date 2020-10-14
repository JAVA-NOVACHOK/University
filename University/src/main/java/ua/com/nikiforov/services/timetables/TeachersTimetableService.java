package ua.com.nikiforov.services.timetables;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import ua.com.nikiforov.dao.timetables.TimetableDAO;
import ua.com.nikiforov.models.timetable.Timetable;

@Service
public class TeachersTimetableService implements PersonalTimetable {

    private TimetableDAO timetableDAO;

    @Autowired
    public TeachersTimetableService(@Qualifier("teachersTimetableDAO") TimetableDAO timetable) {
        this.timetableDAO = timetable;
    }

    @Override
    public boolean addTimetable(long lessonId, long teacherId, String date, Period period) {
        return timetableDAO.addTimetable(lessonId, teacherId, date, period);
    }

    @Override
    public Timetable getTimetableById(long id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Timetable> getAllTimetables() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean updateTimetable(long lessonId, long teacherId, String time, Period period, long id) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean deleteTimetableById(long id) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public List<Timetable> getDayTimetable() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Timetable> getMonthTimetable() {
        // TODO Auto-generated method stub
        return null;
    }

}
