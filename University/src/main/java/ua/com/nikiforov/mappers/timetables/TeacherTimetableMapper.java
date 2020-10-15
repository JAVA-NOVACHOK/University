package ua.com.nikiforov.mappers.timetables;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;

import ua.com.nikiforov.models.timetable.Timetable;

import static ua.com.nikiforov.dao.SqlConstants.TeachersTimetableTable.*;

public class TeacherTimetableMapper implements RowMapper<Timetable> {

    @Override
    public Timetable mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Timetable timetable = new Timetable();
        timetable.setId(resultSet.getLong(ID));
        timetable.setLessonId(resultSet.getLong(LESSON_ID));
        timetable.setPersonId(resultSet.getLong(PERSON_ID));
        timetable.setTime(resultSet.getObject(DATE, Date.class).toInstant());
        timetable.setPeriod(resultSet.getInt(PERIOD));
        return timetable;
    }

}
