package ua.com.nikiforov.services.timetables;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ua.com.nikiforov.dao.timetables.StudentsTimetableDAOImpl;
import ua.com.nikiforov.dao.timetables.TimetableDAO;
import ua.com.nikiforov.models.timetable.Timetable;

@Service
public class StudentTimetableService implements PersonalTimetable {

    private TimetableDAO timetableDAO;

    @Autowired
    public StudentTimetableService(@Qualifier("studentTimtableDao") TimetableDAO timetableDAO) {
        this.timetableDAO = timetableDAO;
    }

    @Override
    public boolean addTimetable(long lessonId, long teacherId, String date, Period period) {
        return timetableDAO.addTimetable(lessonId, teacherId, date, period);
    }

    @Override
    public Timetable getTimetableById(long id) {
        return timetableDAO.getTimetableById(id);
    }

    @Override
    public List<Timetable> getAllTimetables() {
        return timetableDAO.getAllTimetables();
    }

    @Override
    public boolean updateTimetable(long lessonId, long teacherId, String date, Period period,  long id) {
        return timetableDAO.updateTimetable(lessonId, teacherId, date, period, id);
    }

    @Override
    public boolean deleteTimetableById(long id) {
       return timetableDAO.deleteTimetableById(id);
    }

    @Override
    public List<Timetable> getDayTimetable() {
        return null;
    }

    @Override
    public List<Timetable> getMonthTimetable() {
        // TODO Auto-generated method stub
        return null;
    }

}
