<<<<<<< HEAD
package ua.com.nikiforov.services.timetables;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
        return timetableDAO.getTimetableById(id);
    }

    @Override
    public Timetable getTimetableByLessonPersonTimePeriod(long lessonId, long teacherId, String stringDate,
            Period period) {
        return timetableDAO.getTimetableByLessonPersonTimePeriod(lessonId, teacherId, stringDate, period);
    }

    @Override
    public List<Timetable> getAllTimetables() {
        return timetableDAO.getAllTimetables();
    }

    @Override
    public boolean updateTimetable(long lessonId, long teacherId, String date, Period period, long id) {
        return timetableDAO.updateTimetable(lessonId, teacherId, date, period, id);
    }

    @Override
    public boolean deleteTimetableById(long id) {
        return timetableDAO.deleteTimetableById(id);
    }

    @Override
    public List<Timetable> getDayTimetable(String date, long teacherId) {
        return timetableDAO.getDayTimetable(date, teacherId);
    }

    @Override
    public List<Timetable> getMonthTimetable(String date, long teacherId) {
        return timetableDAO.getMonthTimetable(date, teacherId);
    }

}
=======
package ua.com.nikiforov.services.timetables;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
        return timetableDAO.getTimetableById(id);
    }

    @Override
    public Timetable getTimetableByLessonPersonTimePeriod(long lessonId, long teacherId, String stringDate,
            Period period) {
        return timetableDAO.getTimetableByLessonPersonTimePeriod(lessonId, teacherId, stringDate, period);
    }

    @Override
    public List<Timetable> getAllTimetables() {
        return timetableDAO.getAllTimetables();
    }

    @Override
    public boolean updateTimetable(long lessonId, long teacherId, String date, Period period, long id) {
        return timetableDAO.updateTimetable(lessonId, teacherId, date, period, id);
    }

    @Override
    public boolean deleteTimetableById(long id) {
        return timetableDAO.deleteTimetableById(id);
    }

    @Override
    public List<Timetable> getDayTimetable(String date, long teacherId) {
        return timetableDAO.getDayTimetable(date, teacherId);
    }

    @Override
    public List<Timetable> getMonthTimetable(String date, long teacherId) {
        return timetableDAO.getMonthTimetable(date, teacherId);
    }

}
>>>>>>> refs/remotes/origin/master
