package ua.com.nikiforov.services.timetables;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ua.com.nikiforov.dao.timetables.StudentsTimetableDAOImpl;
import ua.com.nikiforov.dao.timetables.TimetableDAO;
import ua.com.nikiforov.models.timetable.Timetable;

@Service
public class StudentTimetableService implements PersonalTimetable {
    
    @Autowired
    @Qualifier("studentTimtableDao")
    private TimetableDAO timetable;

    @Override
    public List<Timetable> getDayTimetable() {
        return null;
    }

    @Override
    public List<Timetable> getMonthTimetable() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean addTimetable(long lessonId, long teacherId, Instant time) {
        // TODO Auto-generated method stub
        return false;
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
    public boolean updateTimetable(long lessonId, long teacherId, Instant time, long id) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean deleteTimetableById(long id) {
        // TODO Auto-generated method stub
        return false;
    }

}
