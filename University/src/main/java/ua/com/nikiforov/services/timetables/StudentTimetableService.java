package ua.com.nikiforov.services.timetables;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

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
    public boolean addTimetable(long lessonId, long studentId, String date, Period period) {
        return timetableDAO.addTimetable(lessonId, studentId, date, period);
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
    public boolean updateTimetable(long lessonId, long studentId, String date, Period period,  long id) {
        return timetableDAO.updateTimetable(lessonId, studentId, date, period, id);
    }

    @Override
    public boolean deleteTimetableById(long id) {
       return timetableDAO.deleteTimetableById(id);
    }

    @Override
    public List<Timetable> getDayTimetable(String date, long studentId) {
        return timetableDAO.getDayTimetable(date, studentId);
    }

    @Override
    public List<Timetable> getMonthTimetable(String date, long studentId) {
        return timetableDAO.getMonthTimetable(date, studentId);
    }

}
